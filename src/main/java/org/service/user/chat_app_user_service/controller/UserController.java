package org.service.user.chat_app_user_service.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.service.user.chat_app_user_service.DTO.UserDTO;
import org.service.user.chat_app_user_service.DTO.request.UserInsertDTO;
import org.service.user.chat_app_user_service.DTO.request.UserPasswdUpdateDTO;
import org.service.user.chat_app_user_service.DTO.request.UserUpdateDTO;
import org.service.user.chat_app_user_service.constants.StatusMessage;
import org.service.user.chat_app_user_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

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
		return new ResponseEntity(userService.getUserById(userId), HttpStatus.OK);
	}

	@Override
	public ResponseEntity updateUser(@PathVariable BigInteger userId, @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
		userService.updateUserById(userId, userUpdateDTO);
		return new ResponseEntity(StatusMessage.SUCCESS, HttpStatus.OK);
	}

	@Override
	public ResponseEntity signup(@RequestBody @Valid UserInsertDTO userInsertDTO) {
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
		userService.deleteUserById(userId);
		return new ResponseEntity(StatusMessage.SUCCESS, HttpStatus.OK);
	}

}
