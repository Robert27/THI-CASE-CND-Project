ALTER TABLE objects
    ADD COLUMN interval_min INT NOT NULL DEFAULT 1;

ALTER TABLE objects
    DROP COLUMN reorder_interval;
