package com.prashanth.url_shortner.service;

import com.prashanth.url_shortner.dto.UrlRequest;
import com.prashanth.url_shortner.dto.UrlResponse;

public interface UrlService {
    UrlResponse shortenUrl(UrlRequest urlRequest);
    String getOriginalUrl(String shortUrl);
}
