CREATE TABLE tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_tokens_user FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_tokens_user_id ON tokens(user_id);
