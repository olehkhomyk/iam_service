package com.post_hub.iam_service.service;

import com.post_hub.iam_service.model.dto.Post.PostDTO;
import com.post_hub.iam_service.model.request.post.PostRequest;
import com.post_hub.iam_service.model.request.post.UpdatePostRequest;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;

public interface PostService {
	IamResponse<ArrayList<PostDTO>> getAll();

	IamResponse<PostDTO> getById(@NotNull Integer id);

	IamResponse<PostDTO> create(@NotNull PostRequest postRequest);

	IamResponse<PostDTO> update(@NotNull Integer id, @NotNull UpdatePostRequest updatePostRequest);
}
