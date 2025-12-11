package com.post_hub.iam_service.service;

import com.post_hub.iam_service.model.dto.Post.PostDTO;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;

public interface PostService {
	IamResponse<PostDTO> getById(@NotNull Integer id);
	IamResponse<ArrayList<PostDTO>> getAll();
}
