# PreethiMart Test Matrix & Quality Verification Sheet
**Anna University Regulations 2025 (R2025) — Semester 3 Capstone**

---

## 1. Automated Test Suite Summary
- **Test Framework:** JUnit 5 (Jupiter 5.10.2) + Mockito 5.11.0
- **Database Engine:** Embedded H2 In-Memory Database (`jdbc:h2:mem:preethimart`)
- **Total Tests Executed:** 21
- **Passed:** 21 (100% Success Rate)
- **Execution Command:** `mvn test`

| Test Class | Layer Tested | Cases Verified | Result |
|:---|:---|:---|:---:|
| `UserDAOTest` | DAO (JDBC H2) | User persistence, email lookup, seed admin account | Passed ✅ |
| `ProductDAOTest` | DAO (JDBC H2) | Catalog query, category filtering, keyword search, product CRUD | Passed ✅ |
| `CartDAOTest` | DAO (JDBC H2) | Add to cart, quantity update, item removal, cart item counter | Passed ✅ |
| `OrderDAOTest` | DAO (JDBC H2) | ACID transaction order placement, stock decrement, status update | Passed ✅ |
| `UserServiceTest` | Service (Mockito) | Input validation, duplicate email rejection, short password rejection, BCrypt verify | Passed ✅ |
| `ProductServiceTest` | Service (Mockito) | Negative price rejection, successful creation, unauthorized merchant edit prevention | Passed ✅ |
| `OrderServiceTest` | Service (Mockito) | Empty cart checkout rejection, insufficient stock rejection, status transition check | Passed ✅ |
| `ChatbotServiceTest` | AI & Service | Shipping FAQ resolution, 500-char input cap, 10 msg/min rate limiter enforcement | Passed ✅ |

---

## 2. Manual End-to-End Test Matrix
| Case ID | Feature Area | Pre-Condition | Steps to Execute | Expected Outcome | Actual Result |
|:---|:---|:---|:---|:---|:---:|
| **TC-01** | Registration (F1) | Guest user | Fill name, valid email, password, select BUYER role, submit | Account created, redirected to sign-in page with success alert | Passed ✅ |
| **TC-02** | Short Password (F1) | Guest user | Enter password < 6 characters | Validation error: "Password must be at least 6 characters" | Passed ✅ |
| **TC-03** | Duplicate Email (F1) | Existing user | Attempt to register with registered email address | Rejection: "An account with email already exists" | Passed ✅ |
| **TC-04** | Authentication (F1) | Registered user | Enter correct email and password | Login succeeds, session created, role-specific redirect | Passed ✅ |
| **TC-05** | Category Filter (F3) | Any user | Click "Electronics" chip on catalog | Only products in Electronics category displayed | Passed ✅ |
| **TC-06** | Search Filter (F3) | Any user | Search for "Headphones" in search bar | Only matching headphones listing returned | Passed ✅ |
| **TC-07** | Add to Cart (F4) | Logged-in Buyer | Click "Add to Cart" on in-stock product | Item added, cart count badge increments | Passed ✅ |
| **TC-08** | Out of Stock (F4) | Logged-in Buyer | Try adding out-of-stock item | "Out of stock" badge displayed; form prevents addition | Passed ✅ |
| **TC-09** | Cart Calculations (F4) | Buyer with cart | Change quantity to 3 in cart | Subtotal updates dynamically; 5% GST and shipping updated | Passed ✅ |
| **TC-10** | Empty Cart Checkout (F5)| Buyer with 0 items | Attempt to access checkout URL | Redirects to cart view with empty basket notice | Passed ✅ |
| **TC-11** | Mock Checkout (F5) | Buyer with items | Enter shipping address, select "Mock UPI", place order | Order created; stock decremented; cart cleared; invoice shown | Passed ✅ |
| **TC-12** | Seller Listing (F2) | Logged-in Seller | Open Dashboard &rarr; Add product with valid inputs | New product appears in inventory table and public catalog | Passed ✅ |
| **TC-13** | Foreign Edit (F2) | Seller B | Attempt to edit Seller A's product | System rejects: "Unauthorized: You can only edit your own listings" | Passed ✅ |
| **TC-14** | Order Workflow (O2) | Seller | Receive order & change status `Confirmed` &rarr; `Shipped` | Status updates in DB and reflects on buyer's order history | Passed ✅ |
| **TC-15** | Admin Moderation (F7)| Admin | Click "Suspend" on product listing | Product status becomes `MODERATED`; removed from catalog | Passed ✅ |
| **TC-16** | Verified Review (F8) | Buyer | Submit review on purchased product | Review published with 5★ rating and Verified badge | Passed ✅ |
| **TC-17** | Chat Rate Limit (O4) | Any session | Send 11 rapid chat queries within 60s | 11th message blocked: "Rate limit exceeded (10 msg/min)" | Passed ✅ |

---

## 3. Basic Load Testing Plan (Apache JMeter / ApacheBench)
Per Week 7 requirements (minimum 10 concurrent users for 60 seconds):

### Benchmark with ApacheBench (`ab`):
```bash
ab -n 1000 -c 10 http://localhost:8080/products
```

### Benchmark Metrics:
- **Target Concurrency:** 10 concurrent worker threads
- **Target Duration:** 60 seconds continuous traffic
- **Target Throughput:** $\ge 250$ requests/second
- **Response Time ($p99$):** $< 50\text{ms}$ (due to HikariCP in-memory connection pooling and zero JVM reflection overhead)
- **HTTP Error Rate:** 0.00%
