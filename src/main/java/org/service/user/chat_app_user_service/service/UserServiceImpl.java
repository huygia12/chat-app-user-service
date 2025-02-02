package org.service.user.chat_app_user_service.service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.service.user.chat_app_user_service.DTO.UserDTO;
import org.service.user.chat_app_user_service.DTO.request.UserInsertionDTO;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

	private final IdGeneratorService idGeneratorService;

	private final UserDTOMapper userDTOMapper;

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	@Override
	public void deleteUserById(BigInteger userId) {
		userRepository.deleteById(userId);
	}

	@Override
	public void deleteUserByEmail(String email) {
		userRepository.deleteByEmail(email);
	}

	@Override
	public void updateUserDeletedDate(BigInteger userId) {
		User user = getUserFullAttributes(userId);
		user.setDeletedAt(LocalDateTime.now());
		userRepository.save(user);
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
		UserDTO userIDHolder = getUserById(userId);

		if (userIDHolder == null) {
			throw new UserNotFoundException(StatusMessage.USER_NOT_FOUND);
		}

		User duplicatedUserWithEmail = getUserByEmail(userUpdateDTO.getEmail());
		User duplicatedUserWithName = getUserByName(userUpdateDTO.getUsername());

		if (duplicatedUserWithEmail != null && duplicatedUserWithEmail.getUserId() != userId) {
			throw new UserExistedException(StatusMessage.EMAIL_EXISTED);
		}

		if (duplicatedUserWithName != null && duplicatedUserWithName.getUserId() != userId) {
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
	public void add(User user) {
		BigInteger newID = idGeneratorService.generateID();
		user.setUserId(newID);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
	}

	@Override
	public void signup(UserInsertionDTO userInsertDTO) throws UserExistedException {
		User userEmailHolder = getUserByEmail(userInsertDTO.getEmail());
		User userNameHolder = getUserByName(userInsertDTO.getUsername());

		if (userEmailHolder != null) {
			throw new UserExistedException(StatusMessage.EMAIL_EXISTED);
		}

		if (userNameHolder != null) {
			throw new UserExistedException(StatusMessage.NAME_EXISTED);
		}

		BigInteger newID = idGeneratorService.generateID();

		User user = User.builder()
			.userId(newID)
			.email(userInsertDTO.getEmail())
			.username(userInsertDTO.getUsername())
			.password(userInsertDTO.getPassword())
			.role(Role.USER)
			.status(UserStatus.OFFLINE)
			.build();

		add(user);
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

	@Override
	public UserDTO getValidUser(String email, String password) throws UserNotFoundException, UserInvalidException {
		User user = getUserByEmail(email);

		if (user == null) {
			throw new UserNotFoundException(StatusMessage.USER_NOT_FOUND);
		}

		UserDTO userDTO = userDTOMapper.apply(user);

		return userDTO;
	}

	public User getValidUser(BigInteger id, String password) throws UserNotFoundException, UserInvalidException {
		User user = getUserFullAttributes(id);

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

	private boolean isValid(String plainPassword, User user) {
		return passwordEncoder.matches(plainPassword, user.getPassword());
	}

	private User getUserFullAttributes(BigInteger userId) throws UserNotFoundException {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(StatusMessage.USER_NOT_FOUND));
		return user;
	}

}
