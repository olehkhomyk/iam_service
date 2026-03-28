package com.post_hub.iam_service.component;

import com.post_hub.iam_service.mapper.CommentLikeMapper;
import com.post_hub.iam_service.model.dto.comment.CommentDTO;
import com.post_hub.iam_service.model.dto.commentLike.CommentLikeDTO;
import com.post_hub.iam_service.repository.CommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentEnricher {
	private final CommentLikeRepository commentLikeRepository;
	private final CommentLikeMapper commentLikeMapper;

	public void enrichWithLikes(CommentDTO commentDTO, int quantity) {
		Page<CommentLikeDTO> likes = commentLikeRepository
				.findAllByCommentIdOrderByCreatedAtDesc(
						commentDTO.getId(),
						PageRequest.of(0, quantity)
				)
				.map(commentLikeMapper::toCommentLikeDTO);

		commentDTO.setLikes(likes.getContent());
		commentDTO.setLikesCount(likes.getTotalElements());
	}

	public void enrichWithLikes(List<CommentDTO> comments, int quantity) {
		comments.forEach(c -> enrichWithLikes(c, quantity));
	}
}
