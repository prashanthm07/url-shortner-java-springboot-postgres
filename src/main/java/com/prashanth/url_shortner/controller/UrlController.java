package com.prashanth.url_shortner.controller;

import com.prashanth.url_shortner.dto.UrlRequestDto;
import com.prashanth.url_shortner.dto.UrlResponseDto;
import com.prashanth.url_shortner.exception.ErrorMessages;
import com.prashanth.url_shortner.exception.InvalidUrlFormatException;
import com.prashanth.url_shortner.service.UrlService;
import com.prashanth.url_shortner.util.UrlValidator;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/url")
public class UrlController {

    private static final Logger logger = LogManager.getLogger(UrlController.class);

    @Autowired
    private UrlService urlService;


    @PostMapping("/shorten")
    public ResponseEntity<UrlResponseDto> shortenUrl(@Valid @RequestBody UrlRequestDto urlRequestDto) {
        logger.info("Received request to shorten URL: {}", urlRequestDto.getOriginalUrl());
        if(!UrlValidator.isValid(urlRequestDto.getOriginalUrl())){
            throw new InvalidUrlFormatException(ErrorMessages.URL_FORMAT_INVALID);
        }
        UrlResponseDto urlResponseDto = urlService.shortenUrl(urlRequestDto);
        logger.info("Shortened URL created: {}", urlResponseDto.getShortUrl());
        return ResponseEntity.ok(urlResponseDto);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> getOriginalUrl(@PathVariable String shortUrl) {
        String originalUrl = urlService.getOriginalUrl(shortUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", originalUrl);
        return ResponseEntity.status(302)
                .headers(headers)
                .build();
    }

}
