package com.post_hub.iam_service.model.request.user;

import com.post_hub.iam_service.model.enums.RegistrationStatus;
import com.post_hub.iam_service.model.enums.UserSortField;
import lombok.Data;

@Data
public class UserSearchRequest {
	private String username;
	private String email;
	private RegistrationStatus registrationStatus;

	private Boolean deleted;
	private String keyword;
	private UserSortField sortField;
}
