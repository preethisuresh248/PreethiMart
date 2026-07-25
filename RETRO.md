# PreethiMart Sprint Retrospectives (RETRO.md)
**Anna University Regulations 2025 (R2025) — Semester 3 Capstone**
**Window:** July 24 – October 10, 2026

Format per syllabus: *What worked*, *What didn't*, *One change for next sprint*.

---

### Kickoff (Jul 24 – Jul 27): Architecture & Toolchain Setup
- **What worked:** Project skeleton organized with clean package layering (`com.preethi.preethimart`), Maven JDK 17, and H2 database schema v1.
- **What didn't:** Initial clarification needed around Tomcat 9 `javax.servlet` versus Tomcat 10 `jakarta.servlet`.
- **Change for next sprint:** Stick strictly to `javax.servlet:javax.servlet-api:4.0.1` targeting Apache Tomcat 9.0.x container standards.

### Week 1 (Jul 27 – Aug 2): Authentication & Base DAO
- **What worked:** jBCrypt password hashing implemented; AuthFilter and EncodingFilter correctly intercepting requests; embedded H2 connection pool via HikariCP.
- **What didn't:** Needed explicit session fixation defense during login.
- **Change for next sprint:** Added `request.changeSessionId()` upon successful credential validation.

### Week 2 (Aug 3 – Aug 9): Core Shopping Flow (Browse &rarr; Cart &rarr; Order)
- **What worked:** Full shopping lifecycle working smoothly from catalog browsing to cart quantity adjustment and mock payment checkout.
- **What didn't:** JSTL escaping was initially missed on dynamic product search terms.
- **Change for next sprint:** Mandated `<c:out value="..."/>` across all JSP templates to prevent potential XSS vulnerabilities.

### Week 3 (Aug 10 – Aug 16): Seller Dashboard & DTOs
- **What worked:** Seller listing management implemented; DTOs decoupled from entity classes (`UserResponseDTO` never exposes `passwordHash`).
- **What didn't:** JSON API endpoints needed a uniform response wrapper.
- **Change for next sprint:** Standardized on `ApiResponse<T>` envelope `{success, data, error}` across all `/api/v1/` endpoints.

### Week 4 (Aug 17 – Aug 23): Admin Panel & Order Views
- **What worked:** F7 Admin portal completed with live GMV metrics, user deletion, and listing moderation; buyer order history views operational.
- **What didn't:** Order item prices were initially referenced directly from products table.
- **Change for next sprint:** Implemented immutable `price_at_purchase` snapshot in `order_items` table to protect financial auditability.

### Week 5 (Aug 24 – Aug 30): Search Refinement & Order Workflow
- **What worked:** Combined keyword and category search with pagination; implemented O2 order status workflow (`Pending` &rarr; `Confirmed` &rarr; `Shipped` &rarr; `Delivered`).
- **What didn't:** Manual database column edits broke reproducible schema migrations.
- **Change for next sprint:** Scripted all status workflow changes into numbered migration `migrations/V2__order_status_workflow.sql`.

### Week 6 (Aug 31 – Sep 6): Reviews, Ratings & Edge Case Hardening
- **What worked:** Verified purchase review system (1–5 stars) operational; sweep performed for empty cart checkout and out-of-stock purchases.
- **What didn't:** Initial review form allowed any user to review products without purchase verification.
- **Change for next sprint:** Enforced verified purchase check in `ReviewDAO.hasBuyerPurchasedProduct()` before accepting reviews.

### Week 7 (Sep 7 – Sep 13): Security Hardening & Test Coverage
- **What worked:** Verified 100% PreparedStatement compliance across all DAOs; created custom error pages in `web.xml` hiding stack traces; expanded test coverage.
- **What didn't:** Minor warning in Checkstyle on import ordering.
- **Change for next sprint:** Configured `checkstyle.xml` and validated clean compilation in CI pipeline.

### Week 8 (Sep 14 – Sep 20): Deployment & Full Build Review
- **What worked:** Health check endpoint `GET /api/v1/health` returning `{"status":"UP","db":"UP"}`; standalone embedded server launcher created (`run.bat`).
- **What didn't:** In-memory H2 cleared records on restart if file mode was omitted.
- **Change for next sprint:** Documented file-persisted H2 mode in `config.properties.example` for persistent multi-restart environments.

### Week 9 (Sep 21 – Sep 27): AI Chatbot Backend (Phase 3)
- **What worked:** Designed `ChatProvider` interface; implemented `MockChatProvider` and `GeminiChatProvider`; added per-session rate limit (10 msg/min) and cache.
- **What didn't:** External API keys must never be committed to source repositories.
- **Change for next sprint:** Ensured API keys are resolved purely server-side from environment variables or gitignored config files.

### Week 10 (Sep 28 – Oct 4): Chatbot UI & Architectural Diagrams
- **What worked:** Built floating chat widget in JS; verified 5–10 domain FAQ answers; created D1 ER diagram, D2 Use Case diagram, and D3 Sequence diagram in Mermaid.
- **What didn't:** Chat window needed auto-scroll to latest message on receive.
- **Change for next sprint:** Added `messages.scrollTop = messages.scrollHeight` on every bubble render.

### Week 11 (Oct 5 – Oct 10): Final Regression, Slide Deck & Academic Report
- **What worked:** 21 automated tests passing with 0 failures; finalized project report, slide deck, demo script, and git commit history intact across the full window.
- **What didn't:** JDK 27 dynamic agent loading required bytecode adjustment for concrete class mocking.
- **Change for next sprint:** Refactored `CartService` into an interface for pure interface-based test doubles, ensuring 100% compatibility across all current and future Java LTS releases.
