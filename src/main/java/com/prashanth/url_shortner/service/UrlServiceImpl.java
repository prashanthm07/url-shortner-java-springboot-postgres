package com.prashanth.url_shortner.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.prashanth.url_shortner.dto.UrlRequestDto;
import com.prashanth.url_shortner.dto.UrlResponseDto;
import com.prashanth.url_shortner.exception.ErrorMessages;
import com.prashanth.url_shortner.exception.UrlExpiredException;
import com.prashanth.url_shortner.exception.UrlNotActiveException;
import com.prashanth.url_shortner.exception.UrlNotFoundException;
import com.prashanth.url_shortner.model.UrlMapping;
import com.prashanth.url_shortner.repository.UrlMappingRepository;
import com.prashanth.url_shortner.util.Base62Encoder;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UrlServiceImpl implements UrlService {

    private static final long CACHE_TTL_SECONDS = 60; // 1 minute fixed TTL

    @Autowired
    private UrlMappingRepository urlMappingRepository;

    @Autowired
    private Base62Encoder base62Encoder;

    @Override
    @Transactional
    public UrlResponseDto shortenUrl(UrlRequestDto urlRequestDto) {

        if(urlRequestDto.getExpiration() != null && urlRequestDto.getExpiration().isBefore(java.time.OffsetDateTime.now())) {
            throw new IllegalArgumentException(ErrorMessages.URL_EXPIRY_INVALID);
        }

        UrlMapping urlMapping = UrlMapping.builder()
                .longUrl(urlRequestDto.getOriginalUrl())
                .expiresAt(urlRequestDto.getExpiration())
                .isActive(true)
                .build();
        urlMappingRepository.save(urlMapping);
        String shortUrl = base62Encoder.encode();
        // Collision check
        while (urlMappingRepository.existsByShortUrl(shortUrl)){
            shortUrl = base62Encoder.encode();
        }
        urlMapping.setShortUrl(shortUrl);
        urlMappingRepository.save(urlMapping);
        return UrlResponseDto.builder()
                .id(urlMapping.getId())
                .shortUrl(shortUrl)
                .originalUrl(urlMapping.getLongUrl())
                .isActive(urlMapping.isActive())
                .expiresAt(urlMapping.getExpiresAt())
                .createdAt(urlMapping.getCreatedAt())
                .updatedAt(urlMapping.getUpdatedAt())
                .build();
    }

    @Override
    @Cacheable(value = "urlCache", key = "#shortUrl")
    public String getOriginalUrl(String shortUrl) {
        if(shortUrl == null || shortUrl.isEmpty()) {
            throw new IllegalArgumentException("Short URL cannot be null or empty");
        }
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (urlMapping == null) {
            throw new UrlNotFoundException(ErrorMessages.URL_NOT_FOUND);
        }
        if(urlMapping.getExpiresAt() != null && urlMapping.getExpiresAt().isBefore(java.time.OffsetDateTime.now())) {
            throw new UrlExpiredException(ErrorMessages.URL_EXPIRED);
        }
        if(!urlMapping.isActive()) {
            throw new UrlNotActiveException(ErrorMessages.URL_NOT_ACTIVE);
        }
        urlMapping.setClickCount(urlMapping.getClickCount() + 1);
        urlMappingRepository.save(urlMapping);
        return urlMapping.getLongUrl();
    }
}
