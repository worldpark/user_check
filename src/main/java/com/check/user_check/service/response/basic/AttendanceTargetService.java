package com.check.user_check.service.response.basic;

import com.check.user_check.entity.AttendanceTarget;
import com.check.user_check.exception.custom.DataIntegrityViolationWithCodeException;
import com.check.user_check.exception.custom.EntityNotFoundWithCodeException;
import com.check.user_check.repository.AttendanceTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceTargetService {

    private final AttendanceTargetRepository attendanceTargetRepository;

    public AttendanceTarget findById(UUID attendanceTargetId){
        return attendanceTargetRepository.findById(attendanceTargetId)
                .orElseThrow(() -> new EntityNotFoundWithCodeException("해당하는 대상자가 존재하지 않습니다.", "040301"));
    }

    public List<UUID> saveAll(List<AttendanceTarget> attendanceTargets){
        try{
            List<AttendanceTarget> targetList = attendanceTargetRepository.saveAll(attendanceTargets);

            return targetList.stream()
                    .map(AttendanceTarget::getTargetId)
                    .collect(Collectors.toList());

        }catch (DataIntegrityViolationException dataIntegrityViolationException){
            String message = dataIntegrityViolationException.getMessage();

            throw new DataIntegrityViolationWithCodeException(message, "040302");
        }
    }

    public List<AttendanceTarget> findAllFetch(){
        return attendanceTargetRepository.findAllFetch();
    }

    public void delete(AttendanceTarget attendanceTarget){
        attendanceTargetRepository.delete(attendanceTarget);
    }
}
