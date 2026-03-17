-- Seed post_likes: post 1 gets 2 likes (users 1 and 2), post 2 gets 1 like (user 3)
INSERT INTO post_likes (post_id, user_id, created_at)
VALUES (1, 1, CURRENT_TIMESTAMP),
       (1, 2, CURRENT_TIMESTAMP),
       (2, 3, CURRENT_TIMESTAMP);