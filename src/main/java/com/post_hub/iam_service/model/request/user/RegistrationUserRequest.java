package com.post_hub.iam_service.model.request.user;

import com.post_hub.iam_service.utils.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
@PasswordMatches
public class RegistrationUserRequest implements Serializable {

	@NotBlank(message = "Username must not be blank!")
	private String username;

	@Email
	@NotBlank(message = "Email must not be blank!")
	private String email;

	@NotBlank(message = "Password must not be blank!")
	private String password;

	@NotBlank(message = "Confirm password must not be blank!")
	private String confirmPassword;
}
