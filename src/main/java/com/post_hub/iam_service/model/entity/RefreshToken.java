package com.post_hub.iam_service.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "refresh_tokens")
public class RefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Size(max = 128)
	@Column(nullable = false, length = 128, unique = true)
	private String token;

	@Column(nullable = false, updatable = false)
	private LocalDateTime created = LocalDateTime.now();

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private User user;
}