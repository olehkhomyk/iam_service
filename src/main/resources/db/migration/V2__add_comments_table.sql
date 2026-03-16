CREATE TABLE comments
(
    id         BIGSERIAL PRIMARY KEY,
    post_id    INTEGER      NOT NULL,
    user_id    INTEGER      NOT NULL,
    content    TEXT         NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),

    CONSTRAINT FK_comments_post FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
    constraint FK_comments_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

INSERT INTO comments (post_id, user_id, content, created_at, created_by)
VALUES (2, 3, 'This is my first comment for post 2 in this application', CURRENT_TIMESTAMP, 'user');