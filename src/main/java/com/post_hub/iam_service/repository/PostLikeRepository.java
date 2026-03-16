package com.post_hub.iam_service.repository;

import com.post_hub.iam_service.model.entity.PostLike;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
	Page<PostLike> findAllByPostIdOrderByCreatedAtDesc(@NotNull Integer postId, Pageable pageable);
}
