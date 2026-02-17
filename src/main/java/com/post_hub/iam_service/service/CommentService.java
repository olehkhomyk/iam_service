package com.post_hub.iam_service.service;

import com.post_hub.iam_service.model.dto.comment.CommentDTO;
import com.post_hub.iam_service.model.request.comment.CommentRequest;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import com.post_hub.iam_service.model.respsonse.PaginationResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

public interface CommentService {
	IamResponse<CommentDTO> create(@NotNull Integer postId, @NotNull CommentRequest request, String username);

	IamResponse<CommentDTO> getById(@NotNull Integer postId, @NotNull Integer id);

	IamResponse<ArrayList<CommentDTO>> getByPostId(@NotNull Integer postId);

	IamResponse<PaginationResponse<CommentDTO>> getAllByPostId(@NotNull Integer postId, Pageable pageable);

	void deleteById(@NotNull Integer postId, @NotNull Integer id);
}
