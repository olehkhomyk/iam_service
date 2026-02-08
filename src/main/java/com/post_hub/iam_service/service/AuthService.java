package com.post_hub.iam_service.service;

import com.post_hub.iam_service.model.dto.user.LoginRequest;
import com.post_hub.iam_service.model.dto.user.UserProfileDTO;
import com.post_hub.iam_service.model.respsonse.IamResponse;

public interface AuthService {

	IamResponse<UserProfileDTO> login(LoginRequest loginRequest);

	IamResponse<UserProfileDTO> refreshAccessToken(String refreshToken);
}
