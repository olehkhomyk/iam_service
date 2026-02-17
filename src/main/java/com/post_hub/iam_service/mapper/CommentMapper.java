package com.post_hub.iam_service.mapper;

import com.post_hub.iam_service.model.dto.comment.CommentDTO;
import com.post_hub.iam_service.model.entity.Comment;
import com.post_hub.iam_service.model.request.comment.CommentRequest;
import org.hibernate.type.descriptor.DateTimeUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Objects;

@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
		imports = {DateTimeUtils.class, Objects.class}
)
public interface CommentMapper {
	@Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
	CommentDTO toCommentDTO(Comment comment);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	Comment createComment(CommentRequest commentRequest);
}
