package com.post_hub.iam_service.mapper;

import com.post_hub.iam_service.model.dto.postLike.PostLikeDTO;
import com.post_hub.iam_service.model.entity.PostLike;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PostLikeMapper {
    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "user.id", target = "userId")
    PostLikeDTO toPostLikeDTO(PostLike postLike);
}
