package com.check.user_check.service.response.basic;

import com.check.user_check.dto.response.admin.AttendanceTargetResponse;
import com.check.user_check.entity.Attendance;
import com.check.user_check.entity.AttendanceTarget;
import com.check.user_check.entity.User;
import com.check.user_check.enumeratedType.AttendanceAuth;
import com.check.user_check.repository.AttendanceRepository;
import com.check.user_check.repository.AttendanceTargetRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttendanceTargetService {

    private final AttendanceTargetRepository attendanceTargetRepository;

    public AttendanceTarget findById(UUID attendanceTargetId){
        return attendanceTargetRepository.findById(attendanceTargetId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 대상자가 존재하지 않습니다.|030301"));
    }

    public UUID save(AttendanceTarget attendanceTarget){
        try{
            return attendanceTargetRepository.save(attendanceTarget).getTargetId();

        }catch (DataIntegrityViolationException dataIntegrityViolationException){
            String message = dataIntegrityViolationException.getMessage();

            throw new DataIntegrityViolationException(message + "|030302");
        }
    }

    public void deleteAllByAttendance(Attendance attendance){
        attendanceTargetRepository.deleteAllByAttendance(attendance);
    }

    public List<AttendanceTargetResponse> findTargetUserAllByTargetId(Attendance attendance){
        return attendanceTargetRepository.findTargetUserAllByTargetId(attendance);
    }

    public void ownerCheck(User user, Attendance attendance){
        attendanceTargetRepository.ownerCheck(user, AttendanceAuth.OWNER, attendance)
                .orElseThrow(() -> new EntityNotFoundException("출결 관리자가 아닙니다.|030303"));
    }

    public void userCheck(User user, Attendance attendance){
        attendanceTargetRepository.userCheck(user, attendance)
                .orElseThrow(() -> new EntityNotFoundException("출결 연관자가 아닙니다.|030304"));
    }
}
