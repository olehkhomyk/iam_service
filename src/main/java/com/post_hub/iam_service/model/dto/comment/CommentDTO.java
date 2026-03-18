package com.post_hub.iam_service.model.dto.comment;

import com.post_hub.iam_service.model.dto.commentLike.CommentLikeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO implements Serializable {
	private Integer id;
	private String content;
	private List<CommentLikeDTO> likes;
	private Long likesCount;
	private String createdAt;
	private String createdBy;
}
