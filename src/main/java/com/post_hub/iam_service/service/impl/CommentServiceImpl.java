package com.post_hub.iam_service.service.impl;

import com.post_hub.iam_service.component.CommentEnricher;
import com.post_hub.iam_service.mapper.CommentLikeMapper;
import com.post_hub.iam_service.mapper.CommentMapper;
import com.post_hub.iam_service.model.constants.ApiErrorMessage;
import com.post_hub.iam_service.model.dto.comment.CommentDTO;
import com.post_hub.iam_service.model.dto.commentLike.CommentLikeDTO;
import com.post_hub.iam_service.model.entity.*;
import com.post_hub.iam_service.model.exception.DataExistException;
import com.post_hub.iam_service.model.exception.NotFoundException;
import com.post_hub.iam_service.model.request.comment.CommentRequest;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import com.post_hub.iam_service.model.respsonse.PaginationResponse;
import com.post_hub.iam_service.repository.CommentLikeRepository;
import com.post_hub.iam_service.repository.CommentRepository;
import com.post_hub.iam_service.repository.PostRepository;
import com.post_hub.iam_service.repository.UserRepository;
import com.post_hub.iam_service.security.validatiton.AccessValidator;
import com.post_hub.iam_service.service.CommentService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final CommentLikeRepository commentLikeRepository;
	private final CommentMapper commentMapper;
	private final CommentLikeMapper commentLikeMapper;
	private final CommentEnricher commentEnricher;
	private final AccessValidator accessValidator;

	@Override
	public IamResponse<CommentDTO> create(@NotNull Integer postId, @NotNull CommentRequest commentRequest, String username) {
		Post post = postRepository.findByIdAndDeletedFalse(postId)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.POST_NOT_FOUND_BY_ID.getMessage(postId)));

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.USERNAME_NOT_FOUND.getMessage(username)));

		Comment comment = commentMapper.createComment(commentRequest);

		comment.setPost(post);
		comment.setUser(user);
		comment.setCreatedBy(username);

		Comment savedComment = commentRepository.save(comment);
		CommentDTO commentDTO = commentMapper.toCommentDTO(savedComment);

		commentDTO.setLikesCount(0L);
		commentDTO.setLikes(new ArrayList<>());

		return IamResponse.createSuccess(commentDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public IamResponse<CommentDTO> getById(@NotNull Integer postId, @NotNull Integer commentId) {
		Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.COMMENT_NOT_FOUND_BY_ID.getMessage(commentId)));

		CommentDTO commentDTO = commentMapper.toCommentDTO(comment);
		commentEnricher.enrichWithLikes(commentDTO, 10);

		return IamResponse.createSuccess(commentDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public IamResponse<ArrayList<CommentDTO>> getByPostId(@NotNull Integer postId) {
		validatePostExistence(postId);

		ArrayList<CommentDTO> commentDTOs = new ArrayList<>(commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId)
				.stream()
				.map(commentMapper::toCommentDTO)
				.toList());

		commentEnricher.enrichWithLikes(commentDTOs, 3);

		return IamResponse.createSuccess(commentDTOs);
	}

	@Override
	@Transactional(readOnly = true)
	public IamResponse<PaginationResponse<CommentDTO>> getAllByPostId(@NotNull Integer postId, @NotNull Pageable pageable) {
		validatePostExistence(postId);

		Page<CommentDTO> comments = commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId, pageable)
				.map(commentMapper::toCommentDTO);
		commentEnricher.enrichWithLikes(comments.getContent(), 3);

		PaginationResponse<CommentDTO> paginationResponse = buildCommetsPaginationResponse(comments, pageable);

		return IamResponse.createSuccess(paginationResponse);
	}

	@Override
	public void deleteById(@NotNull Integer postId, @NotNull Integer commentId) {
		Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.COMMENT_NOT_FOUND_BY_ID.getMessage(commentId)));

		accessValidator.validateAdminOrOwnerAccess(comment.getUser().getId());

		commentRepository.deleteById(commentId);
	}

	@Override
	@Transactional
	public void likeComment(@NotNull Integer commentId, @NotNull Integer userId) {
		if (commentLikeRepository.existsByCommentIdAndUserId(commentId, userId)) {
			throw new DataExistException(ApiErrorMessage.COMMENT_NOT_FOUND_BY_ID.getMessage(commentId));
		}

		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.COMMENT_NOT_FOUND_BY_ID.getMessage(commentId)));
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(userId)));

		CommentLike like = commentLikeMapper.createCommentLike(comment, user);
		commentLikeRepository.save(like);
	}

	@Override
	@Transactional
	public void unlikeComment(@NotNull Integer commentId, @NotNull Integer userId) {
		if (!commentLikeRepository.existsByCommentIdAndUserId(commentId, userId)) {
			throw new NotFoundException(ApiErrorMessage.COMMENT_NOT_FOUND_BY_ID.getMessage(commentId));
		}

		commentLikeRepository.deleteByCommentIdAndUserId(commentId, userId);
	}

	private PaginationResponse<CommentDTO> buildCommetsPaginationResponse(Page<CommentDTO> commentDTOS, Pageable pageable) {
		return new PaginationResponse<>(
				commentDTOS.getContent(),
				new PaginationResponse.Pagination(
						commentDTOS.getTotalElements(),
						pageable.getPageSize(),
						commentDTOS.getNumber() + 1,
						commentDTOS.getTotalPages()
				)
		);
	}

	private void validatePostExistence(Integer postId) {
		if (!postRepository.existsById(postId)) {
			throw new NotFoundException(ApiErrorMessage.POST_NOT_FOUND_BY_ID.getMessage(postId));
		}
	}
}
