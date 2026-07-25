package com.preethi.preethimart.service;

import com.preethi.preethimart.ai.ChatProvider;
import com.preethi.preethimart.ai.ChatProviderFactory;
import com.preethi.preethimart.ai.ChatbotRateLimiter;
import com.preethi.preethimart.dto.ChatMessageDTO;

public class ChatbotService {

    private final ChatProvider chatProvider;
    private final ChatbotRateLimiter rateLimiter;

    public ChatbotService() {
        this(ChatProviderFactory.getProvider(), new ChatbotRateLimiter());
    }

    public ChatbotService(ChatProvider chatProvider, ChatbotRateLimiter rateLimiter) {
        this.chatProvider = chatProvider;
        this.rateLimiter = rateLimiter;
    }

    public ChatMessageDTO processMessage(String rawMessage, String sessionId) {
        // Enforce input length and 10 messages/minute rate limit
        rateLimiter.checkRateLimitAndLength(sessionId, rawMessage);

        String trimmed = rawMessage.trim();

        // Check in-memory cache for repeated questions in this session
        String cached = rateLimiter.getCachedResponse(sessionId, trimmed);
        if (cached != null) {
            return new ChatMessageDTO(trimmed, cached, sessionId);
        }

        // Generate response via ChatProvider (Mock or Gemini)
        String reply;
        try {
            reply = chatProvider.generateResponse(trimmed, sessionId);
        } catch (Exception e) {
            reply = "I apologize, but I am having trouble connecting right now. Please feel free to browse our product catalog or contact support@preethimart.com!";
        }

        // Cache the response
        rateLimiter.cacheResponse(sessionId, trimmed, reply);

        return new ChatMessageDTO(trimmed, reply, sessionId);
    }
}
