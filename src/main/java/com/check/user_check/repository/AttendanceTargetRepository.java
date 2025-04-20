package com.check.user_check.repository;

import com.check.user_check.entity.AttendanceTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AttendanceTargetRepository extends JpaRepository<AttendanceTarget, UUID> {
}
