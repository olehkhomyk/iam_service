package com.post_hub.iam_service.model.request.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostRequest implements Serializable {
	// Not blank works only with String and does not allow: null | "" | "   "
	@NotBlank(message = "Title cannot be empty")
	private String title;
	@NotBlank(message = "Content cannot be empty")
	private String content;
	// Not null allow: "" | "   " | 0
	@NotNull(message = "Likes cannot be empty")
	private Integer likes;
}
