# D3: Sequence Diagram — PreethiMart Place Order & Checkout Flow
**Anna University R2025 Semester 3 Capstone**

The following sequence diagram details the complete end-to-end execution flow of the **Place Order & Mock Payment Confirmation Journey** (F4 -> F5 -> F6), demonstrating strict architectural layering across **Browser**, **AuthFilter**, **CheckoutServlet**, **OrderService**, **ProductDAO**, **CartDAO**, **OrderDAO**, and the **H2 Relational Database**.

```mermaid
sequenceDiagram
    autonumber
    actor Buyer as 🧑 Buyer Browser
    participant AuthFilter as 🛡️ AuthFilter
    participant CheckoutServlet as ⚡ CheckoutServlet
    participant OrderService as 💼 OrderService
    participant CartService as 🛒 CartService
    participant ProductDAO as 📦 ProductDAO
    participant CartDAO as 🧺 CartDAO
    participant OrderDAO as 📑 OrderDAO
    participant DB as 🗄️ H2 Database

    Buyer->>AuthFilter: POST /checkout (address, paymentMethod="MOCK_CARD")
    AuthFilter->>AuthFilter: Validate active session & role == 'BUYER'
    
    alt Unauthenticated
        AuthFilter-->>Buyer: 302 Redirect to /auth/login
    end

    AuthFilter->>CheckoutServlet: Forward validated request
    CheckoutServlet->>OrderService: placeOrder(buyerId, address, paymentMethod)

    OrderService->>CartService: getCart(buyerId)
    CartService-->>OrderService: CartDTO (items, subtotal, tax, grandTotal)

    alt Cart is Empty
        OrderService-->>CheckoutServlet: throw ValidationException("Cart is empty")
        CheckoutServlet-->>Buyer: Render checkout.jsp with error alert
    end

    OrderService->>CartDAO: findByUserId(buyerId)
    CartDAO-->>OrderService: List<CartItem>

    loop For each item in cart
        OrderService->>ProductDAO: findById(productId)
        ProductDAO-->>OrderService: Product (stockQuantity, price)
        OrderService->>OrderService: Validate stockQuantity >= requested quantity
    end

    OrderService->>OrderDAO: createOrder(order, orderItems)
    
    rect rgb(240, 248, 255)
        note over OrderDAO, DB: Begin Atomic JDBC Transaction (conn.setAutoCommit(false))
        OrderDAO->>DB: INSERT INTO orders (buyer_id, total_amount, status='CONFIRMED', ...)
        DB-->>OrderDAO: Generated orderId (e.g. 101)

        loop For each purchased item
            OrderDAO->>DB: INSERT INTO order_items (order_id, product_id, seller_id, qty, price)
            OrderDAO->>DB: UPDATE products SET stock_quantity = stock_quantity - ? WHERE id = ?
        end

        OrderDAO->>DB: DELETE FROM cart_items WHERE user_id = ?
        OrderDAO->>DB: COMMIT TRANSACTION
        note over OrderDAO, DB: Transaction Committed Successfully
    end

    OrderDAO-->>OrderService: Created Order object
    OrderService-->>CheckoutServlet: OrderDTO
    CheckoutServlet-->>Buyer: 302 Redirect to /checkout/success?orderId=101
    
    Buyer->>CheckoutServlet: GET /checkout/success?orderId=101
    CheckoutServlet->>OrderService: getOrderById(101)
    OrderService-->>CheckoutServlet: OrderDTO
    CheckoutServlet-->>Buyer: Render order-success.jsp (Invoice & confirmation checkmark)
```
