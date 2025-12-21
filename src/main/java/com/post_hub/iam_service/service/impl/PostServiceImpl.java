package com.post_hub.iam_service.service.impl;

import com.post_hub.iam_service.mapper.PostMapper;
import com.post_hub.iam_service.model.constants.ApiErrorMessage;
import com.post_hub.iam_service.model.dto.post.PostDTO;
import com.post_hub.iam_service.model.dto.post.PostSearchDTO;
import com.post_hub.iam_service.model.entities.Post;
import com.post_hub.iam_service.model.entities.User;
import com.post_hub.iam_service.model.exception.DataExistsException;
import com.post_hub.iam_service.model.exception.NotFoundException;
import com.post_hub.iam_service.model.request.post.PostRequest;
import com.post_hub.iam_service.model.request.post.PostSearchRequest;
import com.post_hub.iam_service.model.request.post.UpdatePostRequest;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import com.post_hub.iam_service.model.respsonse.PaginationResponse;
import com.post_hub.iam_service.repository.PostRepository;
import com.post_hub.iam_service.repository.UserRepository;
import com.post_hub.iam_service.repository.criteria.PostSearchCriteria;
import com.post_hub.iam_service.service.PostService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Primary
public class PostServiceImpl implements PostService {
	private final PostRepository postRepository;
	private final PostMapper postMapper;
	private final UserRepository userRepository;

	@Override
	public IamResponse<PostDTO> getById(@NotNull Integer id) {
		Post post = postRepository.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.POST_NOT_FOUND_BY_ID.getMessage(id)));

		PostDTO postDTO = postMapper.toPostDTO(post);

		return IamResponse.createSuccess(postDTO);
	}

	@Override
	public IamResponse<PostDTO> create(Integer userId, PostRequest postRequest) {
		if (postRepository.existsByTitle(postRequest.getTitle())) {
			throw new DataExistsException(ApiErrorMessage.POST_ALREADY_EXISTS.getMessage(postRequest.getTitle()));
		}

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(userId)));

		// Map postRequest to Post, to prepare for saving to db.
		Post post = postMapper.createPost(postRequest, user);
		// Saving Post to db.
		Post savedPost = postRepository.save(post);
		// Map post from db to PostDTO
		PostDTO savedPostDTO = postMapper.toPostDTO(savedPost);

		// Create adn return IamResponse with savedPost.
		return IamResponse.createSuccess(savedPostDTO);
	}

	@Override
	public IamResponse<PostDTO> update(@NotNull Integer id, @NotNull UpdatePostRequest postRequest) {
		Post post = postRepository.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.POST_NOT_FOUND_BY_ID.getMessage(id)));

		postMapper.updatePost(post, postRequest);
		post.setUpdated(LocalDateTime.now());

		if (postRepository.existsByTitle(post.getTitle())) {
			throw new DataExistsException(ApiErrorMessage.POST_ALREADY_EXISTS.getMessage(postRequest.getTitle()));
		}

		Post savedPost = postRepository.save(post);

		PostDTO savedPostDTO = postMapper.toPostDTO(savedPost);
		return IamResponse.createSuccess(savedPostDTO);
	}

	@Override
	public void sofDeletePost(@NotNull Integer id) {
		Post post = postRepository.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.POST_NOT_FOUND_BY_ID.getMessage(id)));

		post.setDeleted(true);
		postRepository.save(post);
	}

	@Override
	public IamResponse<PaginationResponse<PostSearchDTO>> findAllPosts(Pageable pageable) {
		Page<PostSearchDTO> posts = postRepository.findAll(pageable)
				.map(postMapper::toPostSearchDTO);

		PaginationResponse<PostSearchDTO> paginationResponse = new PaginationResponse<>(
				posts.getContent(),
				new PaginationResponse.Pagination(
						posts.getTotalElements(),
						pageable.getPageSize(),
						posts.getNumber() + 1,
						posts.getTotalPages()
				)
		);

		return IamResponse.createSuccess(paginationResponse);
	}

	@Override
	public IamResponse<PaginationResponse<PostSearchDTO>> searchPosts(PostSearchRequest request, Pageable pageable) {
		Specification<Post> specification = new PostSearchCriteria(request);
		Page<PostSearchDTO> posts = postRepository.findAll(specification, pageable)
				.map(postMapper::toPostSearchDTO);

		PaginationResponse<PostSearchDTO> response = PaginationResponse.<PostSearchDTO>builder()
				.content(posts.getContent())
				.pagination(
						PaginationResponse.Pagination.builder()
								.total(posts.getTotalElements())
								.limit(pageable.getPageSize())
								.page(posts.getNumber() + 1)
								.pages(posts.getTotalPages())
								.build()
				).build();

		return  IamResponse.createSuccess(response);
	}
}
