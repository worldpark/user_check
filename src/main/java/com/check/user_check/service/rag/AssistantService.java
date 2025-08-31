package com.check.user_check.service.rag;

import com.check.user_check.dto.rag.SourceItem;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssistantService {

    private final ChatClient chatClient;
    private final EmbeddingModel embeddingModel;
    private final RrfSearchService rrfSearchService;

    public Flux<String> elasticRagBot(String message){

        List<RrfSearchService.SearchRunner> runners = new ArrayList<>();
        runners.add(rrfSearchService.bm25Runner("rag_chunks", "content", message, 50));
        runners.add(rrfSearchService.queryStringRunner(
                "rag_chunks", "content:(" + message + ")", 50));

        float[] output = embeddingModel.embedForResponse(List.of(message))
                .getResults()
                .get(0)
                .getOutput();

        Float[] floats = new Float[output.length];

        List<Float> outputWrap = Arrays.asList(floats);

        runners.add(rrfSearchService.knnRunner(
                "rag_chunks", "embedding", outputWrap, 60, 100));

        for (int i = 0; i < output.length; i++) {
            floats[i] = output[i];
        }

        List<RrfSearchService.RrfHit> fused = new ArrayList<>();

        try {
            fused = rrfSearchService.rrfFuse(runners);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<RrfSearchService.RrfHit> top = fused.stream()
                .limit(8)
                .toList();

        StringBuilder ctx = new StringBuilder();
        List<SourceItem> sourceItemList = new ArrayList<>();

        int sid = 1;

        for (RrfSearchService.RrfHit rrfHit : top) {
            String sidStr = "S" + sid;
            sourceItemList.add(new SourceItem(sidStr, rrfHit.getId(), "rag_chunks"));

            String text = extractText(rrfHit.getSource());

            ctx.append("[").append(sidStr).append("] (index=rag_chunks")
                    .append(", id=").append(rrfHit.getId()).append(")\n")
                    .append(text).append("\n\n");

            sid++;
        }

        String prompt = """
                # QUESTION
                {question}

                # CONTEXT
                {context}

                # TASK
                - Use the CONTEXT to answer the QUESTION.
                """;

        String SystemPrompt = """
                    You are an expert assistant. Use ONLY the supplied CONTEXT to answer. 
                    If the answer isn't in the context, say you don't know.
                    Be concise and accurate.
                """;

        String finalPrompt = prompt
                .replace("{question}", message)
                .replace("{context}", ctx.toString());

        Flux<String> response = chatClient.prompt(new Prompt(
                        List.of(
                                new SystemMessage(SystemPrompt),
                                new UserMessage(finalPrompt)
                        )))
                .stream()
                .content()
                .map(str -> str.replace(" ", "\u00A0"));


        return Flux.concat(
                response,
                Flux.just("__END__")
        );

    }

    @SuppressWarnings("unchecked")
    private String extractText(Map<String, Object> src) {
        for (String key : List.of("content", "body", "text", "full_text", "description")) {
            Object v = src.get(key);
            if (v instanceof String str && !str.isBlank()) return str;
        }

        return src.entrySet().stream()
                .map(e -> e.getKey() + ": " + String.valueOf(e.getValue()))
                .collect(Collectors.joining("\n"));
    }
}
