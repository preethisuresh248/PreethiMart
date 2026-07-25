package com.preethi.preethimart.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public final class ChatProviderFactory {

    private static final Logger logger = LoggerFactory.getLogger(ChatProviderFactory.class);
    private static volatile ChatProvider instance;

    private ChatProviderFactory() {}

    public static ChatProvider getProvider() {
        if (instance == null) {
            synchronized (ChatProviderFactory.class) {
                if (instance == null) {
                    String providerType = resolveProviderType();
                    if ("gemini".equalsIgnoreCase(providerType)) {
                        logger.info("Initializing GeminiChatProvider for PreethiMart AI Assistant.");
                        instance = new GeminiChatProvider();
                    } else {
                        logger.info("Initializing MockChatProvider (offline mode) for PreethiMart AI Assistant.");
                        instance = new MockChatProvider();
                    }
                }
            }
        }
        return instance;
    }

    private static String resolveProviderType() {
        String env = System.getenv("AI_CHATBOT_PROVIDER");
        if (env != null && !env.trim().isEmpty()) {
            return env.trim();
        }
        try (InputStream in = ChatProviderFactory.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                Properties props = new Properties();
                props.load(in);
                return props.getProperty("ai.chatbot.provider", "mock");
            }
        } catch (Exception ignored) {}
        return "mock";
    }
}
