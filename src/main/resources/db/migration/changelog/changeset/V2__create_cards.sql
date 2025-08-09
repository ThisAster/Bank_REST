CREATE TABLE cards (
                       id BIGSERIAL PRIMARY KEY,
                       encrypted_card_number VARCHAR(255) UNIQUE NOT NULL,
                       owner_id BIGINT,
                       expiration_date TIMESTAMP,
                       status VARCHAR(50),
                       balance DECIMAL(19, 2),
                       CONSTRAINT fk_cards_owner FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE SET NULL
);