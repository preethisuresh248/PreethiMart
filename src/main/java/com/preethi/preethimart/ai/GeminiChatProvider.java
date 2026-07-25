package com.preethi.preethimart.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class GeminiChatProvider implements ChatProvider {

    private static final Logger logger = LoggerFactory.getLogger(GeminiChatProvider.class);
    private static final String GEMINI_ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

    private final String apiKey;
    private final MockChatProvider fallbackProvider = new MockChatProvider();
    private final HttpClient httpClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public GeminiChatProvider() {
        this.apiKey = resolveApiKey();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    private String resolveApiKey() {
        String key = System.getenv("GEMINI_API_KEY");
        if (key != null && !key.trim().isEmpty()) {
            return key.trim();
        }
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                Properties p = new Properties();
                p.load(in);
                String val = p.getProperty("ai.chatbot.gemini.key");
                if (val != null && !val.trim().isEmpty() && !val.startsWith("YOUR_")) {
                    return val.trim();
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    @Override
    public String generateResponse(String userMessage, String sessionId) {
        if (apiKey == null || apiKey.isEmpty()) {
            logger.info("No Gemini API key supplied; falling back to MockChatProvider.");
            return fallbackProvider.generateResponse(userMessage, sessionId);
        }

        try {
            // Strict server-side prompt template restricting scope to PreethiMart
            String systemInstruction = "You are PreethiBot, the AI assistant for PreethiMart e-commerce platform. " +
                    "PreethiMart is an Indian e-commerce marketplace featuring Electronics, Fashion, Groceries, and Home essentials. " +
                    "Answer concisely, helpfully, and strictly keep your answers related to PreethiMart products, shopping, orders, returns, and seller policies. " +
                    "If asked off-topic questions, politely steer back to shopping.";

            Map<String, Object> textPart = new HashMap<>();
            textPart.put("text", systemInstruction + "\n\nUser Question: " + userMessage);

            Map<String, Object> contentMap = new HashMap<>();
            contentMap.put("parts", List.of(textPart));

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", List.of(contentMap));

            String jsonPayload = mapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GEMINI_ENDPOINT + apiKey))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(6))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode root = mapper.readTree(response.body());
                JsonNode candidates = root.path("candidates");
                if (candidates.isArray() && !candidates.isEmpty()) {
                    JsonNode textNode = candidates.get(0).path("content").path("parts").get(0).path("text");
                    if (!textNode.isMissingNode()) {
                        return textNode.asText();
                    }
                }
            } else {
                logger.warn("Gemini API call failed with status code {}: {}", response.statusCode(), response.body());
            }
        } catch (Exception e) {
            logger.warn("Exception during Gemini API invocation: {}. Returning graceful degraded response.", e.getMessage());
        }

        // On failure return static degraded response or mock response instead of error page
        return fallbackProvider.generateResponse(userMessage, sessionId);
    }
}
