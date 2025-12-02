package com.post_hub.iam_service.controller;

import com.post_hub.iam_service.service.CommentService;
import com.post_hub.iam_service.service.impl.CommentServiceAdvancedImpl;
import com.post_hub.iam_service.service.impl.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/comments")
public class CommentController {
	private CommentService commentService;

	@Autowired
	public void setCommentService(CommentServiceImpl commentService) {
		this.commentService = commentService;
	}

	@PostMapping("/create")
	public ResponseEntity<String> addComment(@RequestBody Map<String, Object> requestBody) {
		String content = (String) requestBody.get("content");
		commentService.createComment(content);

		System.out.println("\n Simple create called \n");

		return new ResponseEntity<>("Comment added: " + content, HttpStatus.OK);
	}

	@PostMapping("/advanced/create")
	public ResponseEntity<String> addCommentAdvanced(@RequestBody Map<String, Object> requestBody) {
		commentService = new CommentServiceAdvancedImpl();

		String content = (String) requestBody.get("content");
		commentService.createComment(content);

		System.out.println("\n Advanced create called \n");

		return new ResponseEntity<>("Switched to advanced, comment added: " + content, HttpStatus.OK);
	}
}
