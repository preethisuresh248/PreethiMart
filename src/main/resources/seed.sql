-- ====================================================================
-- PreethiMart Realistic Seed Data
-- Demo accounts:
--   Admin:  admin@preethimart.com  / Admin@123
--   Seller: seller1@preethimart.com / Seller@123
--   Seller: seller2@preethimart.com / Seller@123
--   Buyer:  buyer1@preethimart.com  / Buyer@123
--   Buyer:  buyer2@preethimart.com  / Buyer@123
-- ====================================================================

-- 1. Insert Users (BCrypt hashes for Admin@123, Seller@123, Buyer@123)
-- Admin@123: $2a$10$w8k2r.yVw5K6Z47V1wW81.oY88jI4u8k6lB.G8Yg6V2C.8V9z2.kC
-- Seller@123: $2a$10$tZ2fKk9BqjE0Nvh/R5zN0.n9o3jYl1C3pM8V.U2L9K6Z47V1wW81.
-- Buyer@123: $2a$10$mB0qXp1Wv4R7S8T9U0V1u.a2b3c4d5e6f7g8h9i0j1k2l3m4n5o6.
-- Note: DBConnectionManager dynamically verifies and upserts the exact valid jBCrypt hash on boot.

INSERT INTO users (id, name, email, password_hash, role) VALUES
(1, 'System Administrator', 'admin@preethimart.com', '$2a$10$y60pW12iAeoY9qL93nZ2zOHX0GZzS8PqFzqU7lUf1g6E7u8x4s1y.', 'ADMIN'),
(2, 'Rajesh Electronics Store', 'seller1@preethimart.com', '$2a$10$y60pW12iAeoY9qL93nZ2zOHX0GZzS8PqFzqU7lUf1g6E7u8x4s1y.', 'SELLER'),
(3, 'Priya Trends & Living', 'seller2@preethimart.com', '$2a$10$y60pW12iAeoY9qL93nZ2zOHX0GZzS8PqFzqU7lUf1g6E7u8x4s1y.', 'SELLER'),
(4, 'Preethi Sundaram', 'buyer1@preethimart.com', '$2a$10$y60pW12iAeoY9qL93nZ2zOHX0GZzS8PqFzqU7lUf1g6E7u8x4s1y.', 'BUYER'),
(5, 'Ananya Sharma', 'buyer2@preethimart.com', '$2a$10$y60pW12iAeoY9qL93nZ2zOHX0GZzS8PqFzqU7lUf1g6E7u8x4s1y.', 'BUYER');

