package com.check.user_check.service.response.basic;

import com.check.user_check.entity.Attendance;
import com.check.user_check.exception.custom.DataIntegrityViolationWithCodeException;
import com.check.user_check.exception.custom.EntityNotFoundWithCodeException;
import com.check.user_check.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
                .orElseThrow(() -> new EntityNotFoundWithCodeException("해당하는 출결이 존재하지 않습니다.", "030301"));
    }

    public Attendance findFetchByAttendanceId(UUID attendanceId){

        return attendanceRepository.findFetchByAttendanceId(attendanceId)
                .orElseThrow(() -> new EntityNotFoundWithCodeException("해당하는 출결이 존재하지 않습니다.", "030301"));
    }

    public UUID save(Attendance attendance){
        try{
            return attendanceRepository.save(attendance).getAttendanceId();
        }catch (DataIntegrityViolationException dataIntegrityViolationException){
            String message = dataIntegrityViolationException.getMessage();

            throw new DataIntegrityViolationWithCodeException(message, "030302");
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

            throw new DataIntegrityViolationWithCodeException(message, "030303");
        }
    }

    public Page<Attendance> findAllByDateTime(LocalDateTime minTime, LocalDateTime maxTime, Pageable pageable){
        return attendanceRepository.findAllByDateTime(minTime, maxTime, pageable);
    }

    public void delete(Attendance attendance){
        attendanceRepository.delete(attendance);
    }

    public Attendance findByUserIdAndAttendanceDate(UUID userId, LocalDateTime attendanceDate){

        return attendanceRepository.findByUserIdAndAttendanceDate(userId, attendanceDate)
                .orElse(null);
    }

    public List<Attendance> findUserAttendance(UUID userId, LocalDateTime minTime, LocalDateTime maxTime){
        return attendanceRepository.findUserAttendance(userId, minTime, maxTime);
    }

    public void updateAttendanceDate(
            LocalDateTime attendanceDate,
            LocalDateTime minAttendanceDate,
            LocalDateTime maxAttendanceDate){

        attendanceRepository.updateAttendanceDate(attendanceDate, minAttendanceDate, maxAttendanceDate);
    }

    public Long findCountNoCheck(LocalDateTime minDate, LocalDateTime maxDate){
        return attendanceRepository.findCountNoCheck(minDate, maxDate);
    }

    public Long findCountIsCheck(LocalDateTime minDate, LocalDateTime maxDate){
        return attendanceRepository.findCountIsCheck(minDate, maxDate);
    }

}
