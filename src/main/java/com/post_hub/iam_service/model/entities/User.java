package com.post_hub.iam_service.model.entities;

import com.post_hub.iam_service.model.enums.RegistrationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
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
}
