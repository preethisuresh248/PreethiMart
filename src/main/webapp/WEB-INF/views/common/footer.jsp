<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<footer class="footer">
    <div class="container footer-inner">
        <div>
            <strong>PreethiMart</strong> &copy; 2026. Anna University R2025 Semester 3 Capstone.
            <div style="font-size: 11px; margin-top: 4px; color: var(--text-muted);">
                Architected with Java Servlets, JDBC PreparedStatement, HikariCP, and H2 Database.
            </div>
        </div>
        <div style="display: flex; gap: 16px;">
            <a href="${pageContext.request.contextPath}/api/v1/health" target="_blank" style="color: var(--text-muted);"><i class="fa-solid fa-heart-pulse"></i> System Health</a>
            <a href="${pageContext.request.contextPath}/products" style="color: var(--text-muted);">Catalog</a>
            <a href="${pageContext.request.contextPath}/auth/login" style="color: var(--text-muted);">Portal</a>
        </div>
    </div>
</footer>

<!-- Floating AI Chatbot Widget (O4 Final Review Deliverable) -->
<button class="chat-launcher" id="chatLauncher" title="Ask PreethiBot AI Assistant">
    <i class="fa-solid fa-robot"></i>
</button>

<div class="chat-window" id="chatWindow">
    <div class="chat-header">
        <div>
            <h4><i class="fa-solid fa-robot"></i> PreethiBot AI</h4>
            <p>Smart E-Commerce & Order Assistant</p>
        </div>
        <button class="chat-close" id="chatClose">&times;</button>
    </div>

    <div class="chat-messages" id="chatMessages">
        <div class="chat-bubble bot">
            👋 Hi there! I'm <strong>PreethiBot</strong>, your 24/7 AI shopping assistant. Ask me anything about products, delivery schedules, return policies, or seller onboarding!
        </div>
    </div>

    <div class="chat-chips">
        <span class="chat-chip" data-prompt="What is your shipping policy?">🚚 Shipping?</span>
        <span class="chat-chip" data-prompt="How do 7-day returns work?">🔄 Returns?</span>
        <span class="chat-chip" data-prompt="What are the best products available?">⭐ Best Sellers</span>
        <span class="chat-chip" data-prompt="How do I sell products here?">💼 Become Seller</span>
    </div>

    <div class="chat-input-bar">
        <input type="text" id="chatInput" placeholder="Ask about shipping, returns, products..." maxlength="500">
        <button id="chatSend"><i class="fa-solid fa-paper-plane"></i></button>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/chat-widget.js"></script>
</body>
</html>
