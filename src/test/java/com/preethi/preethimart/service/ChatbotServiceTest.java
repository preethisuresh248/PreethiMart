package com.preethi.preethimart.service;

import com.preethi.preethimart.ai.ChatbotRateLimiter;
import com.preethi.preethimart.ai.MockChatProvider;
import com.preethi.preethimart.dto.ChatMessageDTO;
import com.preethi.preethimart.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChatbotServiceTest {

    private ChatbotService chatbotService;

    @BeforeEach
    public void setUp() {
        chatbotService = new ChatbotService(new MockChatProvider(), new ChatbotRateLimiter());
    }

    @Test
    @DisplayName("Chatbot - Resolves Shipping FAQ Query")
    public void testShippingQuery() {
        ChatMessageDTO reply = chatbotService.processMessage("What is your shipping policy?", "session_123");
        assertNotNull(reply);
        assertTrue(reply.getReply().toLowerCase().contains("shipping"));
    }

    @Test
    @DisplayName("Chatbot - Enforces 500 Character Input Length Cap")
    public void testInputLengthCap() {
        String longMessage = "a".repeat(501);
        ValidationException ex = assertThrows(ValidationException.class,
                () -> chatbotService.processMessage(longMessage, "session_test"));
        assertTrue(ex.getMessage().contains("exceeds maximum allowed length"));
    }

    @Test
    @DisplayName("Chatbot - Enforces Per-Session Rate Limit (10 Messages per Minute)")
    public void testRateLimiting() {
        String sessionId = "rate_limit_session_" + System.currentTimeMillis();

        // Send 10 messages successfully
        for (int i = 0; i < 10; i++) {
            chatbotService.processMessage("Query " + i, sessionId);
        }

        // 11th message within the same minute must fail
        ValidationException ex = assertThrows(ValidationException.class,
                () -> chatbotService.processMessage("Query 11", sessionId));
        assertTrue(ex.getMessage().contains("Rate limit exceeded"));
    }
}
