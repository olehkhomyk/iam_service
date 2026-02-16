package com.post_hub.iam_service.controller;

import com.post_hub.iam_service.model.constants.ApiLogMessage;
import com.post_hub.iam_service.model.dto.comment.CommentDTO;
import com.post_hub.iam_service.model.request.comment.CommentRequest;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import com.post_hub.iam_service.service.CommentService;
import com.post_hub.iam_service.utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${end.point.comments}")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<IamResponse<CommentDTO>> create(
            @PathVariable(name = "postId") Integer postId,
            @Valid @RequestBody CommentRequest request,
            Principal principal) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        IamResponse<CommentDTO> result = commentService.create(postId, request, principal.getName());
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<IamResponse<ArrayList<CommentDTO>>> getByPostId(@PathVariable(name = "postId") Integer postId) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        IamResponse<ArrayList<CommentDTO>> result = commentService.getByPostId(postId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IamResponse<CommentDTO>> getById(
            @PathVariable(name = "postId") Integer postId,
            @PathVariable(name = "id") Integer id) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        IamResponse<CommentDTO> result = commentService.getById(postId, id);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable(name = "postId") Integer postId,
            @PathVariable(name = "id") Integer id) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        commentService.deleteById(postId, id);

        return ResponseEntity.ok().build();
    }
}
