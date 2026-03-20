package com.post_hub.iam_service.model.request.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest implements Serializable {
	// Not blank works only with String and does not allow: null | "" | "   "
	@NotBlank(message = "Title cannot be empty")
	@Size(max = 255, message = "Title must not exceed 255 characters")
	private String title;
	@NotBlank(message = "Content cannot be empty")
	@Size(max = 1000, message = "Content must not exceed 1000 characters")
	private String content;
}
