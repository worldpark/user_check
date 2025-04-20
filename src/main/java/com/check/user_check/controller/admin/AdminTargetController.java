package com.check.user_check.controller.admin;

import com.check.user_check.service.response.admin.AdminTargetResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminTargetController {

    private final AdminTargetResponseService adminTargetResponseService;
}
