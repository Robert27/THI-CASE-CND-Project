ALTER TABLE objects
    DROP COLUMN interval_min;

CREATE TABLE IF NOT EXISTS object_interval
(
    id          SERIAL PRIMARY KEY,
    object_id   INTEGER   NOT NULL,
    interval    INT       NOT NULL,
    last_active TIMESTAMP NOT NULL,
    CONSTRAINT fk_object
        FOREIGN KEY (object_id)
            REFERENCES objects (id)
            ON DELETE CASCADE
);
