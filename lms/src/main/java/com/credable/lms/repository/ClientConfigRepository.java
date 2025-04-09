package com.credable.lms.repository;

import com.credable.lms.domain.ClientConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClientConfigRepository extends JpaRepository<ClientConfig, Long> {
    Optional<ClientConfig> findByUrl(String url);
    boolean existsByUrl(String url);
} 