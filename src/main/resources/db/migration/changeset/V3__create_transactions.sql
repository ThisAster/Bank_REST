CREATE TABLE transactions (
                              id BIGSERIAL PRIMARY KEY,
                              from_card_id BIGINT,
                              to_card_id BIGINT,
                              amount DECIMAL(19,2) NOT NULL,
                              currency VARCHAR(50),
                              timestamp TIMESTAMP,
                              status VARCHAR(50),
                              CONSTRAINT fk_transactions_from_card FOREIGN KEY (from_card_id) REFERENCES cards (id) ON DELETE SET NULL,
                              CONSTRAINT fk_transactions_to_card FOREIGN KEY (to_card_id) REFERENCES cards (id) ON DELETE SET NULL
);