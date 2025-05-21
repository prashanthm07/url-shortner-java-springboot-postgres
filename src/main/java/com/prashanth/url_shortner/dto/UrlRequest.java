package com.prashanth.url_shortner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.OffsetDateTime;

@Data
@Getter
public class UrlRequest {
    private String originalUrl;
    private OffsetDateTime expirationDate;
}
