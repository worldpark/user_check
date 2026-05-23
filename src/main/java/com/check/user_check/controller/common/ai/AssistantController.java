package com.check.user_check.controller.common.ai;

import com.check.user_check.service.rag.AssistantService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api")
public class AssistantController {

    private final AssistantService assistantService;

    @GetMapping(value = "/assistant", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> assistantBot(
            @RequestParam
            @NotBlank(message = "message 는 비어 있을 수 없습니다.")
            @Size(max = 500, message = "message 는 500자를 초과할 수 없습니다.")
            String message
    ){
        return assistantService.elasticRagBot(message);
    }
}
