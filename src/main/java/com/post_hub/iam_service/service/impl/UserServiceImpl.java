package com.post_hub.iam_service.service.impl;

import com.post_hub.iam_service.mapper.UserMapper;
import com.post_hub.iam_service.model.constants.ApiErrorMessage;
import com.post_hub.iam_service.model.dto.user.UserDTO;
import com.post_hub.iam_service.model.dto.user.UserSearchDTO;
import com.post_hub.iam_service.model.entity.Role;
import com.post_hub.iam_service.model.entity.User;
import com.post_hub.iam_service.model.exception.DataExistsException;
import com.post_hub.iam_service.model.exception.NotFoundException;
import com.post_hub.iam_service.model.request.user.NewUserRequest;
import com.post_hub.iam_service.model.request.user.UpdateUserRequest;
import com.post_hub.iam_service.model.request.user.UserSearchRequest;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import com.post_hub.iam_service.model.respsonse.PaginationResponse;
import com.post_hub.iam_service.repository.RoleRepository;
import com.post_hub.iam_service.repository.UserRepository;
import com.post_hub.iam_service.repository.criteria.UserSearchCriteria;
import com.post_hub.iam_service.service.UserService;
import com.post_hub.iam_service.service.model.IamServiceUserRole;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final RoleRepository roleRepository;

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

		Role userRole = roleRepository.findByName(IamServiceUserRole.USER.getRole())
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.ROLE_NOT_FOUND.getMessage(IamServiceUserRole.USER.getRole())));

		User user = userMapper.createUser(request);
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		Set<Role> roles = new HashSet<>();
		roles.add(userRole);
		user.setRoles(roles);

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
		User user = userRepository.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(id)));

		user.setDeleted(true);
		userRepository.save(user);
	}

	@Override
	public void hardDeleteUserById(Integer id) {
		if (!userRepository.existsById(id)) {
			throw new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(id));
		}

		userRepository.deleteById(id);
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

	@Override
	public IamResponse<PaginationResponse<UserSearchDTO>> findAllUsers(Pageable pageable) {
		Page<UserSearchDTO> users = userRepository.findAll(pageable)
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

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return getUserDetails(email, userRepository);
	}

	static UserDetails getUserDetails(String email, UserRepository userRepository) {
		User user = userRepository.findUserByEmail(email)
				.orElseThrow(() -> new NotFoundException(ApiErrorMessage.EMAIL_NOT_FOUND.getMessage()));

		user.setLastLogin(LocalDateTime.now());
		userRepository.save(user);

		return new org.springframework.security.core.userdetails.User(
				user.getEmail(),
				user.getPassword(),
				user.getRoles().stream()
						.map(role -> new SimpleGrantedAuthority(role.getName()))
						.collect(Collectors.toList())
		);
	}
}