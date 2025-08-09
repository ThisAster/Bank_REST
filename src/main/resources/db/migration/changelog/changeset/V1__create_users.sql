CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role VARCHAR(50) NOT NULL,
                            CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                            CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role)
);