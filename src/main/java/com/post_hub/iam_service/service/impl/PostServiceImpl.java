package com.post_hub.iam_service.service.impl;

import com.post_hub.iam_service.model.constants.ApiErrorMessage;
import com.post_hub.iam_service.model.dto.Post.PostDTO;
import com.post_hub.iam_service.model.entities.Post;
import com.post_hub.iam_service.model.exception.NotFoundException;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import com.post_hub.iam_service.repository.PostRepository;
import com.post_hub.iam_service.service.PostService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class PostServiceImpl implements PostService {
	private final PostRepository postRepository;

	@Override
	public IamResponse<PostDTO> getById(@NotNull Integer id) {
		Post post = postRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.POST_NOT_FOUND_BY_ID.getMessage(id)));

		PostDTO postDTO = PostDTO.builder()
				.id(post.getId())
				.title(post.getTitle())
				.content(post.getContent())
				.likes(post.getLikes())
				.created(post.getCreated())
				.build();

		return IamResponse.createSuccess(postDTO);
	}
}
