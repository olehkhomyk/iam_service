package com.post_hub.iam_service.component;

import com.post_hub.iam_service.mapper.PostLikeMapper;
import com.post_hub.iam_service.model.dto.post.PostDTO;
import com.post_hub.iam_service.model.dto.post.PostSearchDTO;
import com.post_hub.iam_service.model.dto.postLike.PostLikeDTO;
import com.post_hub.iam_service.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostEnricher {
	private final PostLikeRepository postLikeRepository;
	private final PostLikeMapper postLikeMapper;

	public void enrichWithLikes(PostDTO postDTO, int quantity) {
		Page<PostLikeDTO> likes = postLikeRepository
				.findAllByPostIdOrderByCreatedAtDesc(
						postDTO.getId(),
						PageRequest.of(0, quantity)
				)
				.map(postLikeMapper::toPostLikeDTO);

		postDTO.setLikes(Math.toIntExact(likes.getTotalElements()));
	}

	public void enrichWithLikes(List<PostDTO> posts, int quantity) {
		posts.forEach(post -> enrichWithLikes(post, quantity));
	}

	public void enrichWithLikes(PostSearchDTO postDTO, int quantity) {
		Page<PostLikeDTO> likes = postLikeRepository
				.findAllByPostIdOrderByCreatedAtDesc(
						postDTO.getId(),
						PageRequest.of(0, quantity)
				)
				.map(postLikeMapper::toPostLikeDTO);

		postDTO.setLikes(likes.getContent());
		postDTO.setLikesCount(likes.getTotalElements());
	}

	public void enrichSearchWithLikes(List<PostSearchDTO> posts, int quantity) {
		posts.forEach(post -> enrichWithLikes(post, quantity));
	}
}
