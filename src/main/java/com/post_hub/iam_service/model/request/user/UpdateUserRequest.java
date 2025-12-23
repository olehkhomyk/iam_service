package com.post_hub.iam_service.model.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
	@NotBlank(message = "Username must not be blank")
	@Size(max = 10, message = "Username length must be between {min} and {max} characters")
	private String username;

	@NotBlank(message = "Password must not be blank")
	@Size(max = 50, message = "password length must be between {min} and {max} characters")
	private String password;

	@NotBlank(message = "Email must not be blank")
	@Size(max = 50, message = "email length must be between {min} and {max} characters")
	private String email;
}
