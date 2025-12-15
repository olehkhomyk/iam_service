package com.post_hub.iam_service.repository;

import com.post_hub.iam_service.model.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {

	boolean existsByTitle(String title);

	Optional<Post> findByIdAndDeletedFalse(Integer id);
}
