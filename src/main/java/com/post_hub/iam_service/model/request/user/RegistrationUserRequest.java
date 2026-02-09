package com.post_hub.iam_service.model.request.user;

import com.post_hub.iam_service.utils.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
@PasswordMatches
public class RegistrationUserRequest implements Serializable {

	@NotBlank
	private String username;

	@Email
	@NotBlank
	private String email;

	@NotBlank
	private String password;

	@NotBlank
	private String confirmPassword;
}
