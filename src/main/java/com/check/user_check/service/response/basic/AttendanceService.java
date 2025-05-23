package com.check.user_check.service.response.basic;

import com.check.user_check.entity.Attendance;
import com.check.user_check.repository.AttendanceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public Attendance findById(UUID attendanceId){
        return attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 출결이 존재하지 않습니다.|020301"));
    }

    public UUID save(Attendance attendance){
        try{
            return attendanceRepository.save(attendance).getAttendanceId();
        }catch (DataIntegrityViolationException dataIntegrityViolationException){
            String message = dataIntegrityViolationException.getMessage();

            throw new DataIntegrityViolationException(message + "|020302");
        }
    }

    public List<UUID> saveAll(List<Attendance> attendances){

        try{
            List<Attendance> attendanceList = attendanceRepository.saveAll(attendances);

            return attendanceList.stream()
                    .map(Attendance::getAttendanceId)
                    .collect(Collectors.toList());

        }catch (DataIntegrityViolationException dataIntegrityViolationException){
            String message = dataIntegrityViolationException.getMessage();

            throw new DataIntegrityViolationException(message + "|020303");
        }
    }

    public List<Attendance> findAllFetch(){
        return attendanceRepository.findAllFetch();
    }

    public void delete(Attendance attendance){
        attendanceRepository.delete(attendance);
    }

    public Attendance findByUserIdAndAttendanceDate(UUID userId, LocalDateTime attendanceDate){

        return attendanceRepository.findByUserIdAndAttendanceDate(userId, attendanceDate)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 출결이 존재하지 않습니다.|020304"));
    }

    public List<Attendance> findUserAttendance(UUID userId, LocalDateTime minTime, LocalDateTime maxTime){
        return attendanceRepository.findUserAttendance(userId, minTime, maxTime);
    }
}
