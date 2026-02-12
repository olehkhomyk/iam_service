package com.post_hub.iam_service.service;

import com.post_hub.iam_service.model.dto.comment.CommentDTO;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import jakarta.validation.constraints.NotNull;

public interface CommentService {
    IamResponse<CommentDTO> getById(@NotNull Integer id);

    void deleteById(@NotNull Integer id);
}
