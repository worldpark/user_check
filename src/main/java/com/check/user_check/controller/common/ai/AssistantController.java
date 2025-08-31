package com.check.user_check.controller.common.ai;

import com.check.user_check.service.rag.AssistantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AssistantController {

    private final AssistantService assistantService;

    @GetMapping(value = "/assistant", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> assistantBot(
            @RequestParam String message
    ){
        return assistantService.elasticRagBot(message);
    }
}
