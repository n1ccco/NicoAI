INSERT INTO images (id, visibility, like_count)
VALUES (1, 'PUBLIC', 3);
INSERT INTO images (id, visibility, like_count)
VALUES (2, 'PUBLIC', 1);
INSERT INTO images (id, visibility, like_count)
VALUES (3, 'PRIVATE', 5);
INSERT INTO images (id, visibility, like_count)
VALUES (4, 'PUBLIC', 3);

ALTER TABLE images
    ALTER COLUMN id RESTART WITH 5;