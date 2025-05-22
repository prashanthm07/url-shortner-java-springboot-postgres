package com.prashanth.url_shortner.service;

import com.prashanth.url_shortner.dto.UrlRequestDto;
import com.prashanth.url_shortner.dto.UrlResponseDto;

public interface UrlService {
    UrlResponseDto shortenUrl(UrlRequestDto urlRequestDto);
    String getOriginalUrl(String shortUrl);
}
