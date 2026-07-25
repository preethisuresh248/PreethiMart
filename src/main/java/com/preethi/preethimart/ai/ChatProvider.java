package com.preethi.preethimart.ai;

public interface ChatProvider {
    /**
     * Generates a conversational or domain-aware response for the given user message.
     *
     * @param userMessage raw message prompt from the user
     * @param sessionId active session identifier for context
     * @return response text from the AI assistant
     */
    String generateResponse(String userMessage, String sessionId);
}
