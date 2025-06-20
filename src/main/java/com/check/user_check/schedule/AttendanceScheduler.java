package com.check.user_check.schedule;

import com.check.user_check.entity.AttendanceSetting;
import com.check.user_check.entity.AttendanceTarget;
import com.check.user_check.enumeratedType.AttendanceStatus;
import com.check.user_check.service.response.basic.AttendanceSettingService;
import com.check.user_check.service.response.basic.AttendanceTargetService;
import com.check.user_check.util.LocalDateTimeCreator;
import com.check.user_check.util.UUIDv6Generator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class AttendanceScheduler {

    private final JdbcTemplate jdbcTemplate;
    private final AttendanceSettingService attendanceSettingService;
    private final AttendanceTargetService attendanceTargetService;

    @Scheduled(cron = "0 0 8 * * *")
    @Transactional
    public void createAttendance(){
        AttendanceSetting attendanceSetting = attendanceSettingService.findAttendanceSetting();
        LocalDateTime settingDateTime =
                LocalDateTimeCreator.getNowLocalDateTimeToLocalTime(attendanceSetting.getAttendanceTime());

        List<AttendanceTarget> targets = attendanceTargetService.findAllFetch();

        //jdbc로 batchInsert
        jdbcTemplate.batchUpdate(
                """
                    INSERT INTO attendance (attendance_id, attendance_date, status, user_id)
                    VALUES (?, ?, ?, ?)
                """,
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        AttendanceTarget attendanceTarget = targets.get(i);

                        ps.setString(1, UUIDv6Generator.generate().toString());
                        ps.setTimestamp(2, Timestamp.valueOf(settingDateTime));
                        ps.setString(3, AttendanceStatus.ABSENT.toString());
                        ps.setString(4, attendanceTarget.getUser().getUserId().toString());
                    }

                    @Override
                    public int getBatchSize() {
                        return targets.size();
                    }
                }
        );


    }
}
