package com.check.user_check.repository;

import com.check.user_check.dto.AttendanceStatisticsDto;
import com.check.user_check.entity.Attendance;
import com.check.user_check.entity.User;
import com.check.user_check.enumeratedType.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {

    @Query("""
        SELECT a, u
        FROM Attendance a
        JOIN FETCH a.user u
        WHERE a.attendanceId = :attendanceId
    """)
    Optional<Attendance> findFetchByAttendanceId(UUID attendanceId);

    @Query("""
        SELECT a
        FROM Attendance a
        WHERE a.user.userId = :userId 
            AND a.attendanceDate = :attendanceDate
    """)
    Optional<Attendance> findByUserIdAndAttendanceDate(UUID userId, LocalDateTime attendanceDate);

    @Query("""
        SELECT a
        FROM Attendance a
        INNER JOIN a.user u
        WHERE a.attendanceDate >= :minTime AND a.attendanceDate < :maxTime
    """)
    Page<Attendance> findAllByDateTime(LocalDateTime minTime, LocalDateTime maxTime, Pageable pageable);

    @Query("""
        SELECT a, u
        FROM Attendance a
        JOIN FETCH a.user u
        WHERE a.user.userId = :userId
            AND a.attendanceDate >= :minTime AND a.attendanceDate <= :maxTime
    """)
    List<Attendance> findUserAttendance(UUID userId, LocalDateTime minTime, LocalDateTime maxTime);

    @Modifying(clearAutomatically = true)
    @Query("""
        UPDATE Attendance a
        SET a.attendanceDate = :attendanceDate
        WHERE a.attendanceDate >= :minAttendanceDate
            AND a.attendanceDate < :maxAttendanceDate
    """)
    void updateAttendanceDate(
            LocalDateTime attendanceDate,
            LocalDateTime minAttendanceDate,
            LocalDateTime maxAttendanceDate);

    @Query("""
        SELECT COUNT(a)
        FROM Attendance a
        WHERE a.attendanceDate >= :minDate AND a.attendanceDate < :maxDate
            AND a.checkTime IS NULL 
    """)
    Long findCountNoCheck(LocalDateTime minDate, LocalDateTime maxDate);

    @Query("""
        SELECT COUNT(a)
        FROM Attendance a
        WHERE a.attendanceDate >= :minDate AND a.attendanceDate < :maxDate
            AND a.checkTime IS NOT NULL 
    """)
    Long findCountIsCheck(LocalDateTime minDate, LocalDateTime maxDate);

}
