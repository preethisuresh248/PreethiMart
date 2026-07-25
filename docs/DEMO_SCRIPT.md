# PreethiMart Capstone Live Demo Script
**Anna University Regulations 2025 (R2025) — Semester 3 Final Review**
**Duration:** 8–10 Minutes

---

## Pre-Demo Checklist
1. Ensure Java 17 and Maven are available.
2. Launch PreethiMart via one-click runner:
   ```cmd
   .\run.bat
   ```
   Or via Maven:
   ```cmd
   mvn compile exec:java
   ```
3. Open browser at `http://localhost:8080/`.
4. Verify Health check returns UP: `http://localhost:8080/api/v1/health` &rarr; `{"status":"UP","db":"UP"}`.

---

## Demo Script Walkthrough

### Act 1: Introduction & Architecture (1 Minute)
- **Speaker:** "Good morning, respected evaluators. Today I present **PreethiMart**, an enterprise multi-vendor e-commerce platform built strictly with Java Servlets, JSP/JSTL, JDBC PreparedStatement, HikariCP, and H2 database under Anna University R2025 regulations.
- **Show:** The landing page catalog showing active items across Electronics, Fashion, Groceries, and Home essentials. Point out clean responsive design and role indicators in the navigation bar.

---

### Act 2: Shopper Journey (Browse, Cart & Mock Checkout) (3 Minutes)
- **Action 1 (Catalog & Search):**
  - Type `"Headphones"` in the search bar and press Enter.
  - Show instantaneous filtered results.
  - Click on **"Electronics"** category chip to view all electronic gadgets.
- **Action 2 (Product Details & Reviews):**
  - Click on **"Noise-Cancelling Wireless Headphones"**.
  - Highlight the 5.0★ verified buyer rating, stock counter (35 available), seller name, and full description.
- **Action 3 (Cart Operations):**
  - Click **"Add to Cart"**.
  - Navigate to Cart view. Update quantity to `2`. Observe running total, 5% estimated GST calculation, and Free Shipping rule applied.
- **Action 4 (Checkout & Mock Payment):**
  - Click **"Proceed to Checkout"**.
  - Enter delivery address: `"Anna University Campus, Chennai"`.
  - Select payment instrument: **"Mock UPI Payment"**.
  - Click **"Authorize & Place Order"**.
  - Show the instantaneous invoice confirmation screen with Order ID `#PM-100`.
- **Action 5 (Order Tracking):**
  - Click **"View in Orders"** to show the buyer's order history with status badge `CONFIRMED`.

---

### Act 3: Merchant Journey (Inventory & Order Fulfillment) (2.5 Minutes)
- **Action 1 (Switch Account):**
  - Click Logout. Click Sign In. Use the **"Seller"** quick-fill button (`seller1@preethimart.com` / `Seller@123`).
- **Action 2 (Seller Dashboard):**
  - Point out inventory levels, low-stock indicators, and active listing statuses.
- **Action 3 (Create New Listing):**
  - Click **"Add New Product"**. Fill in:
    - Name: `Ergonomic Wireless Trackball Mouse`
    - Category: `Electronics`
    - Price: `1499.00`
    - Stock: `25`
    - Description: `High precision Bluetooth trackball mouse.`
  - Submit and observe immediate inclusion in catalog.
- **Action 4 (Fulfill Incoming Orders):**
  - Click **"View Incoming Orders"**.
  - Find the buyer's recent order. Advance the status dropdown from `CONFIRMED` &rarr; `SHIPPED` &rarr; `DELIVERED`.
  - Notice the audit trail updates across the system.

---

### Act 4: Administrator Portal & Moderation (1.5 Minutes)
- **Action 1 (Admin Login):**
  - Logout and sign in via **"Admin"** quick-fill button (`admin@preethimart.com` / `Admin@123`).
- **Action 2 (Executive Overview):**
  - Show KPI cards: Total Registered Users, Total Orders Processed, Platform GMV, and Catalog Listings.
- **Action 3 (User & Catalog Moderation):**
  - Navigate to **"Listings Moderation"**. Click **"Suspend / Hide"** on an item to demonstrate real-time catalog moderation.
  - Verify that suspended items are instantly hidden from public buyer catalog.

---

### Act 5: PreethiBot AI Assistant (1.5 Minutes)
- **Action 1 (Floating Widget):**
  - Click the floating robot launcher at the bottom-right corner.
- **Action 2 (Domain Queries):**
  - Click quick chips: *"What is your shipping policy?"* and show instantaneous 2–4 day delivery policy response.
  - Ask: *"What are the best products available?"* and observe product recommendations.
  - Explain the resilience architecture: Rate-limited to 10 msg/min, in-memory cache, and fallback to canned responses if offline.

---

### Act 6: Code Quality & Test Suite (0.5 Minutes)
- **Action 1:** Switch to terminal and run:
  ```powershell
  mvn test
  ```
- **Action 2:** Highlight **21 tests passing**, 0 errors, 0 failures across DAO and Service layers.
- **Conclusion:** "Thank you! I am now ready for questions from the evaluation committee."
