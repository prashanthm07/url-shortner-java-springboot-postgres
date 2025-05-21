package com.prashanth.url_shortner.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "url_mappings")
public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "short_url", unique = true)
    private String shortUrl;

    @Column(name = "long_url", nullable = false)
    private String longUrl;

    @Column(name="click_count")
    private long clickCount;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private boolean isActive;

    @Column(name="expires_at")
    private OffsetDateTime expiresAt;

    @Column(name="created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name="updated_at")
    private OffsetDateTime updatedAt;


    @PrePersist
    private void prePersist() {
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }
    @PreUpdate
    private void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

}
