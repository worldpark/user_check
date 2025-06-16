package com.check.user_check.repository;

import com.check.user_check.entity.RefreshTokenRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenRecord, UUID> {

    Optional<RefreshTokenRecord> findByTokenValue(String tokenValue);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void deleteByTokenValue(String tokenValue);
}
