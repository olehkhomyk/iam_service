package com.post_hub.iam_service.model.entity;

import com.post_hub.iam_service.model.enums.RegistrationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
	public static final String ID_FIELD_NAME = "id";
	public static final String USERNAME_FIELD_NAME = "username";
	public static final String PASSWORD_FIELD_NAME = "password";
	public static final String EMAIL_FIELD_NAME = "email";
	public static final String FIRSTNAME_FIELD_NAME = "email";
	public static final String LASTNAME_FIELD_NAME = "email";
	public static final String REGISTRATION_STATUS_FIELD_NAME = "registrationStatus";
	public static final String CREATED_FIELD_NAME = "created";
	public static final String UPDATED_FIELD_NAME = "updated";
	public static final String DELETED_FIELD_NAME = "deleted";
	public static final String LAST_LOGIN_FIELD_NAME = "lastLogin";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Size(max = 30)
	@Column(nullable = false, length = 30, unique = true)
	private String username;

	@Size(max = 80)
	@Column(nullable = false, length = 80)
	private String password;

	@Size(max = 50)
	@Column(unique = true, length = 50)
	private String email;

	@Size(max = 50)
	@Column(name="first_name", nullable = false, length = 50)
	private String firstName;

	@Size(max = 50)
	@Column(name="last_name", nullable = false, length = 50)
	private String lastName;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime created = LocalDateTime.now();

	@UpdateTimestamp
	@Column(nullable = false)
	private LocalDateTime updated = LocalDateTime.now();

	@Enumerated(EnumType.STRING)
	@Column(name = "registration_status", nullable = false)
	private RegistrationStatus registrationStatus;

	@Column(name = "last_login")
	private LocalDateTime lastLogin;

	@Column(nullable = false)
	private boolean deleted = false;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Post> posts;

	@ManyToMany
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Collection<Role> roles;
}
