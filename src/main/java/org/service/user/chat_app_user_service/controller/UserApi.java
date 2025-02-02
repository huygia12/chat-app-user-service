package org.service.user.chat_app_user_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.service.user.chat_app_user_service.DTO.UserDTO;
import org.service.user.chat_app_user_service.DTO.request.UserInsertionDTO;
import org.service.user.chat_app_user_service.DTO.request.UserPasswdUpdateDTO;
import org.service.user.chat_app_user_service.DTO.request.UserUpdateDTO;
import org.service.user.chat_app_user_service.DTO.request.ValidUserDTO;
import org.service.user.chat_app_user_service.constants.StatusMessage;
import org.service.user.chat_app_user_service.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RequestMapping("/api/v1/users")
@Tag(name = "User Management")
public interface UserApi {

	@Operation(summary = "Get all users",
			responses = { @ApiResponse(responseCode = "200", description = StatusMessage.SUCCESS,
					content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))) })
	@GetMapping
	public ResponseEntity<List<UserDTO>> getUsers();

	@Operation(summary = "Get user by id",
			responses = {
					@ApiResponse(responseCode = "200", description = StatusMessage.SUCCESS,
							content = @Content(schema = @Schema(implementation = UserDTO.class))),
					@ApiResponse(responseCode = "404", description = StatusMessage.USER_NOT_FOUND,
							content = @Content(schema = @Schema(implementation = ErrorResponse.class),
									mediaType = "application/json")) })
	@GetMapping(value = "/{userId}")
	public ResponseEntity<UserDTO> getUser(@PathVariable("userId") BigInteger userId);

	@Operation(summary = "Get valid user",
			responses = {
					@ApiResponse(responseCode = "200", description = StatusMessage.SUCCESS,
							content = @Content(schema = @Schema(implementation = UserDTO.class))),
					@ApiResponse(responseCode = "404", description = StatusMessage.USER_NOT_FOUND,
							content = @Content(schema = @Schema(implementation = ErrorResponse.class),
									mediaType = "application/json")),
					@ApiResponse(responseCode = "422", description = StatusMessage.WRONG_PASSWORD,
							content = @Content(schema = @Schema(implementation = ErrorResponse.class),
									mediaType = "application/json")) })
	@PostMapping(value = "/valid-user")
	public ResponseEntity<UserDTO> getValidUser(@RequestBody @Valid ValidUserDTO validUserDTO);

	@Operation(summary = "Update user by id",
			responses = {
					@ApiResponse(responseCode = "200", description = StatusMessage.SUCCESS,
							content = @Content(schema = @Schema(implementation = UserDTO.class))),
					@ApiResponse(responseCode = "404", description = StatusMessage.USER_NOT_FOUND,
							content = @Content(schema = @Schema(implementation = ErrorResponse.class),
									mediaType = "application/json")),
					@ApiResponse(responseCode = "409", description = StatusMessage.EMAIL_EXISTED,
							content = @Content(schema = @Schema(implementation = ErrorResponse.class),
									mediaType = "application/json")) })
	@PutMapping(value = "/{userId}")
	public ResponseEntity updateUser(@PathVariable("userId") BigInteger userId,
			@RequestBody @Valid UserUpdateDTO userUpdateDTO);

	@Operation(summary = "User registration",
			responses = {
					@ApiResponse(responseCode = "200", description = StatusMessage.SUCCESS,
							content = @Content(schema = @Schema(implementation = UserDTO.class))),
					@ApiResponse(responseCode = "404", description = StatusMessage.USER_NOT_FOUND,
							content = @Content(schema = @Schema(implementation = ErrorResponse.class),
									mediaType = "application/json")),
					@ApiResponse(responseCode = "409", description = StatusMessage.EMAIL_EXISTED,
							content = @Content(schema = @Schema(implementation = ErrorResponse.class),
									mediaType = "application/json")) })
	@PostMapping("/signup")
	public ResponseEntity signup(@RequestBody @Valid UserInsertionDTO userInsertDTO);

	@Operation(summary = "Update user's password by id",
			responses = {
					@ApiResponse(responseCode = "200", description = StatusMessage.SUCCESS,
							content = @Content(mediaType = "application/json")),
					@ApiResponse(responseCode = "404", description = StatusMessage.USER_NOT_FOUND,
							content = @Content(schema = @Schema(implementation = ErrorResponse.class),
									mediaType = "application/json")),
					@ApiResponse(responseCode = "422", description = StatusMessage.WRONG_PASSWORD,
							content = @Content(schema = @Schema(implementation = ErrorResponse.class),
									mediaType = "application/json")) })
	@PatchMapping(value = "/{userId}/password")
	public ResponseEntity updatePassword(@PathVariable("userId") BigInteger userId,
			@RequestBody @Valid UserPasswdUpdateDTO userPasswdUpdateDTO);

	@Operation(summary = "Delete user by id",
			responses = { @ApiResponse(responseCode = "200", description = StatusMessage.SUCCESS,
					content = @Content(mediaType = "application/json")) })
	@DeleteMapping(value = "/{userId}")
	public ResponseEntity deleteUser(@PathVariable("userId") BigInteger userId);

}
