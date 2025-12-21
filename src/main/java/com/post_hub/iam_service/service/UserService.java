package com.post_hub.iam_service.service;

import com.post_hub.iam_service.model.dto.user.UserDTO;
import com.post_hub.iam_service.model.request.user.NewUserRequest;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import jakarta.validation.constraints.NotNull;

public interface UserService {
	IamResponse<UserDTO> getById(@NotNull Integer id);

	IamResponse<UserDTO> createUser(@NotNull NewUserRequest request);
}
