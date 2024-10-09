DROP TABLE IF EXISTS user_likes;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS images;
DROP TABLE IF EXISTS images_data;
DROP TABLE IF EXISTS prompts_data;
DROP TABLE IF EXISTS users;

CREATE SEQUENCE users_seq START WITH 51 INCREMENT BY 50;
CREATE SEQUENCE images_seq START WITH 51 INCREMENT BY 50;
CREATE SEQUENCE images_data_seq START WITH 51 INCREMENT BY 50;
CREATE SEQUENCE prompts_data_seq START WITH 51 INCREMENT BY 50;
CREATE SEQUENCE comment_id_seq START WITH 51 INCREMENT BY 50;

CREATE TABLE users (
                       id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
                       password VARCHAR(255),
                       username VARCHAR(255),
                       PRIMARY KEY (id)
);

CREATE TABLE images_data (
                             id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
                             name VARCHAR(255),
                             path VARCHAR(255),
                             type VARCHAR(255),
                             PRIMARY KEY (id)
);

CREATE TABLE prompts_data (
                              id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
                              guidance_scale INTEGER,
                              height INTEGER,
                              negative_prompt VARCHAR(255),
                              num_interference_steps INTEGER,
                              prompt VARCHAR(255),
                              width INTEGER,
                              PRIMARY KEY (id)
);

CREATE TABLE images (
                        id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
                        is_public BOOLEAN NOT NULL,
                        author_id BIGINT,
                        image_data_id BIGINT,
                        prompt_data_id BIGINT,
                        PRIMARY KEY (id),
                        UNIQUE (prompt_data_id),
                        UNIQUE (image_data_id),
                        CONSTRAINT fk_images_prompt_data FOREIGN KEY (prompt_data_id) REFERENCES prompts_data(id),
                        CONSTRAINT fk_images_image_data FOREIGN KEY (image_data_id) REFERENCES images_data(id),
                        CONSTRAINT fk_images_author FOREIGN KEY (author_id) REFERENCES users(id)
);

CREATE TABLE comment (
                         id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
                         body VARCHAR(255),
                         created_at TIMESTAMP NOT NULL,
                         author_id BIGINT,
                         image_id BIGINT,
                         PRIMARY KEY (id),
                         CONSTRAINT fk_comment_author FOREIGN KEY (author_id) REFERENCES users(id),
                         CONSTRAINT fk_comment_image FOREIGN KEY (image_id) REFERENCES images(id)
);

CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            roles VARCHAR(255),
                            PRIMARY KEY (user_id),
                            CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE user_likes (
                            image_id BIGINT NOT NULL,
                            user_id BIGINT NOT NULL,
                            PRIMARY KEY (image_id, user_id),
                            CONSTRAINT fk_user_likes_user FOREIGN KEY (user_id) REFERENCES users(id),
                            CONSTRAINT fk_user_likes_image FOREIGN KEY (image_id) REFERENCES images(id)
);