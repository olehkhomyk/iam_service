package com.post_hub.iam_service.repository;

import com.post_hub.iam_service.model.entity.PostLike;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
	List<PostLike> findAllByPostIdOrderByCreatedAtDesc(@NotNull Integer postId);
	Page<PostLike> findAllByPostIdOrderByCreatedAtDesc(@NotNull Integer postId, Pageable pageable);
	Boolean existsByPostIdAndUserId(@NotNull Integer postId, @NotNull Integer userId);
	void deleteByPostIdAndUserId(@NotNull Integer postId, @NotNull Integer userId);
}
