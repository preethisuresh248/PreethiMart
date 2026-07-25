# Contributing & Local Development Guide — PreethiMart
**Anna University Regulations 2025 (R2025) — Semester 3 Capstone**

Thank you for contributing to **PreethiMart**! Follow this guide to set up your local development environment from a fresh clone.

---

## 1. Prerequisites
- **JDK:** Java SE Development Kit 17 (or compatible 17+)
- **Build Tool:** Apache Maven 3.9+
- **Git:** Git 2.30+

---

## 2. Quickstart: Clone to Running Instance

### Step 1: Clone the Repository
```bash
git clone https://github.com/your-username/PreethiMart.git
cd PreethiMart
```

### Step 2: Configure Environment
Copy the example environment configuration:
```bash
# On Linux / macOS
cp .env.example .env
cp config.properties.example config.properties

# On Windows PowerShell
Copy-Item .env.example .env
Copy-Item config.properties.example config.properties
```
*Note: Both `.env` and `config.properties` are strictly excluded from version control via `.gitignore`.*

### Step 3: Compile & Run Automated Tests
Execute the verification suite to ensure dependencies and schema initialize cleanly:
```bash
mvn clean test
```
All 21 unit and integration tests should pass with `BUILD SUCCESS`.

### Step 4: Launch the Local Server
Start the standalone embedded Tomcat 9 web application server:

**Option A (Windows One-Click):**
```cmd
.\run.bat
```

**Option B (Maven Direct):**
```bash
mvn compile exec:java
```

**Option C (Standard WAR build for external Tomcat):**
```bash
mvn clean package
# Deploy target/preethimart.war to your Tomcat 9.0.x webapps/ directory
```

### Step 5: Access the Web Application
Open your web browser and navigate to:
```
http://localhost:8080/
```
Verify the health check endpoint:
```
http://localhost:8080/api/v1/health
# Response: {"status":"UP","db":"UP"}
```

---

## 3. Demo Credentials
The application is pre-seeded with realistic test accounts:
| Role | Email | Password | Access Level |
|:---|:---|:---|:---|
| **Buyer** | `buyer1@preethimart.com` | `Buyer@123` | Catalog, Cart, Checkout, Order History, Reviews, Wishlist |
| **Buyer** | `buyer2@preethimart.com` | `Buyer@123` | Catalog, Cart, Checkout, Order History |
| **Seller** | `seller1@preethimart.com` | `Seller@123` | Merchant Dashboard, Listings CRUD, Order Fulfillment |
| **Seller** | `seller2@preethimart.com` | `Seller@123` | Merchant Dashboard, Listings CRUD, Order Fulfillment |
| **Admin** | `admin@preethimart.com` | `Admin@123` | Full Console, User Audit, Order Ledger, Listing Moderation |

---

## 4. Branching Model & Commit Convention
- Work in dedicated feature branches: `feature/<feature-name>`.
- Keep `main` always deployable.
- Use conventional commit prefixes:
  - `feat:` for new capabilities
  - `fix:` for bug fixes
  - `test:` for test additions and updates
  - `docs:` for documentation and diagram updates
- Merge via self-reviewed pull requests.
