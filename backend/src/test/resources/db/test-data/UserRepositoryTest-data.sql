INSERT INTO users (id, password, username)
VALUES (1, 'password1', 'user');

ALTER TABLE users
    ALTER COLUMN id RESTART WITH 2;