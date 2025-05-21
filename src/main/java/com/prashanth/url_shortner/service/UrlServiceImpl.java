package com.prashanth.url_shortner.service;

import com.prashanth.url_shortner.dto.UrlRequest;
import com.prashanth.url_shortner.dto.UrlResponse;
import com.prashanth.url_shortner.model.UrlMapping;
import com.prashanth.url_shortner.repository.UrlMappingRepository;
import com.prashanth.url_shortner.util.Base62Encoder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrlServiceImpl implements UrlService {

    @Autowired
    private UrlMappingRepository urlMappingRepository;

    @Autowired
    private Base62Encoder base62Encoder;

    @Override
    @Transactional
    public UrlResponse shortenUrl(UrlRequest urlRequest) {
        UrlMapping urlMapping = UrlMapping.builder()
                .longUrl(urlRequest.getOriginalUrl())
                .expiresAt(urlRequest.getExpirationDate())
                .build();
        urlMappingRepository.save(urlMapping);
        String shortUrl = base62Encoder.encode(urlMapping.getId());
        while (urlMappingRepository.existsByShortUrl(shortUrl)){
            shortUrl = base62Encoder.encode(urlMapping.getId());
        }
        urlMapping.setShortUrl(shortUrl);
        return UrlResponse.builder()
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
            throw new IllegalArgumentException("Short URL not found");
        }
        urlMapping.setClickCount(urlMapping.getClickCount() + 1);
        return urlMapping.getLongUrl();
    }
}
