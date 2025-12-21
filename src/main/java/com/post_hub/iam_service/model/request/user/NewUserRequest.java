package com.post_hub.iam_service.model.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {

	@NotBlank(message = "username must not be blank")
	@Size(max = 30)
	private String username;

	@NotBlank(message = "password must not be blank")
	@Size(max = 50)
	private String password;

	@NotBlank(message = "email must not be blank")
	@Size(max = 50)
	private String email;
}
