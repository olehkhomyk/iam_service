package com.post_hub.iam_service.model.request.post;

import com.post_hub.iam_service.model.enums.PostSortField;
import lombok.Data;

@Data
public class PostSearchRequest {
	private String title;
	private String content;
	private Number likes;

	private Boolean deleted;
	private String keyword;
	private PostSortField sortField;
}
