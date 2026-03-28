package com.post_hub.iam_service.service.impl;

import com.post_hub.iam_service.component.CommentEnricher;
import com.post_hub.iam_service.mapper.CommentMapper;
import com.post_hub.iam_service.mapper.PostLikeMapper;
import com.post_hub.iam_service.mapper.PostMapper;
import com.post_hub.iam_service.model.constants.ApiErrorMessage;
import com.post_hub.iam_service.model.dto.comment.CommentDTO;
import com.post_hub.iam_service.model.dto.post.PostDTO;
import com.post_hub.iam_service.model.dto.post.PostSearchDTO;
import com.post_hub.iam_service.model.dto.postLike.PostLikeDTO;
import com.post_hub.iam_service.model.entity.Post;
import com.post_hub.iam_service.model.entity.PostLike;
import com.post_hub.iam_service.model.entity.User;
import com.post_hub.iam_service.model.exception.DataExistException;
import com.post_hub.iam_service.model.exception.NotFoundException;
import com.post_hub.iam_service.model.request.post.PostRequest;
import com.post_hub.iam_service.model.request.post.PostSearchRequest;
import com.post_hub.iam_service.model.request.post.UpdatePostRequest;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import com.post_hub.iam_service.model.respsonse.PaginationResponse;
import com.post_hub.iam_service.repository.*;
import com.post_hub.iam_service.repository.criteria.PostSearchCriteria;
import com.post_hub.iam_service.security.validatiton.AccessValidator;
import com.post_hub.iam_service.service.PostService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Primary
public class PostServiceImpl implements PostService {
	private final PostRepository postRepository;
	private final PostMapper postMapper;
	private final PostLikeMapper postLikeMapper;
	private final CommentMapper commentMapper;
	private final UserRepository userRepository;
	private final CommentRepository commentRepository;
	private final PostLikeRepository postLikeRepository;
	private final CommentEnricher commentEnricher;
	private final AccessValidator accessValidator;

