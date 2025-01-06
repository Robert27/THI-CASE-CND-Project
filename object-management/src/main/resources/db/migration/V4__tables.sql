CREATE TABLE item_price_log
(
    id         SERIAL PRIMARY KEY,
    item_id    INTEGER        NOT NULL,
    price      NUMERIC(10, 2) NOT NULL,
    available  INTEGER        NOT NULL,
    checked_at TIMESTAMP      NOT NULL,
    CONSTRAINT fk_item
        FOREIGN KEY (item_id)
            REFERENCES objects (id)
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS order_object
(
    id           SERIAL PRIMARY KEY,
    item_id      INTEGER   NOT NULL,
    log_id       INTEGER,
    cycle_time   TIMESTAMP NOT NULL,
    order_status VARCHAR(20) CHECK (order_status IN ('OPEN', 'DONE', 'FAILED', 'ABORTED')),
    item_status  BOOLEAN   NOT NULL,
    CONSTRAINT fk_log
        FOREIGN KEY (log_id)
            REFERENCES item_price_log (id),
    CONSTRAINT fk_item
        FOREIGN KEY (item_id)
            REFERENCES objects (id)
);
