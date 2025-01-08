CREATE TABLE intervals
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    user_id    INT          NOT NULL,
    interval   BIGINT       NOT NULL,
    start_time TIMESTAMP, -- Nullable start_time indicates "disabled"
    CONSTRAINT unique_interval_name
        UNIQUE (name)
);

DROP TABLE IF EXISTS object_interval CASCADE;
