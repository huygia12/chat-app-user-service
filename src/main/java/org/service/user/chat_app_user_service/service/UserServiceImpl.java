package org.service.user.chat_app_user_service.service;

import lombok.AllArgsConstructor;
import org.service.user.chat_app_user_service.DTO.UserDTO;
import org.service.user.chat_app_user_service.DTO.request.UserInsertDTO;
import org.service.user.chat_app_user_service.DTO.request.UserPasswdUpdateDTO;
import org.service.user.chat_app_user_service.DTO.request.UserUpdateDTO;
import org.service.user.chat_app_user_service.constants.Gender;
import org.service.user.chat_app_user_service.constants.Role;
import org.service.user.chat_app_user_service.constants.StatusMessage;
import org.service.user.chat_app_user_service.constants.UserStatus;
import org.service.user.chat_app_user_service.entity.User;
import org.service.user.chat_app_user_service.exception.user.UserExistedException;
import org.service.user.chat_app_user_service.exception.user.UserInvalidException;
import org.service.user.chat_app_user_service.exception.user.UserNotFoundException;
import org.service.user.chat_app_user_service.repository.UserRepository;
import org.service.user.chat_app_user_service.utils.UserDTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	private final UserDTOMapper userDTOMapper;

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private static int id = 123123;

	@Override
	public void deleteUserById(BigInteger userId) {
		userRepository.deleteById(userId);
	}

	@Override
	public UserDTO getUserById(BigInteger userId) throws UserNotFoundException {
		UserDTO userDTO = userRepository.findById(userId)
			.map(userDTOMapper::apply)
			.orElseThrow(() -> new UserNotFoundException(StatusMessage.USER_NOT_FOUND));
		return userDTO;
	}

	@Override
	public void updateUserById(BigInteger userId, UserUpdateDTO userUpdateDTO) throws UserNotFoundException {
		User userEmailHolder = getUserByEmail(userUpdateDTO.getEmail());
		User userNameHolder = getUserByName(userUpdateDTO.getUsername());

		if (userEmailHolder != null) {
			throw new UserExistedException(StatusMessage.EMAIL_EXISTED);
		}

		if (userNameHolder != null) {
			throw new UserExistedException(StatusMessage.NAME_EXISTED);
		}

		User user = getUserFullAttributes(userId);

		if (userUpdateDTO.getEmail() != null)
			user.setEmail(userUpdateDTO.getEmail());
		if (userUpdateDTO.getUsername() != null)
			user.setUsername(userUpdateDTO.getUsername());
		if (userUpdateDTO.getFirstName() != null)
			user.setFirstName(userUpdateDTO.getFirstName());
		if (userUpdateDTO.getLastName() != null)
			user.setLastName(userUpdateDTO.getLastName());
		if (userUpdateDTO.getBirthday() != null)
			user.setBirthday(userUpdateDTO.getBirthday());
		if (userUpdateDTO.getGender() != null)
			user.setGender(Gender.valueOf(userUpdateDTO.getGender()));
		if (userUpdateDTO.getPhoneNumber() != null)
			user.setPhoneNumber(userUpdateDTO.getPhoneNumber());
		if (userUpdateDTO.getAvatarUrl() != null)
			user.setAvatarUrl(userUpdateDTO.getAvatarUrl());

		userRepository.save(user);
	}

	@Override
	public void signup(UserInsertDTO userInsertDTO) throws UserExistedException {
		User userEmailHolder = getUserByEmail(userInsertDTO.getEmail());
		User userNameHolder = getUserByName(userInsertDTO.getUsername());

		if(userEmailHolder != null) {
			throw new UserExistedException(StatusMessage.EMAIL_EXISTED);
		}

		if(userNameHolder != null) {
			throw new UserExistedException(StatusMessage.NAME_EXISTED);
		}

		User user = new User();

		user.setUserId(new BigInteger(STR."\{id++}"));
		user.setEmail(userInsertDTO.getEmail());
		user.setUsername(userInsertDTO.getUsername());
		user.setPassword(userInsertDTO.getPassword());
		user.setFirstName(userInsertDTO.getFirstName());
		user.setLastName(userInsertDTO.getLastName());
		user.setBirthday(userInsertDTO.getBirthday());
		user.setGender(Gender.valueOf(userInsertDTO.getGender()));
		user.setRole(Role.valueOf("USER"));
		user.setPhoneNumber(userInsertDTO.getPhoneNumber());
		user.setStatus(UserStatus.valueOf("OFFLINE"));
		user.setAvatarUrl(userInsertDTO.getAvatarUrl());

		userRepository.save(user);
	}

	@Override
	public void updatePassword(BigInteger userId, UserPasswdUpdateDTO userPasswordDTO)
			throws UserNotFoundException, UserInvalidException {
		User user = getValidUser(userId, userPasswordDTO.getOldPassword());

		user.setPassword(passwordEncoder.encode(userPasswordDTO.getNewPassword()));
		userRepository.save(user);
	}

	@Override
	public List<UserDTO> getUsers() {
		List<UserDTO> users = new ArrayList<>();
		userRepository.findAll().stream().forEach(user -> {
			users.add(userDTOMapper.apply(user));
		});
		return users;
	}

	private User getValidUser(BigInteger userId, String password) throws UserNotFoundException, UserInvalidException {
		User user = getUserFullAttributes(userId);

		if (!isValid(password, user)) {
			throw new UserInvalidException(StatusMessage.WRONG_PASSWORD);
		}

		return user;
	}

	private User getUserByName(String name) {
		return userRepository.findByUsername(name).orElse(null);
	}

	private User getUserByEmail(String email) {
		return userRepository.findByEmail(email).orElse(null);
	}

	private boolean isValid(String password, User user) {
		return passwordEncoder.matches(password, user.getPassword());
	}

	private User getUserFullAttributes(BigInteger userId) throws UserNotFoundException {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(StatusMessage.USER_NOT_FOUND));
		return user;
	}

}
