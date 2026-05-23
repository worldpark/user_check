package com.check.user_check.controller.common.ai;

import com.check.user_check.service.rag.AssistantService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class AssistantControllerValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void assistantRejectsBlankMessage() throws Exception {
        AssistantController controller = new AssistantController(mock(AssistantService.class));
        Method method = AssistantController.class.getMethod("assistantBot", String.class);

        Set<?> violations = validator.forExecutables()
                .validateParameters(controller, method, new Object[]{" "});

        assertThat(violations).isNotEmpty();
    }
}
