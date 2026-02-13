package com.post_hub.iam_service.service;

import com.post_hub.iam_service.model.dto.comment.CommentDTO;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;

public interface CommentService {
    IamResponse<CommentDTO> getById(@NotNull Integer id);

    IamResponse<ArrayList<CommentDTO>> getByPostId(@NotNull Integer postId);

    void deleteById(@NotNull Integer id);
}
