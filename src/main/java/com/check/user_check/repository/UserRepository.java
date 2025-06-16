package com.check.user_check.repository;

import com.check.user_check.aop.logtrace.annotation.NoLog;
import com.check.user_check.entity.AttendanceTarget;
import com.check.user_check.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @NoLog
    Optional<User> findByUsername(String username);

    @Query("""
        SELECT u
        FROM User u
        LEFT OUTER JOIN AttendanceTarget at
                    ON at.user = u
        WHERE at.user.userId IS NULL 
            AND u.role = 'ROLE_USER'
    """)
    List<User> findByAttendanceTargets(AttendanceTarget attendanceTarget);

}
