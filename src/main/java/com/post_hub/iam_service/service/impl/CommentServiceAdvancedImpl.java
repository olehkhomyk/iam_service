package com.post_hub.iam_service.service.impl;

import com.post_hub.iam_service.service.CommentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service("advancedCommentService")
public class CommentServiceAdvancedImpl implements CommentService {
	private final List<String> comments = new ArrayList<>();

	@Override
	public void createComment(String commentContent) {
		comments.add(commentContent);
		System.out.println("\n Advanced create called \n");

		System.out.println("Comment Created: " + "\nDate: " + LocalDateTime.now() + " \nComment: " + commentContent);
	}
}
