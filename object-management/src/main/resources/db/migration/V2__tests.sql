ALTER TABLE objects
    DROP COLUMN IF EXISTS order_cycle;

-- Remove 'price' column if it exists
ALTER TABLE objects
    DROP COLUMN IF EXISTS price;
