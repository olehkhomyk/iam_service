package com.post_hub.iam_service.model.dto.user;

import com.post_hub.iam_service.model.enums.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchDTO {
	private String id;
	private String username;
	private String email;
	private RegistrationStatus registrationStatus;
	private LocalDateTime created;
	private Boolean isDeleted;
	private LocalDateTime lastLogin;
}
