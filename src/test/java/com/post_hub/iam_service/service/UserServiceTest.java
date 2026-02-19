package com.post_hub.iam_service.service;

import com.post_hub.iam_service.mapper.UserMapper;
import com.post_hub.iam_service.model.dto.role.RoleDto;
import com.post_hub.iam_service.model.dto.user.UserDTO;
import com.post_hub.iam_service.model.entity.Role;
import com.post_hub.iam_service.model.entity.User;
import com.post_hub.iam_service.repository.RoleRepository;
import com.post_hub.iam_service.repository.UserRepository;
import com.post_hub.iam_service.service.impl.UserServiceImpl;
import com.post_hub.iam_service.service.model.IamServiceUserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class UserServiceTest {
	@Mock
	private UserRepository userRepository;

	@Mock
	private UserMapper userMapper;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private RoleRepository roleRepository;

	@InjectMocks
	private UserServiceImpl userService;

	private User testUser;
	private UserDTO testUserDTO;
	private Role testRole;

	@BeforeEach
	void setUp() {
		Role superAdminRole = new Role();
		superAdminRole.setName(IamServiceUserRole.SUPER_ADMIN.getRole());

		testUser = new User();
		testUser.setId(1);
		testUser.setUsername("testUser");
		testUser.setEmail("testuser@gmail.com");
		testUser.setPassword("testPassword");
		testUser.setRoles(Set.of(superAdminRole));

		testUserDTO = new UserDTO();
		testUserDTO.setId(1);
		testUserDTO.setUsername("testUser");
		testUserDTO.setEmail("testuser@gmail.com");
	}

	@Test
	void getById_UserExists_ReturnUserDTO() {
		// Setting the mock behavior, what the mock should return when a specific method is called.
		when(userRepository.findByIdAndDeletedFalse(1)).thenReturn(Optional.of(testUser));
		when(userMapper.toDTO(testUser)).thenReturn(testUserDTO);

		UserDTO resultUserDTO = userService.getById(1).getPayload();

		assertNotNull(resultUserDTO);
		assertEquals(testUserDTO.getId(), resultUserDTO.getId());
		assertEquals(testUserDTO.getUsername(), resultUserDTO.getUsername());

		InOrder inOrder = inOrder(userRepository, userMapper);
		inOrder.verify(userRepository, times(1)).findByIdAndDeletedFalse(1);
		inOrder.verify(userMapper, times(1)).toDTO(testUser);
	}
}
