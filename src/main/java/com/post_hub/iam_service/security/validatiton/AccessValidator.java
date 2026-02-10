package com.post_hub.iam_service.security.validatiton;

import com.post_hub.iam_service.model.constants.ApiErrorMessage;
import com.post_hub.iam_service.model.entity.User;
import com.post_hub.iam_service.model.exception.DataExistException;
import com.post_hub.iam_service.model.exception.InvalidPasswordException;
import com.post_hub.iam_service.model.exception.NotFoundException;
import com.post_hub.iam_service.repository.UserRepository;
import com.post_hub.iam_service.service.model.IamServiceUserRole;
import com.post_hub.iam_service.utils.ApiUtils;
import com.post_hub.iam_service.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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

	public boolean isAdminOrSuperAdmin(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.USERNAME_NOT_FOUND.getMessage()));

		return user.getRoles().stream()
				.map(role -> IamServiceUserRole.fromName(role.getName()))
				.anyMatch(role ->
						role == IamServiceUserRole.ADMIN ||
								role == IamServiceUserRole.SUPER_ADMIN);
	}

	public void validateAdminOrOwnerAccess(String username, String createdBy) {
		String currentUsername = ApiUtils.getCurrentUserName();

		if (!currentUsername.equals(username) &&
				!currentUsername.equals(createdBy) &&
				!isAdminOrSuperAdmin(currentUsername)
		) {
			throw new AccessDeniedException(ApiErrorMessage.HAVE_NO_ACCESS.getMessage());
		}
	}
}
