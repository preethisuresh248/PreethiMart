# PreethiMart — Enterprise Multi-Vendor E-Commerce Platform
[![Java 17](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Tomcat 9.0](https://img.shields.io/badge/Tomcat-9.0.x-yellow.svg)](https://tomcat.apache.org/)
[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)]()
[![Tests](https://img.shields.io/badge/tests-21%20passing-success.svg)]()
[![License](https://img.shields.io/badge/Academic-Anna%20University%20R2025-blue.svg)]()

> **Anna University Regulations 2025 (R2025), Semester 3 Capstone Project**  
> **Evaluation Window:** July 24 – October 10, 2026 | **Builder:** Solo Capstone  
> **Package Namespace:** `com.preethi.preethimart` | **Release Version:** `v1.1.0`

---

## 📖 Project Overview
**PreethiMart** is a production-grade multi-vendor e-commerce platform built strictly from first principles using core Java Enterprise APIs (**Java Servlets**, **JSP & JSTL**, **JDBC PreparedStatement**, **HikariCP**, and **Apache Tomcat 9.0**). Designed without monolithic framework magic, it delivers high performance, ironclad transaction guarantees, role-based access control, and an integrated AI shopping assistant (**PreethiBot**).

---

## 🏛️ Architecture & Tech Stack

```
+---------------------------------------------------------------------------------+
|                                 Client Tier                                     |
|  Responsive HTML5/CSS3 Views | JSTL (XML-Escaped) | Floating PreethiBot Widget  |
+---------------------------------------------------------------------------------+
                                         | HTTP/S
                                         v
+---------------------------------------------------------------------------------+
|                              Filter & Routing Tier                              |
|           EncodingFilter (UTF-8)    --->    AuthFilter (Role RBAC)              |
+---------------------------------------------------------------------------------+
                                         |
                                         v
+---------------------------------------------------------------------------------+
|                             Servlet Controller Tier                             |
|  AuthServlet | ProductServlet | CartServlet | CheckoutServlet | OrderServlet    |
|  SellerServlet | AdminServlet | ReviewServlet | WishlistServlet | ChatApiServlet|
+---------------------------------------------------------------------------------+
                                         |
                                         v
+---------------------------------------------------------------------------------+
|                               Business Service Tier                             |
|  UserService | ProductService | CartService | OrderService | ChatbotService     |
+---------------------------------------------------------------------------------+
                                         |
                                         v
+---------------------------------------------------------------------------------+
|                            Data Access Layer (DAO)                              |
|  UserDAO | ProductDAO | CartDAO | OrderDAO | ReviewDAO | WishlistDAO (Prepared) |
+---------------------------------------------------------------------------------+
                                         |
                                         v
+---------------------------------------------------------------------------------+
|                         Persistence & Connection Pool                           |
|       HikariCP Connection Pool (Singleton)  --->  H2 Relational Database        |
+---------------------------------------------------------------------------------+
```

### Technology Matrix
| Layer | Technology | Details / Purpose |
|:---|:---|:---|
| **Language** | Java 17 LTS | Modern LTS Java runtime with strong typing |
| **Server / Web** | Apache Tomcat 9.0.86 / Embedded | Servlet 4.0 (`javax.servlet.*`) compliant container |
| **Templating** | JSP 2.3 + JSTL 1.2 | Server-side rendering with mandatory `<c:out>` XSS escaping |
| **Persistence** | JDBC + HikariCP 5.1.0 | Fast, zero-leakage connection pooling |
| **Database** | H2 Database 2.2.224 | In-memory for testing, file-persisted for deployment |
| **Security** | jBCrypt 0.4 | Adaptive salted password hashing (10 rounds) |
| **JSON & REST** | Jackson Databind 2.16.1 | High-speed JSON serialization for `/api/v1/` endpoints |
| **AI Assistant** | Gemini API + Mock Engine | Provider abstraction (`ChatProvider`) with rate limiting |
| **Testing** | JUnit 5 + Mockito 5.11 | 21 automated unit, integration, and DAO tests |

---

## 📐 Architecture Diagrams (Final Deliverables)
PreethiMart includes complete, verified UML specifications:
- 📊 **[D1 Entity-Relationship Diagram](docs/D1_ER_DIAGRAM.md):** 3NF relational model, primary/foreign keys, and indexes.
- 👥 **[D2 Use Case Diagram](docs/D2_USE_CASE_DIAGRAM.md):** Actor interactions for Buyer, Seller, Admin, and AI Assistant.
- ⚡ **[D3 Sequence Diagram](docs/D3_SEQUENCE_DIAGRAM.md):** Step-by-step place-order and mock payment execution trace.

---

## 🚀 Quickstart & Running Locally

### 1. Requirements
- JDK 17+ installed and configured
- Apache Maven 3.9+ (bundled at `C:\Users\hp\apache-maven-3.9.6\bin\mvn.cmd`)

### 2. Run Automated Tests
```powershell
mvn clean test
```
*Output: 21 tests run, 0 failures, 0 errors, 100% BUILD SUCCESS.*

### 3. Launch the Application
Double-click `run.bat` or run via Maven:
```powershell
.\run.bat
# Or:
mvn compile exec:java
```

Visit the application in your browser:
```
http://localhost:8080/
```

Health Check Endpoint:
```
http://localhost:8080/api/v1/health  ==> {"status":"UP","db":"UP"}
```

---

## 🔑 Demo Accounts & Credentials
The application automatically seeds realistic demo data on initial boot:
- **Buyer:** `buyer1@preethimart.com` / `Buyer@123`
- **Seller:** `seller1@preethimart.com` / `Seller@123`
- **Admin:** `admin@preethimart.com` / `Admin@123`

*(The Sign In page features quick-fill buttons to populate credentials instantly!)*

---

## ✨ Features by Module

### Core Flow (F1–F8)
- **F1 Authentication:** Role-based registration/login, session ID regeneration, and explicit 30-minute timeout.
- **F2 Merchant Listings:** Product CRUD with pricing, categories, and inventory tracking.
- **F3 Discovery:** Keyword search, category tabs, and pagination.
- **F4 Shopping Cart:** Dynamic item addition, quantity +/- updating, removal, and running totals.
- **F5 Mock Checkout:** Payment confirmation (Card, UPI, NetBanking) with atomic stock deduction and invoice receipt.
- **F6 Order Audit:** Buyer order history with timeline and Seller incoming order fulfillment view.
- **F7 Administration:** Platform overview KPIs (GMV, Users, Orders), user directory, and listing moderation.
- **F8 Verified Reviews:** Verified purchase 1–5 star reviews and aggregate ratings.

### Advanced Enhancements (O1–O4)
- **O1 Wishlist:** Save-for-later functionality with one-click move-to-cart migration.
- **O2 Order Workflow:** Formal state progression (`Pending` &rarr; `Confirmed` &rarr; `Shipped` &rarr; `Delivered`).
- **O3 Inventory Alerts:** Real-time low-stock threshold indicators for merchants.
- **O4 PreethiBot AI:** 24/7 floating assistant supporting domain FAQs, recommendations, and rate limiting (10 msg/min).

---

## 📄 Documentation Index
- 📑 **[Final Project Report](docs/FINAL_PROJECT_REPORT.md)**
- 🖥️ **[Slide Deck Presentation (Markdown)](docs/PRESENTATION.md)** | **[Interactive Slides (HTML)](docs/slides.html)**
- 🎬 **[Live Demo Script](docs/DEMO_SCRIPT.md)**
- 🧪 **[Test Matrix & Cases Sheet](docs/TEST_CASES.md)**
- 🔄 **[Sprint Retrospectives (RETRO.md)](RETRO.md)**
- 🤝 **[Contributing Guide (CONTRIBUTING.md)](CONTRIBUTING.md)**

---

## 🛡️ License & Academic Integrity
Developed as a capstone project for **Anna University R2025 Semester 3**. All rights reserved.
