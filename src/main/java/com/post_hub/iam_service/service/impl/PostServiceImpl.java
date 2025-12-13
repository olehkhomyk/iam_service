package com.post_hub.iam_service.service.impl;

import com.post_hub.iam_service.mapper.PostMapper;
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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Primary
public class PostServiceImpl implements PostService {
	private final PostRepository postRepository;
	private final PostMapper postMapper;

	@Override
	public IamResponse<PostDTO> getById(@NotNull Integer id) {
		Post post = postRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.POST_NOT_FOUND_BY_ID.getMessage(id)));

		PostDTO postDTO = postMapper.toPostDTO(post);

		return IamResponse.createSuccess(postDTO);
	}

	@Override
	public IamResponse<ArrayList<PostDTO>> getAll() {
		List<Post> posts = postRepository.findAll();

		List<PostDTO> postDTOs = posts
				.stream()
				.map(postMapper::toPostDTO)
				.toList();

		ArrayList<PostDTO> postDTOsArrayList = new ArrayList<>(postDTOs);

		return IamResponse.createSuccess(postDTOsArrayList);
	}
}
