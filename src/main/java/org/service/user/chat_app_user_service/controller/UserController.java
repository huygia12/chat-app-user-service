package org.service.user.chat_app_user_service.controller;

import java.math.BigInteger;
import java.util.List;

import org.service.user.chat_app_user_service.DTO.UserDTO;
import org.service.user.chat_app_user_service.DTO.request.UserInsertionDTO;
import org.service.user.chat_app_user_service.DTO.request.UserPasswdUpdateDTO;
import org.service.user.chat_app_user_service.DTO.request.UserUpdateDTO;
import org.service.user.chat_app_user_service.DTO.request.ValidUserDTO;
import org.service.user.chat_app_user_service.constants.StatusMessage;
import org.service.user.chat_app_user_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class UserController implements UserApi {

	private final UserService userService;

	@Override
	public ResponseEntity<List<UserDTO>> getUsers() {
		return new ResponseEntity(userService.getUsers(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<UserDTO> getUser(@PathVariable BigInteger userId) {
		UserDTO userDTO = userService.getUserById(userId);
		return new ResponseEntity(userDTO, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<UserDTO> getValidUser(@RequestBody @Valid ValidUserDTO validUserDTO) {
		UserDTO userDTO = userService.getValidUser(validUserDTO.getEmail(), validUserDTO.getPassword());
		return new ResponseEntity(userDTO, HttpStatus.OK);
	}

	@Override
	public ResponseEntity updateUser(@PathVariable BigInteger userId, @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
		userService.updateUserById(userId, userUpdateDTO);
		return new ResponseEntity(StatusMessage.SUCCESS, HttpStatus.OK);
	}

	@Override
	public ResponseEntity signup(@RequestBody @Valid UserInsertionDTO userInsertDTO) {
		userService.signup(userInsertDTO);
		return new ResponseEntity(StatusMessage.SUCCESS, HttpStatus.OK);
	}

	@Override
	public ResponseEntity updatePassword(@PathVariable BigInteger userId,
			@RequestBody @Valid UserPasswdUpdateDTO userPasswordDTO) {
		userService.updatePassword(userId, userPasswordDTO);
		return new ResponseEntity(StatusMessage.SUCCESS, HttpStatus.OK);
	}

	@Override
	public ResponseEntity deleteUser(@PathVariable BigInteger userId) {
		userService.updateUserDeletedDate(userId);
		return new ResponseEntity(StatusMessage.SUCCESS, HttpStatus.OK);
	}

}
