# D1: Entity-Relationship (ER) Diagram — PreethiMart
**Anna University R2025 Semester 3 Capstone**

The following Mermaid diagram visualizes the complete database schema for **PreethiMart**, including entities, primary keys, foreign keys, relationships, and data types (utilizing `DECIMAL(10,2)` for financial precision and audit timestamps).

```mermaid
erDiagram
    USERS ||--o{ PRODUCTS : "sells"
    USERS ||--o{ ORDERS : "places (buyer)"
    USERS ||--o{ CART_ITEMS : "maintains"
    USERS ||--o{ REVIEWS : "writes"
    USERS ||--o{ WISHLIST_ITEMS : "saves"
    USERS ||--o{ ORDER_ITEMS : "fulfills (seller)"

    PRODUCTS ||--o{ CART_ITEMS : "added to"
    PRODUCTS ||--o{ ORDER_ITEMS : "purchased in"
    PRODUCTS ||--o{ REVIEWS : "receives"
    PRODUCTS ||--o{ WISHLIST_ITEMS : "listed in"

    ORDERS ||--|{ ORDER_ITEMS : "contains"
    ORDERS ||--o{ REVIEWS : "verified in"

    USERS {
        BIGINT id PK "AUTO_INCREMENT"
        VARCHAR name "Full user name"
        VARCHAR email UK "Unique lowercased email"
        VARCHAR password_hash "BCrypt hashed password"
        VARCHAR role "BUYER | SELLER | ADMIN"
        TIMESTAMP created_at "Registration timestamp"
    }

    PRODUCTS {
        BIGINT id PK "AUTO_INCREMENT"
        BIGINT seller_id FK "References USERS(id)"
        VARCHAR name "Product title"
        TEXT description "Detailed specs"
        VARCHAR category "Electronics | Fashion | Groceries | Home"
        DECIMAL price "DECIMAL(10,2) selling price"
        INT stock_quantity "Available inventory count"
        VARCHAR image_url "Thumbnail CDN URL"
        VARCHAR status "ACTIVE | INACTIVE | MODERATED"
        TIMESTAMP created_at "Creation timestamp"
    }

    CART_ITEMS {
        BIGINT id PK "AUTO_INCREMENT"
        BIGINT user_id FK "References USERS(id)"
        BIGINT product_id FK "References PRODUCTS(id)"
        INT quantity "Item units in cart"
        TIMESTAMP created_at "Added timestamp"
    }

    ORDERS {
        BIGINT id PK "AUTO_INCREMENT"
        BIGINT buyer_id FK "References USERS(id)"
        DECIMAL total_amount "DECIMAL(10,2) grand total"
        VARCHAR status "PENDING | CONFIRMED | SHIPPED | DELIVERED | CANCELLED"
        TEXT shipping_address "Delivery destination"
        VARCHAR payment_method "MOCK_CARD | MOCK_UPI | MOCK_NETBANKING"
        VARCHAR tracking_number "Shipment tracking code"
        TIMESTAMP created_at "Order placement timestamp"
        TIMESTAMP updated_at "Workflow transition timestamp"
    }

    ORDER_ITEMS {
        BIGINT id PK "AUTO_INCREMENT"
        BIGINT order_id FK "References ORDERS(id)"
        BIGINT product_id FK "References PRODUCTS(id)"
        BIGINT seller_id FK "References USERS(id)"
        INT quantity "Purchased units"
        DECIMAL price_at_purchase "DECIMAL(10,2) price frozen at checkout"
        TIMESTAMP created_at "Item timestamp"
    }

    REVIEWS {
        BIGINT id PK "AUTO_INCREMENT"
        BIGINT product_id FK "References PRODUCTS(id)"
        BIGINT buyer_id FK "References USERS(id)"
        BIGINT order_id FK "References ORDERS(id) verified order"
        INT rating "1 to 5 star rating"
        TEXT comment "Customer feedback text"
        TIMESTAMP created_at "Review submission timestamp"
    }

    WISHLIST_ITEMS {
        BIGINT id PK "AUTO_INCREMENT"
        BIGINT user_id FK "References USERS(id)"
        BIGINT product_id FK "References PRODUCTS(id)"
        TIMESTAMP created_at "Saved timestamp"
    }
```
