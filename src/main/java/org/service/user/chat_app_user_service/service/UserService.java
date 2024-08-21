package org.service.user.chat_app_user_service.service;

import org.service.user.chat_app_user_service.DTO.UserDTO;
import org.service.user.chat_app_user_service.DTO.request.UserInsertDTO;
import org.service.user.chat_app_user_service.DTO.request.UserPasswdUpdateDTO;
import org.service.user.chat_app_user_service.DTO.request.UserUpdateDTO;
import org.service.user.chat_app_user_service.entity.User;
import org.service.user.chat_app_user_service.exception.user.UserNotFoundException;

import java.math.BigInteger;
import java.util.List;

public interface UserService {

	void deleteUserById(BigInteger userId);

	void updateUserDeletedDate(BigInteger userId);

	UserDTO getUserById(BigInteger userId) throws UserNotFoundException;

	void updateUserById(BigInteger userId, UserUpdateDTO userUpdateDTO);

	void add(User user);

	void deleteUserByEmail(String email);

	void signup(UserInsertDTO userInsertDTO);

	void updatePassword(BigInteger userId, UserPasswdUpdateDTO userPasswordDTO);

	List<UserDTO> getUsers();

}
