package com.preethi.preethimart.controller.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.preethi.preethimart.dto.ApiResponse;
import com.preethi.preethimart.dto.ChatMessageDTO;
import com.preethi.preethimart.exception.ValidationException;
import com.preethi.preethimart.service.ChatbotService;
import com.preethi.preethimart.util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "ChatApiServlet", urlPatterns = {"/api/chat", "/api/v1/chat"})
public class ChatApiServlet extends HttpServlet {

    private final ChatbotService chatbotService = new ChatbotService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(true);
        String sessionId = session.getId();

        StringBuilder body = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
        }

        String message = null;
        if (body.length() > 0) {
            try {
                JsonNode root = JsonUtil.getMapper().readTree(body.toString());
                if (root.has("message")) {
                    message = root.get("message").asText();
                }
            } catch (Exception ignored) {}
        }

        if (message == null || message.trim().isEmpty()) {
            message = req.getParameter("message");
        }

        try {
            if (message == null || message.trim().isEmpty()) {
                throw new ValidationException("Message text cannot be empty.");
            }

            ChatMessageDTO result = chatbotService.processMessage(message, sessionId);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok(result)));
        } catch (ValidationException ve) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.error(ve.getMessage())));
        } catch (Exception e) {
            // Degraded static fallback
            ChatMessageDTO fallback = new ChatMessageDTO(
                    message,
                    "PreethiBot is currently busy helping other shoppers. Please browse our catalog or email support@preethimart.com!",
                    sessionId
            );
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok(fallback)));
        }
    }
}
