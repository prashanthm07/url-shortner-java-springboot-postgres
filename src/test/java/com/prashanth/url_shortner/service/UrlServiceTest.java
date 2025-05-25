package com.prashanth.url_shortner.service;

import com.prashanth.url_shortner.dto.UrlRequestDto;
import com.prashanth.url_shortner.model.UrlMapping;
import com.prashanth.url_shortner.repository.UrlMappingRepository;
import com.prashanth.url_shortner.util.Base62Encoder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // to let junit know that this is a test class using Mockito
public class UrlServiceTest {

    

    @Mock // Mocking the UrlMappingRepository to simulate its behavior
    private UrlMappingRepository urlMappingRepository;
    @Mock // Mocking the Base62Encoder to simulate its behavior
    private Base62Encoder encoder;

    @InjectMocks // this will inject the mocks into the UrlService instance
    private UrlServiceImpl urlService;

    @Test
    void testShortenUrl() {
        String longUrl = "https://example.com";
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setLongUrl(longUrl);
        urlMapping.setExpiresAt(OffsetDateTime.now().plusSeconds(3600));

        UrlMapping savedUrlMapping = new UrlMapping();
        savedUrlMapping.setId(1L);
        savedUrlMapping.setLongUrl(longUrl);

        // Mocks
        when(urlMappingRepository.save(any(UrlMapping.class))).thenReturn(savedUrlMapping);
        when(encoder.encode()).thenReturn("6YqLa4");

        UrlRequestDto urlRequestDto = new UrlRequestDto();
        urlRequestDto.setOriginalUrl(longUrl);
        urlRequestDto.setExpiration(OffsetDateTime.now().plusSeconds(3600));
        String shortUrl = urlService.shortenUrl(urlRequestDto).getShortUrl();

        assertEquals("6YqLa4", shortUrl);
        verify(urlMappingRepository, times(2)).save(any(UrlMapping.class));
        verify(encoder).encode();
    }

    @Test
    void testGetOriginalUrl() {
        String shortUrl = "6YqLa4";
        String longUrl = "https://example.com";

        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setLongUrl(longUrl);
        urlMapping.setExpiresAt(OffsetDateTime.now().plusSeconds(3600));
        urlMapping.setActive(true);

        // Mocks
        when(urlMappingRepository.findByShortUrl(shortUrl)).thenReturn(urlMapping);

        String originalUrl = urlService.getOriginalUrl(shortUrl);

        assertEquals(longUrl, originalUrl);
        verify(urlMappingRepository).findByShortUrl(shortUrl);
    }

    @Test
    void testGetOriginalUrlWithExpiredUrl() {
        String shortUrl = "6YqLa4";

        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setExpiresAt(OffsetDateTime.now().minusSeconds(3600)); // Expired URL
        urlMapping.setActive(true);

        // Mocks
        when(urlMappingRepository.findByShortUrl(shortUrl)).thenReturn(urlMapping);

        try {
            urlService.getOriginalUrl(shortUrl);
        } catch (Exception e) {
            assertEquals("URL has expired", e.getMessage());
        }

        verify(urlMappingRepository).findByShortUrl(shortUrl);
    }
}