	// TODO: enrich with comments and likes.
	@Override
	public IamResponse<PostDTO> getById(@NotNull Integer id) {
		Post post = postRepository.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.POST_NOT_FOUND_BY_ID.getMessage(id)));

		PostDTO postDTO = postMapper.toPostDTO(post);

		return IamResponse.createSuccess(postDTO);
	}

	@Override
	public IamResponse<PostDTO> create(@NotNull PostRequest postRequest, Integer userId) {
		if (postRepository.existsByTitle(postRequest.getTitle())) {
			throw new DataExistException(ApiErrorMessage.POST_ALREADY_EXISTS.getMessage(postRequest.getTitle()));
		}

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(userId)));

		Post post = postMapper.createPost(postRequest);
		post.setUser(user);
		post.setCreatedBy(user.getUsername());
		Post savedPost = postRepository.save(post);
		PostDTO savedPostDTO = postMapper.toPostDTO(savedPost);

		return IamResponse.createSuccess(savedPostDTO);
	}

	// TODO: enrich with comments and likes.
	@Override
	public IamResponse<PostDTO> update(@NotNull Integer id, @NotNull UpdatePostRequest postRequest) {
		Post post = postRepository.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.POST_NOT_FOUND_BY_ID.getMessage(id)));

		accessValidator.validateAdminOrOwnerAccess(post.getUser().getId());

		postMapper.updatePost(post, postRequest);
		post.setUpdated(LocalDateTime.now());
		Post savedPost = postRepository.save(post);
		PostDTO savedPostDTO = postMapper.toPostDTO(savedPost);

		return IamResponse.createSuccess(savedPostDTO);
	}

	@Override
	public void sofDeletePost(@NotNull Integer id) {
		Post post = postRepository.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.POST_NOT_FOUND_BY_ID.getMessage(id)));

		accessValidator.validateAdminOrOwnerAccess(post.getUser().getId());

		post.setDeleted(true);
		postRepository.save(post);
	}

	@Override
	public void hardDeletePost(Integer id) {
		if (!postRepository.existsById(id)) {
			throw new NotFoundException(ApiErrorMessage.POST_NOT_FOUND_BY_ID.getMessage(id));
		}

		postRepository.deleteById(id);
	}

	@Override
	public IamResponse<PaginationResponse<PostSearchDTO>> findAllPosts(Pageable pageable, Boolean includeComments) {
		Page<PostSearchDTO> postDTOs = postRepository.findAll(pageable)
				.map(postMapper::toPostSearchDTO);

		if (Boolean.TRUE.equals(includeComments)) {
			for (PostSearchDTO post : postDTOs.getContent()) {
				enrichPostWithComments(post, 3);
			}
		}

		for (PostSearchDTO post : postDTOs.getContent()) {
			enrichPostWithLikes(post);
		}

		PaginationResponse<PostSearchDTO> paginationResponse = buildPostsPaginationResponse(postDTOs, pageable);

		return IamResponse.createSuccess(paginationResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public IamResponse<PaginationResponse<PostSearchDTO>> searchPosts(PostSearchRequest request, Pageable pageable, Boolean includeComments) {
		Specification<Post> specification = new PostSearchCriteria(request);
		Page<PostSearchDTO> postDTOs = postRepository.findAll(specification, pageable)
				.map(postMapper::toPostSearchDTO);

		if (Boolean.TRUE.equals(includeComments)) {
			for (PostSearchDTO post : postDTOs.getContent()) {
				enrichPostWithComments(post, 3);
			}
		}

		for (PostSearchDTO post : postDTOs.getContent()) {
			enrichPostWithLikes(post);
		}

		PaginationResponse<PostSearchDTO> response = buildPostsPaginationResponse(postDTOs, pageable);

		return IamResponse.createSuccess(response);
	}

	@Override
	@Transactional
	public void likePost(@NotNull Integer postId, @NotNull Integer userId) {
		if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
			throw new DataExistException(ApiErrorMessage.POST_ALREADY_LIKED.getMessage(postId));
		}

		Post post = postRepository.findByIdAndDeletedFalse(postId)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.POST_NOT_FOUND_BY_ID.getMessage(postId)));
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(userId)));

		PostLike like = postLikeMapper.createPostLike(post, user);
		postLikeRepository.save(like);
	}

	@Override
	@Transactional
	public void unlikePost(@NotNull Integer postId, @NotNull Integer userId) {
		if (!postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
			throw new NotFoundException(ApiErrorMessage.POST_LIKE_NOT_FOUND.getMessage(postId));
		}

		postLikeRepository.deleteByPostIdAndUserId(postId, userId);
	}

	private void enrichPostWithLikes(PostSearchDTO post) {
		Page<PostLikeDTO> postLikes = postLikeRepository
				.findAllByPostIdOrderByCreatedAtDesc(post.getId(), PageRequest.of(0, 3))
				.map(postLikeMapper::toPostLikeDTO);

		post.setLikes(postLikes.getContent());
		post.setLikesCount(postLikes.getTotalElements());
	}

	private void enrichPostWithComments(PostSearchDTO post, Integer quantity) {
		Page<CommentDTO> comments = commentRepository
				.findAllByPostIdOrderByCreatedAtDesc(post.getId(), PageRequest.of(0, quantity))
				.map(commentMapper::toCommentDTO);

		post.setTotalComments(comments.getTotalElements());
		post.setPreviewComments(comments.getContent());

		commentEnricher.enrichWithLikes(comments.getContent(), 3);
	}

	private PaginationResponse<PostSearchDTO> buildPostsPaginationResponse(
			Page<PostSearchDTO> postDTOs,
			Pageable pageable
	) {
		return PaginationResponse.<PostSearchDTO>builder()
				.content(postDTOs.getContent())
				.pagination(
						PaginationResponse.Pagination.builder()
								.total(postDTOs.getTotalElements())
								.limit(pageable.getPageSize())
								.page(postDTOs.getNumber() + 1)
								.pages(postDTOs.getTotalPages())
								.build()
				).build();
	}
}
