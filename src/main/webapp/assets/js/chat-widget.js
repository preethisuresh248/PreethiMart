// PreethiMart AI Assistant Floating Widget
document.addEventListener("DOMContentLoaded", function () {
    const launcher = document.getElementById("chatLauncher");
    const windowEl = document.getElementById("chatWindow");
    const closeBtn = document.getElementById("chatClose");
    const input = document.getElementById("chatInput");
    const sendBtn = document.getElementById("chatSend");
    const messages = document.getElementById("chatMessages");
    const chips = document.querySelectorAll(".chat-chip");

    if (!launcher || !windowEl) return;

    launcher.addEventListener("click", () => {
        windowEl.classList.toggle("open");
        if (windowEl.classList.contains("open")) {
            input.focus();
        }
    });

    closeBtn.addEventListener("click", () => {
        windowEl.classList.remove("open");
    });

    chips.forEach(chip => {
        chip.addEventListener("click", () => {
            const query = chip.getAttribute("data-prompt") || chip.innerText.trim();
            input.value = query;
            sendMessage();
        });
    });

    sendBtn.addEventListener("click", sendMessage);
    input.addEventListener("keydown", (e) => {
        if (e.key === "Enter") {
            e.preventDefault();
            sendMessage();
        }
    });

    function appendMessage(text, sender) {
        const bubble = document.createElement("div");
        bubble.className = "chat-bubble " + sender;
        
        // Basic markdown formatting for **bold**, line breaks
        let formatted = text
            .replace(/\*\*(.*?)\*\*/g, "<strong>$1</strong>")
            .replace(/\*(.*?)\*/g, "<em>$1</em>")
            .replace(/`(.*?)`/g, "<code>$1</code>")
            .replace(/\n/g, "<br>");

        bubble.innerHTML = formatted;
        messages.appendChild(bubble);
        messages.scrollTop = messages.scrollHeight;
    }

    async function sendMessage() {
        const text = input.value.trim();
        if (!text) return;

        appendMessage(text, "user");
        input.value = "";

        // Typing indicator
        const typingIndicator = document.createElement("div");
        typingIndicator.className = "chat-bubble bot";
        typingIndicator.id = "typingIndicator";
        typingIndicator.innerText = "PreethiBot is thinking...";
        messages.appendChild(typingIndicator);
        messages.scrollTop = messages.scrollHeight;

        try {
            const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 1));
            const apiUrl = (contextPath ? contextPath : "") + "/api/chat";

            const res = await fetch(apiUrl, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ message: text })
            });

            const data = await res.json();
            const ind = document.getElementById("typingIndicator");
            if (ind) ind.remove();

            if (data.success && data.data && data.data.reply) {
                appendMessage(data.data.reply, "bot");
            } else if (data.error) {
                appendMessage("⚠️ " + data.error, "bot");
            } else {
                appendMessage("Hello! PreethiMart offers fast delivery, easy returns, and authentic products.", "bot");
            }
        } catch (err) {
            const ind = document.getElementById("typingIndicator");
            if (ind) ind.remove();
            appendMessage("🚚 PreethiMart delivers in 2–4 days with free shipping on orders above ₹500! Contact support@preethimart.com for immediate help.", "bot");
        }
    }
});
