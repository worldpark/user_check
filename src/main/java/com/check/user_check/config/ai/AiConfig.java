package com.check.user_check.config.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder){
        OpenAiChatOptions chatOptions = new OpenAiChatOptions();
        chatOptions.setModel("gpt-4o-mini");

        return chatClientBuilder
                .defaultOptions(chatOptions)
                .build();
    }
}
