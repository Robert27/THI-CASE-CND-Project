ALTER TABLE db.public.objects
    ADD COLUMN interval_min INT NOT NULL DEFAULT 1;

ALTER TABLE db.public.objects
    DROP COLUMN reorder_interval;
