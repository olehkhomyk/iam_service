package com.post_hub.iam_service.mapper;

import com.post_hub.iam_service.model.dto.postLike.PostLikeDTO;
import com.post_hub.iam_service.model.entity.Post;
import com.post_hub.iam_service.model.entity.PostLike;
import com.post_hub.iam_service.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PostLikeMapper {
    @Mapping(source = "user.id", target = "userId")
    PostLikeDTO toPostLikeDTO(PostLike postLike);

	@Mapping(target = "id", ignore = true)
	@Mapping(source = "post", target = "post")
	@Mapping(source = "user", target = "user")
	PostLike createPostLike(Post post, User user);
}
