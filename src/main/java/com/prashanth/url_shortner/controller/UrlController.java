package com.prashanth.url_shortner.controller;

import com.prashanth.url_shortner.dto.UrlRequest;
import com.prashanth.url_shortner.dto.UrlResponse;
import com.prashanth.url_shortner.service.UrlService;
import com.prashanth.url_shortner.util.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/url")
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<UrlResponse> shortenUrl(@RequestBody  UrlRequest urlRequest) {
        UrlResponse urlResponse = urlService.shortenUrl(urlRequest);
        return ResponseEntity.ok(urlResponse);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> getOriginalUrl(@PathVariable String shortUrl) {
        if(!UrlValidator.isValid(shortUrl)){
            throw new IllegalArgumentException("Invalid URL format");
        }
        String originalUrl = urlService.getOriginalUrl(shortUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", originalUrl);
        return ResponseEntity.status(302)
                .headers(headers)
                .build();
    }

}
