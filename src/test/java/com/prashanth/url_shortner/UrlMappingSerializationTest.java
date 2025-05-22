package com.prashanth.url_shortner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.prashanth.url_shortner.model.UrlMapping;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UrlMappingSerializationTest {

    @Test
    public void testUrlMappingSerialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setId(1L);
        urlMapping.setShortUrl("6YqLa4");
        urlMapping.setLongUrl("https://example.com");
        urlMapping.setActive(true);
        urlMapping.setExpiresAt(OffsetDateTime.parse("2025-05-22T18:33:00Z"));
        urlMapping.setCreatedAt(OffsetDateTime.now());
        urlMapping.setUpdatedAt(OffsetDateTime.now());
        urlMapping.setClickCount(0);

        String json = mapper.writeValueAsString(urlMapping);
        System.out.println(json);

        UrlMapping deserialized = mapper.readValue(json, UrlMapping.class);
        assertEquals(urlMapping.getShortUrl(), deserialized.getShortUrl());
    }
}
