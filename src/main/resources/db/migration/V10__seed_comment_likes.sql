-- Seed comment_likes: comment 1 gets 2 likes (users 1 and 2)
INSERT INTO comment_likes (comment_id, user_id, created_at)
VALUES (1, 1, CURRENT_TIMESTAMP),
       (1, 2, CURRENT_TIMESTAMP);