package com.post_hub.iam_service.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "posts")
public class Post {
	public static final String ID_FIELD_NAME = "id";
	public static final String TITLE_FIELD_NAME = "title";
	public static final String CONTENT_FIELD_NAME = "content";
	public static final String CREATED_FIELD_NAME = "created";
	public static final String UPDATED_FIELD_NAME = "updated";
	public static final String DELETED_FIELD_NAME = "deleted";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, length = 1000)
	private String content;

	@Column(nullable = false, updatable = false)
	private LocalDateTime created = LocalDateTime.now();

	@Column(nullable = false)
	private LocalDateTime updated = LocalDateTime.now();

	@Column(nullable = false)
	private Boolean deleted = false;

	@ManyToOne()
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "created_by")
	private String createdBy;
}
