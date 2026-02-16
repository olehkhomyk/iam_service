package com.post_hub.iam_service.service.impl;

import com.post_hub.iam_service.mapper.CommentMapper;
import com.post_hub.iam_service.model.constants.ApiErrorMessage;
import com.post_hub.iam_service.model.dto.comment.CommentDTO;
import com.post_hub.iam_service.model.entity.Comment;
import com.post_hub.iam_service.model.entity.Post;
import com.post_hub.iam_service.model.entity.User;
import com.post_hub.iam_service.model.exception.NotFoundException;
import com.post_hub.iam_service.model.request.comment.CommentRequest;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import com.post_hub.iam_service.model.respsonse.PaginationResponse;
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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final CommentMapper commentMapper;
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

		return IamResponse.createSuccess(commentDTO);
	}

	@Override
	public IamResponse<CommentDTO> getById(@NotNull Integer postId, @NotNull Integer commentId) {
		Comment comment = commentRepository.findByIdAndPostId(commentId.longValue(), postId)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.COMMENT_NOT_FOUND_BY_ID.getMessage(commentId)));

		CommentDTO commentDTO = commentMapper.toCommentDTO(comment);

		return IamResponse.createSuccess(commentDTO);
	}

	@Override
	public IamResponse<ArrayList<CommentDTO>> getByPostId(@NotNull Integer postId) {
		if (!postRepository.existsById(postId)) {
			throw new NotFoundException(ApiErrorMessage.POST_NOT_FOUND_BY_ID.getMessage(postId));
		}

		ArrayList<CommentDTO> commentDTOs = new ArrayList<>(commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId)
				.stream()
				.map(commentMapper::toCommentDTO)
				.toList());

		return IamResponse.createSuccess(commentDTOs);
	}

	@Override
	public IamResponse<PaginationResponse<CommentDTO>> getAllByPostId(Integer postId, Pageable pageable) {
		if (!postRepository.existsById(postId)) {
			throw new NotFoundException(ApiErrorMessage.POST_NOT_FOUND_BY_ID.getMessage(postId));
		}

		Page<CommentDTO> comments = commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId, pageable)
				.map(commentMapper::toCommentDTO);

		PaginationResponse<CommentDTO> paginationResponse = new PaginationResponse<>(
				comments.getContent(),
				new PaginationResponse.Pagination(
						comments.getTotalElements(),
						pageable.getPageSize(),
						comments.getNumber() + 1,
						comments.getTotalPages()
				)
		);

		return IamResponse.createSuccess(paginationResponse);
	}

	@Override
	public void deleteById(@NotNull Integer postId, @NotNull Integer commentId) {
		Comment comment = commentRepository.findByIdAndPostId(commentId.longValue(), postId)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.COMMENT_NOT_FOUND_BY_ID.getMessage(commentId)));

		accessValidator.validateAdminOrOwnerAccess(comment.getUser().getId());

		commentRepository.deleteById(commentId.longValue());
	}
}
