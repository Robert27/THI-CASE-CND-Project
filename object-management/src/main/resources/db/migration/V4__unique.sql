ALTER TABLE objects
    ADD COLUMN user_id BIGINT NOT NULL;

ALTER TABLE objects
    ADD CONSTRAINT unique_reorder_url
        UNIQUE (user_id, reorder_url);

ALTER TABLE objects
    ADD CONSTRAINT unique_category_name
        UNIQUE (user_id, category_id, name);
