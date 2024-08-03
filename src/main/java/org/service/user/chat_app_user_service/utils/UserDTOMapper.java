package org.service.user.chat_app_user_service.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.service.user.chat_app_user_service.DTO.UserDTO;
import org.service.user.chat_app_user_service.controller.UserController;
import org.service.user.chat_app_user_service.entity.User;

import java.util.function.Function;

public class UserDTOMapper implements Function<User, UserDTO> {

	@Override
	public UserDTO apply(User user) {
		return (user == null) ? null
				: new UserDTO(user.getUserId(), user.getEmail(), user.getUsername(), user.getFirstName(),
						user.getLastName(), user.getBirthday(), user.getGender().getName(), user.getRole().getName(),
						user.getPhoneNumber(), user.getPrivacy(), user.getStatus().getName(), user.getLastActiveAt(),
						user.getAvatarUrl(), user.getCreatedAt(), user.getUpdatedAt(), user.getDeletedAt());
	}

}
