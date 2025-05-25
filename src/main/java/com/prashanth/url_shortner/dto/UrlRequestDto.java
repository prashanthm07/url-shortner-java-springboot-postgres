package com.prashanth.url_shortner.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

import java.time.OffsetDateTime;

@Data
@Getter
public class UrlRequestDto {
    @NotBlank(message = "url is required")
    private String originalUrl;
    private OffsetDateTime expiration;
}
