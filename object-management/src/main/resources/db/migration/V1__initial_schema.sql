CREATE TABLE categories (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(100) UNIQUE NOT NULL,
                            description TEXT
);

CREATE TABLE objects (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         description TEXT,
                         category_id INT REFERENCES categories(id) ON DELETE SET NULL,
                         order_cycle INTERVAL NOT NULL, -- z.B. '7 days', '1 month'
                         reorder_url TEXT,
                         price NUMERIC(10, 2),
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
