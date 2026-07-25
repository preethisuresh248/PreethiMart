package com.preethi.preethimart.ai;

import com.preethi.preethimart.exception.ValidationException;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatbotRateLimiter {

    private static final int MAX_REQUESTS_PER_MINUTE = 10;
    private static final long ONE_MINUTE_MS = 60_000L;
    public static final int MAX_INPUT_LENGTH = 500;

    // Session -> timestamps of requests
    private final Map<String, Deque<Long>> sessionTimestamps = new ConcurrentHashMap<>();
    // Session -> (Question -> Cached Response)
    private final Map<String, Map<String, String>> sessionCache = new ConcurrentHashMap<>();

    public void checkRateLimitAndLength(String sessionId, String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new ValidationException("Message cannot be empty.");
        }
        if (message.length() > MAX_INPUT_LENGTH) {
            throw new ValidationException("Message exceeds maximum allowed length of " + MAX_INPUT_LENGTH + " characters.");
        }

        String sid = (sessionId != null && !sessionId.isEmpty()) ? sessionId : "anonymous";
        long now = System.currentTimeMillis();

        sessionTimestamps.compute(sid, (key, deque) -> {
            if (deque == null) {
                deque = new ArrayDeque<>();
            }
            // Remove timestamps older than 1 minute
            while (!deque.isEmpty() && now - deque.peekFirst() > ONE_MINUTE_MS) {
                deque.pollFirst();
            }
            if (deque.size() >= MAX_REQUESTS_PER_MINUTE) {
                throw new ValidationException("Rate limit exceeded: Maximum 10 messages per minute allowed. Please wait a moment.");
            }
            deque.addLast(now);
            return deque;
        });
    }

    public String getCachedResponse(String sessionId, String question) {
        if (sessionId == null || question == null) return null;
        Map<String, String> cache = sessionCache.get(sessionId);
        if (cache != null) {
            return cache.get(question.trim().toLowerCase());
        }
        return null;
    }

    public void cacheResponse(String sessionId, String question, String response) {
        if (sessionId == null || question == null || response == null) return;
        sessionCache.computeIfAbsent(sessionId, k -> new ConcurrentHashMap<>())
                .put(question.trim().toLowerCase(), response);
    }
}
