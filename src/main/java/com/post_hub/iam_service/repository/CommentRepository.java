package com.post_hub.iam_service.repository;

import com.post_hub.iam_service.model.entity.Comment;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findAllByPostIdOrderByCreatedAtDesc(@NotNull Integer postId);

	Page<Comment> findAllByPostIdOrderByCreatedAtDesc(@NotNull Integer postId, Pageable pageable);

//	Page<Comment> findAllByPostIdOrderByLikesDesc(@NotNull Integer postId, Pageable pageable);

	Optional<Comment> findByIdAndPostId(@NotNull Integer id, @NotNull Integer postId);

	@Query("""
			    SELECT c.post.id, COUNT(c)
			    FROM Comment c
			    WHERE c.post.id IN :postIds
			    GROUP BY c.post.id
			""")
	List<Object[]> countByPostIds(@Param("postIds") List<Integer> postIds);
}
