# PreethiMart: Enterprise E-Commerce Platform
## Final Capstone Project Report
**Course:** Anna University Regulations 2025 (R2025), Semester 3  
**Project Title:** PreethiMart (YourNameMart Capstone Specification)  
**Builder:** Solo  
**Evaluation Window:** July 24 – October 10, 2026  
**Final Release Version:** v1.1.0  

---

## 1. Executive Summary & Problem Statement
The modern digital economy necessitates scalable, secure, and resilient e-commerce platforms capable of handling multi-party transactions across consumers, merchants, and platform overseers. **PreethiMart** is an enterprise-grade multi-vendor e-commerce platform built strictly adhering to the Anna University R2025 Semester 3 Capstone specification using core Java enterprise technologies (**Java Servlets**, **JSP & JSTL**, **JDBC with HikariCP**, and **Apache Tomcat 9.0.x**).

The system eliminates third-party monolithic framework magic (such as Spring Boot) to demonstrate a deep, foundational mastery of HTTP request lifecycle handling, servlet filters, connection pooling, transactional ACID guarantees, relational database normalization, and AI-assisted conversational customer support.

---

## 2. System Architecture & High-Level Design
PreethiMart is engineered following a clean **N-Tier Layered Architecture** with strict boundaries:

```
[ Client Browser / HTTP Consumer / Floating AI Chat Widget ]
                           │
                           ▼
[ Security & Encoding Filters: EncodingFilter -> AuthFilter ]
                           │
                           ▼
[ Controller Layer (Java Servlets): Auth, Product, Cart, Checkout, Order, Seller, Admin, ChatApi, HealthApi ]
                           │
                           ▼
[ Service Layer: UserService, ProductService, CartService, OrderService, ReviewService, ChatbotService ]
                           │
                           ▼
[ Data Access Layer (DAO): UserDAO, ProductDAO, CartDAO, OrderDAO, ReviewDAO, WishlistDAO ]
                           │
                           ▼
[ HikariCP Connection Pool (DBConnectionManager Singleton) ]
                           │
                           ▼
[ H2 Relational Database Engine (In-Memory / File Persistent Server Mode) ]
```

### Architectural Principles:
1. **Separation of Concerns:** Controllers handle HTTP requests and JSON envelopes; Services enforce business validation, authorization, and transactional logic; DAOs interact solely with the database using parameterized JDBC `PreparedStatement`.
2. **Zero Plaintext Credentials:** Password hashing is powered by `jBCrypt` (10 log rounds). Raw passwords never touch database disks or log streams.
3. **DTO Decoupling:** Entity models are never leaked to clients. `UserResponseDTO` strictly omits `passwordHash`.
4. **Resilience & Graceful Degradation:** The AI chatbot incorporates per-session rate limits (10 msgs/min), in-memory caching, and seamless fallback to `MockChatProvider` during external network latency or missing API keys.

---

## 3. Database Schema & Data Integrity (D1 ER Summary)
The database schema (`schema.sql` and `migrations/V2__order_status_workflow.sql`) adheres to Third Normal Form (3NF) standards:
- **`users`:** Stores system participants with roles `BUYER`, `SELLER`, and `ADMIN`. Includes unique indexed lowercased email and BCrypt hash.
- **`products`:** Multi-vendor catalog items linked to merchant FK (`seller_id`), `DECIMAL(10,2)` price precision, inventory counter, and categorization.
- **`cart_items`:** Active shopping baskets with unique constraint `(user_id, product_id)`.
- **`orders`:** Transaction master records maintaining buyer FK, financial totals, delivery addresses, mock payment instruments, and workflow status (`PENDING`, `CONFIRMED`, `SHIPPED`, `DELIVERED`, `CANCELLED`).
- **`order_items`:** Order item ledger capturing point-in-time price snapshot (`price_at_purchase`) preventing retroactive price alterations.
- **`reviews`:** Verified customer ratings (1–5 stars) restricted to confirmed purchasers.
- **`wishlist_items`:** Saved items supporting customer save-for-later actions.
- **Indexes:** Foreign keys and frequent filter predicates (`seller_id`, `category`, `user_id`, `buyer_id`, `status`) are indexed for $O(\log N)$ query performance.

---

