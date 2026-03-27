package com.post_hub.iam_service.mapper;

import com.post_hub.iam_service.model.dto.commentLike.CommentLikeDTO;
import com.post_hub.iam_service.model.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CommentLikeMapper {
    @Mapping(source = "comment.id", target = "commentId")
    @Mapping(source = "user.id", target = "userId")
    CommentLikeDTO toCommentLikeDTO(CommentLike commentLike);

	@Mapping(target = "id", ignore = true)
	@Mapping(source = "comment", target = "comment")
	@Mapping(source = "user", target = "user")
	CommentLike createCommentLike(Comment comment, User user);
}
