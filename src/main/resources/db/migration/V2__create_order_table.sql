CREATE TABLE IF NOT EXISTS "ws-tg-ui"."ws01_order_draft" (
    id BIGSERIAL NOT NULL,
    customer_id VARCHAR(100) NOT NULL,
    chat_id BIGINT NOT NULL,
    category VARCHAR(50) NOT NULL,
    short_description TEXT NOT NULL,
    detailed_description TEXT,
    amount NUMERIC,
    status VARCHAR(50) NOT NULL,
    send_process VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
    );