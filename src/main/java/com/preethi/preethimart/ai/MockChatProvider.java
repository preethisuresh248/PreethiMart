package com.preethi.preethimart.ai;

import java.util.Locale;

public class MockChatProvider implements ChatProvider {

    @Override
    public String generateResponse(String userMessage, String sessionId) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return "Hello! How can I help you today at PreethiMart?";
        }

        String msg = userMessage.toLowerCase(Locale.ROOT).trim();

        if (msg.contains("shipping") || msg.contains("delivery") || msg.contains("deliver") || msg.contains("courier")) {
            return "🚚 **PreethiMart Shipping Policy**:\nWe deliver standard orders within 2–4 business days across India. Orders above ₹500 qualify for **Free Shipping**! Standard flat rate is ₹49 for smaller carts.";
        }

        if (msg.contains("return") || msg.contains("refund") || msg.contains("exchange")) {
            return "🔄 **Returns & Refunds**:\nPreethiMart offers a no-questions-asked 7-day return window on eligible electronics, fashion, and home goods. Once approved, refunds are credited back to your original payment method in 3–5 working days.";
        }

        if (msg.contains("track") || msg.contains("where is my order") || msg.contains("order status")) {
            return "📦 **Order Tracking**:\nYou can track your shipment real-time in your **Orders** tab! Orders progress through: `Pending` ➔ `Confirmed` ➔ `Shipped` ➔ `Delivered`.";
        }

        if (msg.contains("payment") || msg.contains("pay") || msg.contains("upi") || msg.contains("card")) {
            return "💳 **Accepted Payments**:\nPreethiMart supports Mock Credit/Debit Cards (Visa, MasterCard, RuPay), UPI, and NetBanking. No real money is charged during our academic capstone demo!";
        }

        if (msg.contains("sell") || msg.contains("seller") || msg.contains("listing") || msg.contains("vendor")) {
            return "💼 **Selling on PreethiMart**:\nRegister with the **Seller** role! You can create listings with high-res photos, set price and inventory, and manage incoming orders from your dedicated Seller Dashboard.";
        }

        if (msg.contains("recommend") || msg.contains("best product") || msg.contains("popular") || msg.contains("headphone") || msg.contains("watch")) {
            return "⭐ **Top PreethiMart Picks**:\n1. **Noise-Cancelling Wireless Headphones** (₹4,499) — 4.9★ rating with 40h battery\n2. **Smart Fitness Watch AMOLED** (₹2,999) — Built-in GPS & SpO2\n3. **Pure Cotton Formal Shirts** (₹1,299) — Breathable daily wear.";
        }

        if (msg.contains("contact") || msg.contains("support") || msg.contains("help") || msg.contains("email")) {
            return "📞 **PreethiMart Support**:\nReach our customer care team at `support@preethimart.com` or Anna University Capstone Lab, Dept of CSE. We are here to help!";
        }

        if (msg.contains("grocery") || msg.contains("tea") || msg.contains("oil") || msg.contains("saffron")) {
            return "🌿 **Organic Groceries**:\nWe stock farm-fresh Organic Nilgiri Green Tea, Grade-1 Kashmiri Saffron, and 100% Cold-Pressed Virgin Coconut Oil in our Groceries section!";
        }

        if (msg.contains("hi") || msg.contains("hello") || msg.contains("hey")) {
            return "👋 Hello! I am **PreethiBot**, your AI shopping assistant. You can ask me about our products, shipping speeds, returns, seller onboarding, or order status!";
        }

        return "🤖 **PreethiBot Assistant**: Thank you for asking! I can assist you with product catalog recommendations, shipping details, return policies, order tracking, and seller listings. Try asking: *'How does shipping work?'* or *'What are your best electronics?'*";
    }
}
