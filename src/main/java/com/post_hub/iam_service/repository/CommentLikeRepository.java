package com.post_hub.iam_service.repository;

import com.post_hub.iam_service.model.entity.CommentLike;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
	Page<CommentLike> findAllByCommentIdOrderByCreatedAtDesc(@NotNull Integer commentId, Pageable pageable);
}
