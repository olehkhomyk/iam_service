package com.post_hub.iam_service.service.impl;

import com.post_hub.iam_service.mapper.UserMapper;
import com.post_hub.iam_service.model.constants.ApiErrorMessage;
import com.post_hub.iam_service.model.dto.user.UserDTO;
import com.post_hub.iam_service.model.dto.user.UserSearchDTO;
import com.post_hub.iam_service.model.entities.User;
import com.post_hub.iam_service.model.exception.DataExistsException;
import com.post_hub.iam_service.model.exception.NotFoundException;
import com.post_hub.iam_service.model.request.user.NewUserRequest;
import com.post_hub.iam_service.model.request.user.UpdateUserRequest;
import com.post_hub.iam_service.model.request.user.UserSearchRequest;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import com.post_hub.iam_service.model.respsonse.PaginationResponse;
import com.post_hub.iam_service.repository.UserRepository;
import com.post_hub.iam_service.repository.criteria.UserSearchCriteria;
import com.post_hub.iam_service.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final UserMapper userMapper;

	@Override
	public IamResponse<UserDTO> getById(@NotNull Integer userId) {
		User user = userRepository.findByIdAndDeletedFalse(userId)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(userId)));

		UserDTO userDTO = userMapper.toDTO(user);

		return IamResponse.createSuccess(userDTO);
	}

	@Override
	public IamResponse<UserDTO> createUser(@NotNull NewUserRequest request) {
		validateUserAlreadyExists(request);

		User user = userMapper.createUser(request);
		User savedUser = userRepository.save(user);
		UserDTO userDTO = userMapper.toDTO(savedUser);

		return IamResponse.createSuccess(userDTO);
	}

	@Override
	public IamResponse<UserDTO> updateUserById(@NotNull Integer id, @NotNull UpdateUserRequest request) {
		User user = userRepository.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(id)));

		userMapper.updateUser(user, request);
		user.setUpdated(LocalDateTime.now());
		User savedUser = userRepository.save(user);
		UserDTO savedUserDTO = userMapper.toDTO(savedUser);

		return IamResponse.createSuccess(savedUserDTO);
	}

	@Override
	public void softDeleteUserById(Integer id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(id)));

		user.setDeleted(true);
		userRepository.save(user);
	}

	@Override
	public IamResponse<PaginationResponse<UserSearchDTO>> searchUsers(@NotNull UserSearchRequest request, Pageable pageable) {
		Specification<User> specification = new UserSearchCriteria(request);
		Page<UserSearchDTO> users = userRepository.findAll(specification, pageable)
				.map(userMapper::toUserSearchDTO);

		PaginationResponse<UserSearchDTO> response = PaginationResponse.<UserSearchDTO>builder()
				.content(users.getContent())
				.pagination(
						PaginationResponse.Pagination.builder()
								.total(users.getTotalElements())
								.limit(pageable.getPageSize())
								.page(users.getNumber() + 1)
								.pages(users.getTotalPages())
								.build()
				).build();

		return IamResponse.createSuccess(response);
	}

	private void validateUserAlreadyExists(NewUserRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new DataExistsException(
					ApiErrorMessage.USER_EMAIL_ALREADY_EXISTS.getMessage(request.getEmail())
			);
		}
		if (userRepository.existsByUsername(request.getUsername())) {
			throw new DataExistsException(
					ApiErrorMessage.USERNAME_ALREADY_EXISTS.getMessage(request.getUsername())
			);
		}
	}
}