package com.check.user_check.repository;

import com.check.user_check.entity.Attendance;
import com.check.user_check.entity.User;
import com.check.user_check.enumeratedType.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {

    @Query("""
        SELECT ad
        FROM Attendance ad
        JOIN FETCH ad.attendanceTargets at
        WHERE at.user = :user
    """)
    List<Attendance> findAllByUser(User user);

    @Query("""
        SELECT ad
        FROM Attendance ad
        INNER JOIN ad.attendanceTargets at
        INNER JOIN at.user us
        WHERE at.user = :user
            AND us.role IN (:roles)
    """)
    List<Attendance> findAllByUserAndAuth(User user, List<Role> roles);
}
