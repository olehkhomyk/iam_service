package com.post_hub.iam_service.security.validatiton;

import com.post_hub.iam_service.model.constants.ApiErrorMessage;
import com.post_hub.iam_service.model.exception.DataExistException;
import com.post_hub.iam_service.model.exception.InvalidPasswordException;
import com.post_hub.iam_service.repository.UserRepository;
import com.post_hub.iam_service.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessValidator {
	private final UserRepository userRepository;

	public void validateNewUser(String username, String email, String password) {
		userRepository.findByUsername(username)
				.ifPresent(existingUser -> {
					throw new DataExistException(ApiErrorMessage.USERNAME_ALREADY_EXISTS.getMessage(username));
				});

		userRepository.findByEmail(email)
				.ifPresent(existingEmail -> {
					throw new DataExistException(ApiErrorMessage.USER_EMAIL_ALREADY_EXISTS.getMessage(email));
				});

		if (PasswordUtils.isNotValidPassword(password)) {
			throw new InvalidPasswordException(ApiErrorMessage.INVALID_PASSWORD.getMessage());
		}
	}
}
