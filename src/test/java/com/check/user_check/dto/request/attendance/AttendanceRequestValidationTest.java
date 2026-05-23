package com.check.user_check.dto.request.attendance;

import com.check.user_check.dto.request.attendance.setting.PositionSettingRequest;
import com.check.user_check.dto.request.attendance.target.AttendanceTargetRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AttendanceRequestValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void attendanceUpdateRequestRequiresStatus() {
        AttendanceUpdateRequest request = new AttendanceUpdateRequest(LocalDateTime.now(), null);

        Set<ConstraintViolation<AttendanceUpdateRequest>> violations = validator.validate(request);

        assertThat(violations).extracting(violation -> violation.getPropertyPath().toString())
                .contains("status");
    }

    @Test
    void positionSettingRequestRejectsOutOfRangeCoordinates() {
        PositionSettingRequest request = new PositionSettingRequest(100.0, 200.0);

        Set<ConstraintViolation<PositionSettingRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(2);
        assertThat(violations).extracting(violation -> violation.getPropertyPath().toString())
                .contains("latitude", "longitude");
    }

    @Test
    void attendanceTargetRequestRejectsEmptyTargetList() {
        AttendanceTargetRequest request = new AttendanceTargetRequest(List.of());

        Set<ConstraintViolation<AttendanceTargetRequest>> violations = validator.validate(request);

        assertThat(violations).extracting(violation -> violation.getPropertyPath().toString())
                .contains("targetRequests");
    }
}
