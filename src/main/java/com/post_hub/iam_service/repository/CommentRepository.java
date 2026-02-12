package com.post_hub.iam_service.repository;

import com.post_hub.iam_service.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
