package com.check.user_check.service.response.basic;

import com.check.user_check.entity.AttendanceSetting;
import com.check.user_check.exception.custom.EntityNotFoundWithCodeException;
import com.check.user_check.repository.AttendanceSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendanceSettingService {

    private final AttendanceSettingRepository attendanceSettingRepository;

    public AttendanceSetting findAttendanceSetting(){
        AttendanceSetting result = attendanceSettingRepository.findAll().get(0);

        if(result == null){
            throw new EntityNotFoundWithCodeException("설정된 출석이 존재하지 않습니다.", "050301");
        }

        return result;
    }

}
