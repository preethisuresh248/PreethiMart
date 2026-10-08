# PreethiMart Cloud Deployment Guide

This guide provides step-by-step instructions for deploying **PreethiMart** to production cloud platforms. Thanks to multi-stage Docker containerization and dynamic port binding, PreethiMart is cloud-agnostic and runs seamlessly on all major cloud hosts.

---

## 🚀 Option 1: 1-Click Deploy on Render (Recommended & 100% Free)

[Render](https://render.com) offers free web service hosting, automatic SSL/TLS certificates (`https://`), and continuous deployment directly from your GitHub repository.

### Step-by-Step Instructions:
1. **Sign Up / Log In to Render:**
   - Go to [dashboard.render.com](https://dashboard.render.com/) and log in with your **GitHub account**.
2. **Create New Web Service:**
   - Click the **"New +"** button at the top right and select **"Web Service"**.
3. **Connect Your GitHub Repository:**
   - Choose `preethisuresh248/PreethiMart` from your list of repositories (or paste the repository URL: `https://github.com/preethisuresh248/PreethiMart`).
   - Click **Connect**.
4. **Configure the Service:**
   - **Name:** `preethimart` (or your preferred name)
   - **Region:** Any (e.g., *Oregon (US West)* or *Frankfurt (EU)*)
   - **Branch:** `main`
   - **Runtime:** **Docker** *(Render detects the `Dockerfile` automatically)*
   - **Instance Type:** Select **Free**
5. **Set Environment Variables (Optional):**
   - Click **Advanced** &rarr; **Add Environment Variable**:
     - `CHATBOT_PROVIDER` = `mock` *(or `gemini` if you have an API key)*
     - `GEMINI_API_KEY` = `your_gemini_api_key_here` *(optional)*
   - **Health Check Path:** `/api/v1/health`
6. **Deploy:**
   - Click **"Deploy Web Service"** at the bottom.
   - Render will pull your repository, build the multi-stage Docker container, and start Apache Tomcat.
   - Once the status changes to **"Live"**, your website will be accessible at:  
     `https://preethimart.onrender.com` (or your chosen service URL).

---

## 🚆 Option 2: Deploy on Railway (Fast & Zero Config)

[Railway](https://railway.app) automatically detects the `Dockerfile` in your GitHub repository and provisions a cloud container with public HTTPS.

### Step-by-Step Instructions:
1. Go to [railway.app](https://railway.app/) and sign in with **GitHub**.
2. Click **"+ New Project"** &rarr; **"Deploy from GitHub repo"**.
3. Select `preethisuresh248/PreethiMart`.
4. Railway will automatically detect the `Dockerfile` and start the build.
5. In your project settings, click **"Generate Domain"** under the **Networking** section to get your public `https://...up.railway.app` URL.

---

## ☁️ Option 3: Deploy on Google Cloud Run (Serverless Container)

If you prefer Google Cloud Platform (GCP):

```bash
# 1. Authenticate with Google Cloud
gcloud auth login
gcloud config set project YOUR_PROJECT_ID

# 2. Build and deploy container directly from source
gcloud run deploy preethimart \
  --source . \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --memory 512Mi \
  --port 8080
```

---

## 🧪 Post-Deployment Verification

Once deployed on your cloud platform, verify the deployment:

1. **Health API Check:**
   ```text
   GET https://<your-cloud-app-url>/api/v1/health
   Response: {"status":"UP","db":"UP"}
   ```
2. **Product Catalog:**
   - Visit `https://<your-cloud-app-url>/products` to verify the storefront loads with all catalog items.
3. **Demo User Login:**
   - **Buyer:** `buyer1@preethimart.com` / `Buyer@123`
   - **Seller:** `seller1@preethimart.com` / `Seller@123`
   - **Admin:** `admin@preethimart.com` / `Admin@123`
4. **PreethiBot AI Chat:**
   - Click the floating chat bubble on the bottom right and ask: *"What products are on sale?"*

---

## 🔒 Cloud Architecture & Database Persistence Note
- PreethiMart is configured with an in-memory embedded H2 database (`jdbc:h2:mem:preethimart;DB_CLOSE_DELAY=-1`) seeded on startup with realistic e-commerce products, buyers, sellers, and orders.
- For persistent production deployments across container restarts, you can configure an external PostgreSQL or MySQL database by supplying the `JDBC_URL`, `JDBC_USER`, and `JDBC_PASSWORD` environment variables.
