package com.post_hub.iam_service.service.impl;

import com.post_hub.iam_service.mapper.CommentMapper;
import com.post_hub.iam_service.model.constants.ApiErrorMessage;
import com.post_hub.iam_service.model.dto.comment.CommentDTO;
import com.post_hub.iam_service.model.entity.Comment;
import com.post_hub.iam_service.model.exception.NotFoundException;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import com.post_hub.iam_service.repository.CommentRepository;
import com.post_hub.iam_service.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public IamResponse<CommentDTO> getById(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.COMMENT_NOT_FOUND_BY_ID.getMessage(commentId)));

        CommentDTO commentDTO = commentMapper.toCommentDTO(comment);

        return IamResponse.createSuccess(commentDTO);
    }

    @Override
    public void deleteById(Integer id) {

    }
}
