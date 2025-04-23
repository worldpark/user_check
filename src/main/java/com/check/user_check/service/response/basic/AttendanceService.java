package com.check.user_check.service.response.basic;

import com.check.user_check.entity.Attendance;
import com.check.user_check.entity.User;
import com.check.user_check.enumeratedType.Role;
import com.check.user_check.repository.AttendanceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public List<Attendance> findAllByUser(User user){
        return attendanceRepository.findAllByUser(user);
    }

    public Attendance findById(UUID attendanceId){
        return attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 출결이 존재하지 않습니다.|020301"));
    }

    public Attendance save(Attendance attendance){
        try{
            return attendanceRepository.save(attendance);
        }catch (DataIntegrityViolationException dataIntegrityViolationException){
            String message = dataIntegrityViolationException.getMessage();

            throw new DataIntegrityViolationException(message + "|020302");
        }
    }

    public void delete(Attendance attendance){
        attendanceRepository.delete(attendance);
    }
}
