# PreethiMart Capstone Presentation Slides
**Anna University Regulations 2025 (R2025) — Semester 3**
**Final Review Evaluation — October 10, 2026**

---

### Slide 1: Title & Overview
- **Project Title:** PreethiMart — Enterprise Multi-Vendor E-Commerce Platform
- **Student / Builder:** Solo Capstone
- **Tech Stack:** Java 17, Java Servlets, JSP/JSTL, JDBC PreparedStatement, HikariCP, H2 DB, Tomcat 9.0
- **Version:** v1.1.0 (Full Build + AI Chatbot)

---

### Slide 2: Problem Statement & Motivation
- Enterprise e-commerce requires strict consistency, data integrity, and low latency.
- Goal: Engineer a complete, robust e-commerce engine from ground up using core Java Enterprise APIs without framework abstraction layers.
- Key Challenges Addressed:
  - Preventing Race Conditions in Stock Deduction (ACID Transactions)
  - Zero Plaintext Password Storage (jBCrypt with salt rounds = 10)
  - Resilient Conversational AI Support (Rate Limiting, In-Memory Caching, Fallback)

---

### Slide 3: Architecture & Layering
- **Presentation Layer:** JSP + JSTL views with automatic XML escaping; responsive CSS design.
- **Controller Layer:** Dedicated Java Servlets implementing Front Controller pattern; AuthFilter and EncodingFilter.
- **Service Layer:** Transaction demarcation, validation, DTO transformation.
- **Data Access Layer:** Interface-based DAOs with 100% `PreparedStatement` queries.
- **Persistence & Pooling:** HikariCP connection pool managing embedded & persistent H2 database.

---

### Slide 4: Key Modules (F1–F8)
- **F1 (Auth):** Role-based access control (`BUYER`, `SELLER`, `ADMIN`).
- **F2 (Listings):** Merchant inventory management and real-time stock updates.
- **F3 (Browse & Search):** Multi-attribute search, category filtering, and pagination.
- **F4 (Cart):** Dynamic cart operations, subtotal, 5% tax, and shipping calculations.
- **F5 (Checkout):** Atomic checkout with mock payment instruments (Card, UPI, NetBanking).
- **F6 (Orders):** Buyer history and Seller order fulfillment workflow.
- **F7 (Admin):** User management, GMV metrics, and listing moderation.
- **F8 (Reviews):** Verified purchase 1–5 star reviews.

---

### Slide 5: Optional Deliverables & AI Chatbot (O1–O4)
- **O1 Wishlist:** Save-for-later functionality with instant move-to-cart migration.
- **O2 Order Workflow:** Formal state machine (`Pending` &rarr; `Confirmed` &rarr; `Shipped` &rarr; `Delivered`).
- **O3 Merchant Dashboard:** Real-time low-stock alerts.
- **O4 PreethiBot Assistant:**
  - Pluggable provider architecture via `ChatProvider` interface (`GeminiChatProvider` + `MockChatProvider`).
  - Strict 10 msg/min per-session rate limiter with in-memory query cache.
  - Floating UI widget.

---

### Slide 6: Security & Quality Engineering
- **SQL Injection Defense:** All SQL parameterized with `PreparedStatement`.
- **Session Security:** `changeSessionId()` upon login to prevent session fixation attacks.
- **XSS Defense:** JSTL `<c:out value="..."/>` sanitization across all JSP output points.
- **Testing Coverage:** 21 automated tests across embedded H2 DAO tests, Mockito service tests, and validation tests (100% passing).
- **CI/CD:** Automated GitHub Actions build pipeline executing `mvn verify`.

---

### Slide 7: Live Demonstration Summary
- **Journey 1:** User Registration & Authentication (Role switching).
- **Journey 2:** Catalog Search, Filter, and Product Details inspection.
- **Journey 3:** Add to Cart, Quantity adjustment, and Mock Payment Checkout.
- **Journey 4:** Order fulfillment workflow progression in Seller Dashboard.
- **Journey 5:** 24/7 AI Chatbot queries (Shipping, Returns, Recommendations).

---

### Slide 8: Conclusion & Future Scope
- **Current Milestone:** Successfully completed all 11 weeks of the capstone schedule up to the October 10 Final Review.
- **Future Enhancements:**
  - Integration with live payment gateways (Razorpay / Stripe).
  - Webhook-driven logistics tracking (Shiprocket API).
  - Semantic vector search for AI product recommendations.
- **Thank You!** Questions & Live Demo.
