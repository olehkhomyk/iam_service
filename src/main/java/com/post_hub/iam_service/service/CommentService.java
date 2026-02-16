package com.post_hub.iam_service.service;

import com.post_hub.iam_service.model.dto.comment.CommentDTO;
import com.post_hub.iam_service.model.request.comment.CommentRequest;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;

public interface CommentService {
    IamResponse<CommentDTO> create(@NotNull Integer postId, @NotNull CommentRequest request, String username);

    IamResponse<CommentDTO> getById(@NotNull Integer postId, @NotNull Integer id);

    IamResponse<ArrayList<CommentDTO>> getByPostId(@NotNull Integer postId);

    void deleteById(@NotNull Integer postId, @NotNull Integer id);
}
