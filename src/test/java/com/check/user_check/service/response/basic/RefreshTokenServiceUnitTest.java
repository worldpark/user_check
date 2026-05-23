package com.check.user_check.service.response.basic;

import com.check.user_check.entity.RefreshTokenRecord;
import com.check.user_check.exception.custom.DataIntegrityViolationWithCodeException;
import com.check.user_check.exception.token.refreshToken.RefreshTokenException;
import com.check.user_check.repository.RefreshTokenRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceUnitTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private RefreshTokenRepository repository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Test
    void saveWrapsDataIntegrityViolation() {
        RefreshTokenRecord token = token("refresh-token");
        when(repository.save(token)).thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> refreshTokenService.save(token))
                .isInstanceOf(DataIntegrityViolationWithCodeException.class)
                .extracting("code")
                .isEqualTo("060301");
    }

    @Test
    void findByTokenValueReturnsSavedToken() {
        RefreshTokenRecord token = token("refresh-token");
        when(repository.findByTokenValue("refresh-token")).thenReturn(Optional.of(token));

        assertThat(refreshTokenService.findByTokenValue("refresh-token")).isSameAs(token);
    }

    @Test
    void findByTokenValueThrowsWhenMissing() {
        when(repository.findByTokenValue("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> refreshTokenService.findByTokenValue("missing"))
                .isInstanceOf(RefreshTokenException.class);
    }

    @Test
    void deleteByTokenValueFlushesAndClearsEntityManager() {
        refreshTokenService.deleteByTokenValue("refresh-token");

        verify(repository).deleteByTokenValue("refresh-token");
        verify(entityManager).flush();
        verify(entityManager).clear();
    }

    private RefreshTokenRecord token(String tokenValue) {
        return RefreshTokenRecord.builder()
                .tokenId(UUID.randomUUID())
                .username("user")
                .tokenValue(tokenValue)
                .expireAt(LocalDateTime.now().plusDays(1))
                .build();
    }
}
