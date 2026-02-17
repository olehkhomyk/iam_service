package com.post_hub.iam_service.model.request.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest implements Serializable {
	@NotBlank(message = "Content cannot be empty")
	private String content;
}
