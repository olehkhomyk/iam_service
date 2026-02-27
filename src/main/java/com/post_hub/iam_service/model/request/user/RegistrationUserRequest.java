package com.post_hub.iam_service.model.request.user;

import com.post_hub.iam_service.utils.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
@PasswordMatches
public class RegistrationUserRequest implements Serializable {

	@NotBlank(message = "Username must not be blank!")
	@Size(max = 50)
	private String username;

	@NotBlank(message = "First name must not be blank!")
	@Size(max = 50)
	private String firstName;

	@NotBlank(message = "Last name must not be blank!")
	@Size(max = 50)
	private String lastName;

	@Email
	@NotBlank(message = "Email must not be blank!")
	@Size(max = 50)
	private String email;

	@NotBlank(message = "Password must not be blank!")
	private String password;

	@NotBlank(message = "Confirm password must not be blank!")
	private String confirmPassword;
}
