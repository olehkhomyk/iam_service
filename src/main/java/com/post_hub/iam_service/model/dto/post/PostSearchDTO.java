package com.post_hub.iam_service.model.dto.post;

import com.post_hub.iam_service.model.dto.comment.CommentDTO;
import com.post_hub.iam_service.model.dto.postLike.PostLikeDTO;
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
	private LocalDateTime created;
	private LocalDateTime updated;
	private Boolean isDeleted;
	private String createdBy;

	private List<PostLikeDTO> likes;
	private Long likesCount;

	private List<CommentDTO> previewComments;
	private Long totalComments;
}
