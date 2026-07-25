-- ====================================================================
-- Migration V2: Order Status Workflow Enhancement
-- Supports lifecycle: PENDING -> CONFIRMED -> SHIPPED -> DELIVERED, plus CANCELLED
-- ====================================================================

-- Ensure status check constraint allows all workflow states
ALTER TABLE orders DROP CONSTRAINT IF EXISTS orders_status_check;
ALTER TABLE orders ADD CONSTRAINT orders_status_check 
    CHECK (status IN ('PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED'));

-- Add tracking metadata columns if not present
ALTER TABLE orders ADD COLUMN IF NOT EXISTS tracking_number VARCHAR(100);
ALTER TABLE orders ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Index for fast order status filtering
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
