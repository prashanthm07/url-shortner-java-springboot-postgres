package com.prashanth.url_shortner.repository;

import com.prashanth.url_shortner.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    UrlMapping findByShortUrl(String shortUrl);
    UrlMapping findByLongUrl(String longUrl);
    boolean existsByShortUrl(String shortUrl);
    boolean existsByLongUrl(String longUrl);
}
