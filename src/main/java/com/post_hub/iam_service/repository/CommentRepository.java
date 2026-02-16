package com.post_hub.iam_service.repository;

import com.post_hub.iam_service.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findAllByPostIdOrderByCreatedAtDesc(Integer postId);

	Optional<Comment> findByIdAndPostId(Long id, Integer postId);
}
