package com.prashanth.url_shortner.service;

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
import org.springframework.stereotype.Service;

@Service
public class UrlServiceImpl implements UrlService {

    @Autowired
    private UrlMappingRepository urlMappingRepository;

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
        String shortUrl = Base62Encoder.encode();
        // Collision check
        while (urlMappingRepository.existsByShortUrl(shortUrl)){
            shortUrl = Base62Encoder.encode();
        }
        urlMapping.setShortUrl(shortUrl);
        return UrlResponseDto.builder()
                .id(urlMapping.getId())
                .shortUrl(shortUrl)
                .originalUrl(urlMapping.getLongUrl())
                .expiresAt(urlMapping.getExpiresAt())
                .createdAt(urlMapping.getCreatedAt())
                .updatedAt(urlMapping.getUpdatedAt())
                .build();
    }

    @Override
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
