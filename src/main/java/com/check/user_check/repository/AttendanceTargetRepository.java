package com.check.user_check.repository;

import com.check.user_check.entity.AttendanceTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceTargetRepository extends JpaRepository<AttendanceTarget, UUID> {

    @Query("""
        SELECT at, u
        FROM AttendanceTarget at
        JOIN FETCH at.user u
        ORDER BY u.name, u.username
    """)
    List<AttendanceTarget> findAllFetch();

}
