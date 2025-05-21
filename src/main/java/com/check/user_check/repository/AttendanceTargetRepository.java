package com.check.user_check.repository;

import com.check.user_check.dto.response.admin.AttendanceTargetResponse;
import com.check.user_check.entity.Attendance;
import com.check.user_check.entity.AttendanceTarget;
import com.check.user_check.entity.User;
import com.check.user_check.enumeratedType.AttendanceAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceTargetRepository extends JpaRepository<AttendanceTarget, UUID> {

    void deleteAllByAttendance(Attendance attendances);

//    @Query("""
//        SELECT new com.check.user_check.dto.response.admin.AttendanceTargetResponse(
//            u.uid,
//            u.userId,
//            u.userName,
//            at.auth,
//            at.targetStatus
//        )
//        FROM AttendanceTarget at
//        JOIN FETCH at.user u
//        WHERE at.attendance = :attendance
//    """)
//    List<AttendanceTargetResponse> findTargetUserAllByTargetId(Attendance attendance);

    @Query("""
        SELECT at
        FROM AttendanceTarget at
        WHERE at.auth = com.check.user_check.enumeratedType.AttendanceAuth.OWNER
            AND at.user = :user
            AND at.attendance = :attendance
    """)
    Optional<AttendanceTarget> ownerCheck(User user, Attendance attendance);

    @Query("""
        SELECT at
        FROM AttendanceTarget at
        WHERE at.user = :user
            AND at.attendance = :attendance
    """)
    Optional<AttendanceTarget> userCheck(User user, Attendance attendance);


}
