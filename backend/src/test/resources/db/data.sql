-- for H2 test database

INSERT INTO users (id, password, username) VALUES (1, 'password1', 'user');
INSERT INTO users (id, password, username) VALUES (2, 'password2', 'author');
INSERT INTO users (id, password, username) VALUES (3, 'password3', 'otherUser');


INSERT INTO images_data (id, name, path, type) VALUES (1, 'ImageData1', '/path/to/image1', 'jpeg');
INSERT INTO images_data (id, name, path, type) VALUES (2, 'ImageData2', '/path/to/image2', 'png');


INSERT INTO prompts_data (id, guidance_scale, height, negative_prompt, num_interference_steps, prompt, width)
VALUES (1, 7, 512, 'No negative', 50, 'A beautiful sunset', 512);
INSERT INTO prompts_data (id, guidance_scale, height, negative_prompt, num_interference_steps, prompt, width)
VALUES (2, 8, 256, 'No negative', 60, 'A majestic mountain', 256);


INSERT INTO images (id, visibility, author_id, image_data_id, prompt_data_id) VALUES (1, 'PUBLIC', 1, 1, 1);
INSERT INTO images (id, visibility, author_id, image_data_id, prompt_data_id) VALUES (2, 'PRIVATE', 2, 2, 2);


INSERT INTO comment (body, created_at, author_id, image_id) VALUES ('First comment', '2024-09-30 10:00:00', 1, 1);
INSERT INTO comment (body, created_at, author_id, image_id) VALUES ('Second comment', '2024-09-30 11:00:00', 1, 1);
INSERT INTO comment (body, created_at, author_id, image_id) VALUES ('Authors comment', '2024-09-30 12:00:00', 2, 1);


INSERT INTO user_roles (user_id, roles) VALUES (1, 'ROLE_USER');
INSERT INTO user_roles (user_id, roles) VALUES (2, 'ROLE_AUTHOR');
INSERT INTO user_roles (user_id, roles) VALUES (3, 'ROLE_USER');


INSERT INTO user_likes (image_id, user_id) VALUES (1, 1);
INSERT INTO user_likes (image_id, user_id) VALUES (1, 2);

ALTER TABLE users ALTER COLUMN id RESTART WITH 4;
ALTER TABLE images_data ALTER COLUMN id RESTART WITH 3;
ALTER TABLE images ALTER COLUMN id RESTART WITH 3;
ALTER TABLE prompts_data ALTER COLUMN id RESTART WITH 3;