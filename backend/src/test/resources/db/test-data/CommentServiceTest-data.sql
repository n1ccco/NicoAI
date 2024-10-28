INSERT INTO users (id, password, username)
VALUES (1, 'password1', 'user');
INSERT INTO users (id, password, username)
VALUES (2, 'password2', 'author');
INSERT INTO users (id, password, username)
VALUES (3, 'password3', 'otherUser');

ALTER TABLE users
    ALTER COLUMN id RESTART WITH 4;

INSERT INTO user_roles (user_id, roles)
VALUES (1, 'ROLE_USER');
INSERT INTO user_roles (user_id, roles)
VALUES (2, 'ROLE_USER');
INSERT INTO user_roles (user_id, roles)
VALUES (3, 'ROLE_USER');

INSERT INTO images (id, visibility, author_id)
VALUES (1, 'PUBLIC', 1);
INSERT INTO images (id, visibility, author_id)
VALUES (2, 'PRIVATE', 2);

ALTER TABLE images
    ALTER COLUMN id RESTART WITH 3;

INSERT INTO comment (id, body, created_at, author_id, image_id)
VALUES (1, 'First comment', '2024-09-30 10:00:00', 1, 1);
INSERT INTO comment (id, body, created_at, author_id, image_id)
VALUES (2, 'Second comment', '2024-09-30 11:00:00', 1, 1);
INSERT INTO comment (id, body, created_at, author_id, image_id)
VALUES (3, 'Authors comment', '2024-09-30 12:00:00', 2, 1);

ALTER TABLE comment
    ALTER COLUMN id RESTART WITH 4;