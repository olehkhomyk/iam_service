package com.post_hub.iam_service.model.dto.post;

import com.post_hub.iam_service.model.dto.comment.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostSearchDTO implements Serializable {
	private Integer id;
	private String title;
	private String content;
	private Integer likes;
	private LocalDateTime created;
	private LocalDateTime updated;
	private Boolean isDeleted;
	private String createdBy;

	private List<CommentDTO> previewComments;
	private Long totalComments;
}
