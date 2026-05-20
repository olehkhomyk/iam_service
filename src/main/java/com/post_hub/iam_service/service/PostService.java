package com.post_hub.iam_service.service;

import com.post_hub.iam_service.model.dto.post.PostDTO;
import com.post_hub.iam_service.model.dto.post.PostSearchDTO;
import com.post_hub.iam_service.model.request.post.PostRequest;
import com.post_hub.iam_service.model.request.post.PostSearchRequest;
import com.post_hub.iam_service.model.request.post.UpdatePostRequest;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import com.post_hub.iam_service.model.respsonse.PaginationResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Pageable;

public interface PostService {
	IamResponse<PostDTO> getById(@NotNull Integer id);

	IamResponse<PostDTO> create(@NotNull PostRequest postRequest, Integer userId, MultipartFile image);

	IamResponse<PostDTO> update(@NotNull Integer id, @NotNull UpdatePostRequest updatePostRequest, MultipartFile image);

	void sofDeletePost(@NotNull Integer id);

	void hardDeletePost(@NotNull Integer id);

	IamResponse<PaginationResponse<PostSearchDTO>> findAllPosts(Pageable pageable, Boolean includeComments);

	IamResponse<PaginationResponse<PostSearchDTO>> searchPosts(@NotNull PostSearchRequest request, Pageable pageable, Boolean includeComments);

	void likePost(@NotNull Integer postId, @NotNull Integer userId);

	void unlikePost(@NotNull Integer postId, @NotNull Integer userId);
}
