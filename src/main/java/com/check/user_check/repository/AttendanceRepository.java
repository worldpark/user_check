package com.check.user_check.repository;

import com.check.user_check.entity.Attendance;
import com.check.user_check.entity.User;
import com.check.user_check.enumeratedType.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {

    @Query("""
        SELECT ad
        FROM Attendance ad
        WHERE ad.user.userId = :userId 
            AND ad.attendanceDate = :attendanceDate
    """)
    Optional<Attendance> findByUserIdAndAttendanceDate(UUID userId, LocalDateTime attendanceDate);

    @Query("""
        SELECT ad
        FROM Attendance ad
        JOIN FETCH ad.user u
    """)
    List<Attendance> findAllFetch();

    @Query("""
        SELECT ad
        FROM Attendance ad
        JOIN FETCH ad.user u
        WHERE ad.user.userId = :userId
            AND ad.attendanceDate >= :minTime AND ad.attendanceDate <= :maxTime
    """)
    List<Attendance> findUserAttendance(UUID userId, LocalDateTime minTime, LocalDateTime maxTime);

}
