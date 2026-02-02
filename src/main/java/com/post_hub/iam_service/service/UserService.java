package com.post_hub.iam_service.service;

import com.post_hub.iam_service.model.dto.user.UserDTO;
import com.post_hub.iam_service.model.dto.user.UserSearchDTO;
import com.post_hub.iam_service.model.request.user.NewUserRequest;
import com.post_hub.iam_service.model.request.user.UpdateUserRequest;
import com.post_hub.iam_service.model.request.user.UserSearchRequest;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import com.post_hub.iam_service.model.respsonse.PaginationResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
	IamResponse<UserDTO> getById(@NotNull Integer id);

	IamResponse<UserDTO> createUser(@NotNull NewUserRequest request);

	IamResponse<UserDTO> updateUserById(@NotNull Integer id, @NotNull UpdateUserRequest request);

	void softDeleteUserById(@NotNull Integer id);

	void hardDeleteUserById(@NotNull Integer id);

	IamResponse<PaginationResponse<UserSearchDTO>> searchUsers(@NotNull UserSearchRequest request, Pageable pageable);

	IamResponse<PaginationResponse<UserSearchDTO>> findAllUsers(Pageable pageable);
}
