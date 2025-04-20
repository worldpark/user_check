package com.check.user_check.repository;

import com.check.user_check.aop.logtrace.annotation.NoLog;
import com.check.user_check.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @NoLog
    Optional<User> findByUserId(String userId);

}
