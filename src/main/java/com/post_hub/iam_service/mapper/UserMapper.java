package com.post_hub.iam_service.mapper;

import com.post_hub.iam_service.model.dto.user.UserDTO;
import com.post_hub.iam_service.model.dto.user.UserSearchDTO;
import com.post_hub.iam_service.model.entities.User;
import com.post_hub.iam_service.model.enums.RegistrationStatus;
import com.post_hub.iam_service.model.request.user.NewUserRequest;
import com.post_hub.iam_service.model.request.user.UpdateUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
		imports = {RegistrationStatus.class, Object.class}
)
public interface UserMapper {
	UserDTO toDTO(User user);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "created", ignore = true)
	@Mapping(target = "registrationStatus", expression = "java(RegistrationStatus.ACTIVE)")
	User createUser(NewUserRequest userDTO);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "created", ignore = true)
	// Mapping target means mutate target entity;
	void updateUser(@MappingTarget User user, UpdateUserRequest request);

	@Mapping(source = "deleted", target = "isDeleted")
	UserSearchDTO toUserSearchDTO(User user);
}
