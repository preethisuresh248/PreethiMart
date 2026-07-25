package com.preethi.preethimart.dto;

public class ChatMessageDTO {
    private String message;
    private String reply;
    private String sessionId;
    private long timestamp = System.currentTimeMillis();

    public ChatMessageDTO() {}

    public ChatMessageDTO(String message, String reply, String sessionId) {
        this.message = message;
        this.reply = reply;
        this.sessionId = sessionId;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
