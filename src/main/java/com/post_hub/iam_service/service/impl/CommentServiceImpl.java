package com.post_hub.iam_service.service.impl;

import com.post_hub.iam_service.mapper.CommentMapper;
import com.post_hub.iam_service.model.constants.ApiErrorMessage;
import com.post_hub.iam_service.model.dto.comment.CommentDTO;
import com.post_hub.iam_service.model.entity.Comment;
import com.post_hub.iam_service.model.exception.NotFoundException;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import com.post_hub.iam_service.repository.CommentRepository;
import com.post_hub.iam_service.security.validatiton.AccessValidator;
import com.post_hub.iam_service.service.CommentService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AccessValidator accessValidator;

    @Override
    public IamResponse<CommentDTO> getById(@NotNull Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.COMMENT_NOT_FOUND_BY_ID.getMessage(commentId)));

        CommentDTO commentDTO = commentMapper.toCommentDTO(comment);

        return IamResponse.createSuccess(commentDTO);
    }

    @Override
    public IamResponse<ArrayList<CommentDTO>> getByPostId(@NotNull Integer postId) {
        return null;
    }

    @Override
    public void deleteById(@NotNull Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.COMMENT_NOT_FOUND_BY_ID.getMessage(commentId)));

        accessValidator.validateAdminOrOwnerAccess(comment.getUser().getId());

        commentRepository.deleteById(commentId);
    }
}