## 4. Design Patterns Applied
1. **Data Access Object (DAO) Pattern:** Encapsulates raw SQL queries behind Java interfaces (`UserDAO`, `ProductDAO`, `CartDAO`, etc.), isolating database engine specifics from business services.
2. **Front Controller / MVC Servlet Pattern:** Dedicated HTTP Servlets route operations, hydrate request attributes, and dispatch clean JSTL-escaped JSP views.
3. **Singleton Pattern:** Managed connection pooling via `DBConnectionManager` and provider resolution via `ChatProviderFactory`.
4. **Factory & Strategy Patterns:** The `ChatProvider` interface supports interchangeable AI strategies (`GeminiChatProvider` and `MockChatProvider`), resolved dynamically at runtime by `ChatProviderFactory` based on configuration flags.
5. **Data Transfer Object (DTO) Pattern:** Decouples internal database entities from external representation (`ApiResponse<T>`, `UserResponseDTO`, `ProductDTO`, `CartDTO`, `OrderDTO`).

---

## 5. Summary of Functional Modules (F1–F8 & O1–O4)
| Module ID | Requirement Description | Implementation Status |
|:---|:---|:---:|
| **F1** | Buyer & Seller registration, secure login, seeded Admin account | ✅ Complete |
| **F2** | Seller product catalog CRUD (name, price, stock, images, category) | ✅ Complete |
| **F3** | Buyer browse, category filtering, keyword search, pagination | ✅ Complete |
| **F4** | Shopping cart item operations (add, quantity increment/decrement, remove, total) | ✅ Complete |
| **F5** | Checkout with mock payment confirmation (Card, UPI, NetBanking) & atomic order creation | ✅ Complete |
| **F6** | Buyer past order history and Seller incoming orders fulfillment view | ✅ Complete |
| **F7** | Admin user audit, order supervision, and listing moderation/suspension | ✅ Complete |
| **F8** | Verified purchase customer reviews and star ratings | ✅ Complete |
| **O1** | Wishlist / Save-for-later functionality | ✅ Complete |
| **O2** | Order status workflow progression (`Pending` &rarr; `Confirmed` &rarr; `Shipped` &rarr; `Delivered`) | ✅ Complete |
| **O3** | Seller inventory dashboard and real-time stock alert indicators | ✅ Complete |
| **O4** | AI Assistant Chatbot backend with rate limiting, cache, and UI widget | ✅ Complete |

---

## 6. Security Hardening & Threat Defense (Section 9 Audit)
- **SQL Injection Defense:** Zero string concatenation in SQL. 100% of database interactions run through parameterized `PreparedStatement`.
- **Session Fixation Defense:** Every successful authentication triggers `request.changeSessionId()` to prevent session hijacking.
- **Cross-Site Scripting (XSS) Prevention:** All user-supplied inputs (names, descriptions, reviews, categories) are escaped with `<c:out value="..."/>` in JSTL.
- **Stack Trace Suppression:** Custom error dispatchers mapped in `web.xml` route HTTP 404 and 500 errors to user-friendly pages without revealing JVM stack traces.
- **Credential Hygiene:** Passwords hashed with jBCrypt; production secrets excluded via `.gitignore`.

---

## 7. Automated Test Suite & Quality Verification
- **Unit & DAO Integration Tests:** Embedded H2 in-memory test database validates all CRUD queries, unique constraints, and schema triggers.
- **Service Layer Mockito Tests:** Core business rules (password length, short passwords, duplicate emails, negative pricing, unauthorized merchant edits, and empty cart checkout) are verified with Mockito test doubles.
- **AI Chatbot Rate Limiter Tests:** Verifies strict enforcement of 10 messages/minute per session and 500-character caps.
- **Continuous Integration:** `.github/workflows/build.yml` validates clean compilation and unit test success on every git push.

---

## 8. Deployment & Execution Instructions
The application is packaged as both a standalone runnable server and a deployable WAR:
- **Local Embedded One-Click Run:**
  ```powershell
  .\run.bat
  # Or via Maven:
  mvn compile exec:java
  ```
- **Access URL:** `http://localhost:8080/`
- **Health Check Endpoint:** `http://localhost:8080/api/v1/health` &rarr; `{"status":"UP","db":"UP"}`

---

## 9. Conclusion
PreethiMart fulfills all academic and functional requirements set forth in the Anna University R2025 Semester 3 Capstone syllabus across the entire 11-week development window (July 24 – October 10, 2026). The platform stands as an exemplary implementation of resilient, production-ready Java Enterprise architecture.
