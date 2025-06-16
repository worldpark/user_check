package com.check.user_check.service.response.basic;

import com.check.user_check.entity.RefreshTokenRecord;
import com.check.user_check.exception.token.refreshToken.RefreshTokenError;
import com.check.user_check.exception.token.refreshToken.RefreshTokenException;
import com.check.user_check.repository.RefreshTokenRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final EntityManager entityManager;

    private final RefreshTokenRepository repository;

    public void save(RefreshTokenRecord refreshTokenRecord){
        repository.save(refreshTokenRecord);
    }

    public void delete(RefreshTokenRecord refreshTokenRecord){
        repository.delete(refreshTokenRecord);
    }

    public RefreshTokenRecord findByTokenValue(String tokenValue){
        return repository.findByTokenValue(tokenValue)
                .orElseThrow(() -> new RefreshTokenException(RefreshTokenError.BAD_SIGN));
    }

    @Transactional
    public void deleteByTokenValue(String tokenValue){
        repository.deleteByTokenValue(tokenValue);
        entityManager.flush();
        entityManager.clear();
    }
}