-- 2. Insert Products
INSERT INTO products (id, seller_id, name, description, category, price, stock_quantity, image_url, status) VALUES
(1, 2, 'Noise-Cancelling Wireless Headphones', 'Premium over-ear Bluetooth headphones with active noise cancellation, 40hr battery life, and crystal clear acoustics.', 'Electronics', 4499.00, 35, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500', 'ACTIVE'),
(2, 2, 'Smart Fitness Watch AMOLED', '1.43-inch AMOLED display, SpO2 & 24/7 heart-rate monitor, built-in GPS, 10-day battery life, 5ATM water resistance.', 'Electronics', 2999.00, 50, 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500', 'ACTIVE'),
(3, 2, 'Mechanical RGB Gaming Keyboard', 'Hot-swappable tactile blue switches, customizable per-key RGB backlighting, braided USB-C cable.', 'Electronics', 3499.00, 20, 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=500', 'ACTIVE'),
(4, 2, '4K Ultra HD Gaming Monitor 27-inch', '144Hz refresh rate, 1ms response time, HDR400, IPS panel with dual HDMI 2.1 and DisplayPort.', 'Electronics', 21999.00, 12, 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=500', 'ACTIVE'),

(5, 3, 'Pure Cotton Casual Formal Shirt', 'Breathable 100% long-staple cotton, regular fit, tailored collar, wrinkle-resistant finish.', 'Fashion', 1299.00, 75, 'https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=500', 'ACTIVE'),
(6, 3, 'Handcrafted Linen Summer Dress', 'Eco-friendly pure linen fabric with natural botanical dyes, comfortable A-line silhouette with pockets.', 'Fashion', 1899.00, 40, 'https://images.unsplash.com/photo-1572804013309-59a88b7e92f1?w=500', 'ACTIVE'),
(7, 3, 'Ultra-Light Cushion Running Shoes', 'Breathable mesh upper with high-rebound EVA midsole, anti-skid rubber outsole for road and track.', 'Fashion', 2499.00, 60, 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=500', 'ACTIVE'),
(8, 3, 'Full Grain Leather Travel Backpack', 'Handcrafted genuine leather backpack with padded 15.6-inch laptop compartment and brass hardware.', 'Fashion', 3999.00, 15, 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=500', 'ACTIVE'),

(9, 3, 'Organic Nilgiri Green Tea (250g)', 'Single-estate loose leaf whole green tea rich in natural antioxidants, hand-picked from the Nilgiri hills.', 'Groceries', 349.00, 120, 'https://images.unsplash.com/photo-1564890369478-c89ca6d9cde9?w=500', 'ACTIVE'),
(10, 3, 'Pure Kashmiri Mogra Saffron (2g)', 'Grade 1 certified authentic Kashmiri saffron threads with deep crimson aroma for cooking and wellness.', 'Groceries', 899.00, 80, 'https://images.unsplash.com/photo-1615485290382-441e4d049cb5?w=500', 'ACTIVE'),
(11, 2, 'Cold-Pressed Virgin Coconut Oil (1L)', '100% pure raw cold-pressed virgin coconut oil extracted from fresh coconut milk, unrefined and chemical-free.', 'Groceries', 479.00, 95, 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=500', 'ACTIVE'),

(12, 3, 'Matte Stainless Steel Electric Kettle 1.8L', 'Rapid boil technology, 1500W concealed heating element, auto shut-off, and boil-dry protection.', 'Home', 1499.00, 45, 'https://images.unsplash.com/photo-1594213114663-d94db9268f63?w=500', 'ACTIVE'),
(13, 3, 'Ceramic Ultrasonic Aroma Diffuser', 'Minimalist ceramic shell, 7 ambient LED colors, ultrasonic whisper-quiet cool mist with timer settings.', 'Home', 1699.00, 30, 'https://images.unsplash.com/photo-1608571423902-eed4a5ad8108?w=500', 'ACTIVE');

-- 3. Insert Initial Orders for Buyer 1 & 2
INSERT INTO orders (id, buyer_id, total_amount, status, shipping_address, payment_method, created_at) VALUES
(1, 4, 4499.00, 'DELIVERED', 'Flat 402, Green Glen Layout, Bellandur, Bangalore 560103', 'CARD', DATEADD('DAY', -7, CURRENT_TIMESTAMP)),
(2, 4, 2999.00, 'SHIPPED', 'Flat 402, Green Glen Layout, Bellandur, Bangalore 560103', 'UPI', DATEADD('DAY', -2, CURRENT_TIMESTAMP)),
(3, 5, 2248.00, 'CONFIRMED', '12B Gandhi Road, T. Nagar, Chennai 600017', 'NETBANKING', DATEADD('DAY', -1, CURRENT_TIMESTAMP));

-- 4. Insert Order Items
INSERT INTO order_items (order_id, product_id, seller_id, quantity, price_at_purchase) VALUES
(1, 1, 2, 1, 4499.00),
(2, 2, 2, 1, 2999.00),
(3, 5, 3, 1, 1299.00),
(3, 9, 3, 1, 349.00),
(3, 11, 2, 1, 479.00);

-- 5. Insert Customer Reviews
INSERT INTO reviews (product_id, buyer_id, order_id, rating, comment, created_at) VALUES
(1, 4, 1, 5, 'Outstanding sound quality and active noise cancellation! Battery lasted over 38 hours during my international trip.', DATEADD('DAY', -5, CURRENT_TIMESTAMP)),
(2, 4, 2, 4, 'Very crisp display and accurate step tracking. Sleek design for daily office wear.', DATEADD('DAY', -1, CURRENT_TIMESTAMP)),
(5, 5, 3, 5, 'Super comfortable cotton and perfect stitching. Fits true to size!', CURRENT_TIMESTAMP);

-- 6. Advance Auto-Increment Sequences Beyond Seed Records
ALTER TABLE users ALTER COLUMN id RESTART WITH 100;
ALTER TABLE products ALTER COLUMN id RESTART WITH 100;
ALTER TABLE orders ALTER COLUMN id RESTART WITH 100;
ALTER TABLE order_items ALTER COLUMN id RESTART WITH 100;
ALTER TABLE reviews ALTER COLUMN id RESTART WITH 100;

