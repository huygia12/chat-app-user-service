package org.service.user.chat_app_user_service.utils;

import java.util.function.Function;

import org.service.user.chat_app_user_service.DTO.UserDTO;
import org.service.user.chat_app_user_service.entity.User;

public class UserDTOMapper implements Function<User, UserDTO> {

	@Override
	public UserDTO apply(User user) {
		return (user == null) ? null
				: new UserDTO(user.getUserId(), user.getEmail(), user.getUsername(), user.getFirstName(),
						user.getLastName(), user.getBirthday(),
						user.getGender() != null ? user.getGender().getName() : null,
						user.getRole() != null ? user.getRole().getName() : null, user.getPhoneNumber(),
						user.getPrivacy(), user.getStatus().getName(), user.getLastActiveAt(), user.getAvatarUrl(),
						user.getCreatedAt(), user.getUpdatedAt(), user.getDeletedAt());
	}

}
