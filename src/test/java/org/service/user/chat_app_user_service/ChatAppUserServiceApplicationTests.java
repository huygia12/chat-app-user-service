package org.service.user.chat_app_user_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.service.user.chat_app_user_service.DTO.UserDTO;
import org.service.user.chat_app_user_service.DTO.request.UserPasswdUpdateDTO;
import org.service.user.chat_app_user_service.DTO.request.UserUpdateDTO;
import org.service.user.chat_app_user_service.constants.StatusMessage;
import org.service.user.chat_app_user_service.exception.user.UserInvalidException;
import org.service.user.chat_app_user_service.exception.user.UserNotFoundException;
import org.service.user.chat_app_user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
class ChatAppUserServiceApplicationTests {

	private final String userUrl = "/api/v1/users/";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	private BigInteger userID;

	private UserUpdateDTO userUpdateRequest;

	private UserDTO userUpdateResponse;

	private UserPasswdUpdateDTO passwdUpdateRequest;

	@BeforeEach
	public void setup() {
		userID = new BigInteger("123123");
		userUpdateRequest = UserUpdateDTO.builder()
			.email("123@gmail.com")
			.username("Huy")
			.firstName("Ryan")
			.lastName("Reiner")
			.gender("MALE")
			.phoneNumber("123-123-121")
			.avatarUrl("/avt.jpg")
			.birthday(LocalDate.of(1999, 2, 1))
			.build();

		userUpdateResponse = UserDTO.builder()
			.userId(userID)
			.email(userUpdateRequest.getEmail())
			.username(userUpdateRequest.getUsername())
			.firstName(userUpdateRequest.getFirstName())
			.lastName(userUpdateRequest.getLastName())
			.gender(userUpdateRequest.getGender())
			.phoneNumber(userUpdateRequest.getPhoneNumber())
			.avatarUrl(userUpdateRequest.getAvatarUrl())
			.birthday(userUpdateRequest.getBirthday())
			.createdAt(LocalDateTime.of(2024, 7, 7, 7, 7, 7))
			.build();

		passwdUpdateRequest = UserPasswdUpdateDTO.builder().oldPassword("12345678").newPassword("1234567@").build();
	}

	@Test
	void updateUser_validRequest_success() throws JsonProcessingException {
		//Given
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		String content = mapper.writeValueAsString(userUpdateRequest);

		//When, Then
		Mockito.doNothing().when(userService).updateUserById(ArgumentMatchers.any(), ArgumentMatchers.any());

		try{
			mockMvc.perform(MockMvcRequestBuilders
						.put(STR."\{userUrl}\{userID}")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(content))
					.andExpect(MockMvcResultMatchers.status().isOk());
		}catch(Exception e){
			fail(e.getMessage());
		}
	}

	@Test
	void updateUser_invalidEmail_fail() throws JsonProcessingException {
		//Given
		userUpdateRequest.setEmail("123gmail.com");

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		String content = mapper.writeValueAsString(userUpdateRequest);

		//When, Then
		Mockito.doNothing().when(userService).updateUserById(ArgumentMatchers.any(), ArgumentMatchers.any());

		try{
			mockMvc.perform(MockMvcRequestBuilders
							.put(STR."\{userUrl}\{userID}")
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.content(content))
					.andExpect(MockMvcResultMatchers.status().isBadRequest())
					.andExpect(MockMvcResultMatchers.jsonPath("message").value(StatusMessage.EMAIL_INVALID));
		}catch(Exception e){
			fail(e.getMessage());
		}
	}

	@Test
	void updateUser_invalidBirthDate_fail() throws JsonProcessingException {
		//Given
		userUpdateRequest.setBirthday(LocalDate.of(2999, 2, 1));

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		String content = mapper.writeValueAsString(userUpdateRequest);

		//When, Then
		Mockito.doNothing().when(userService).updateUserById(ArgumentMatchers.any(), ArgumentMatchers.any());

		try{
			mockMvc.perform(MockMvcRequestBuilders
							.put(STR."\{userUrl}\{userID}")
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.content(content))
					.andExpect(MockMvcResultMatchers.status().isBadRequest())
					.andExpect(MockMvcResultMatchers.jsonPath("message").value(StatusMessage.BIRTH_DATE_INVALID));
		}catch(Exception e){
			fail(e.getMessage());
		}
	}

	@Test
	void updateUser_genderInvalid_fail() throws JsonProcessingException {
		//Given
		userUpdateRequest.setGender("RAINBOW");

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		String content = mapper.writeValueAsString(userUpdateRequest);

		//When, Then
		Mockito.doNothing().when(userService).updateUserById(ArgumentMatchers.any(), ArgumentMatchers.any());

		try{
			mockMvc.perform(MockMvcRequestBuilders
							.put(STR."\{userUrl}\{userID}")
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.content(content))
					.andExpect(MockMvcResultMatchers.status().isBadRequest())
					.andExpect(MockMvcResultMatchers.jsonPath("message").value(StatusMessage.GENDER_INVALID));
		}catch(Exception e){
			fail(e.getMessage());
		}
	}

	@Test
	void updateUser_userNotFound_fail() throws JsonProcessingException {
		//Given
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		String content = mapper.writeValueAsString(userUpdateRequest);

		//When
		Mockito.doThrow(new UserNotFoundException(StatusMessage.USER_NOT_FOUND))
				.when(userService).updateUserById(ArgumentMatchers.any(), ArgumentMatchers.any());

		try{
			mockMvc.perform(MockMvcRequestBuilders
							.put(STR."\{userUrl}\{userID}")
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.content(content))
					.andExpect(MockMvcResultMatchers.status().isNotFound())
					.andExpect(MockMvcResultMatchers.jsonPath("message").value(StatusMessage.USER_NOT_FOUND));

			//Then
			assertThrows(UserNotFoundException.class, () -> userService.updateUserById(ArgumentMatchers.any(), ArgumentMatchers.any()));
		}catch(Exception e){
			fail(e.getMessage());
		}
	}

	@Test
	void deleteUser_validRequest_success() {
		//When
		Mockito.doNothing()
				.when(userService).deleteUserById(ArgumentMatchers.any());

		try {
			mockMvc.perform(MockMvcRequestBuilders
							.delete(STR."\{userUrl}\{userID}"))
					.andExpect(MockMvcResultMatchers.status().isOk());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	void deleteUser_userNotFound_fail() {
		//When
		Mockito.doThrow(new UserNotFoundException(StatusMessage.USER_NOT_FOUND))
				.when(userService).deleteUserById(ArgumentMatchers.any());

		try{
			mockMvc.perform(MockMvcRequestBuilders
							.delete(STR."\{userUrl}\{userID}"))
					.andExpect(MockMvcResultMatchers.status().isNotFound())
					.andExpect(MockMvcResultMatchers.jsonPath("message").value(StatusMessage.USER_NOT_FOUND));

			//Then
			assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(ArgumentMatchers.any()));
		}catch(Exception e){
			fail(e.getMessage());
		}
	}

	@Test
	void getUser_validRequest_success() {
		//When
		Mockito.when(userService.getUserById(ArgumentMatchers.any()))
				.thenReturn(userUpdateResponse);

		try {
			mockMvc.perform(MockMvcRequestBuilders
							.get(STR."\{userUrl}\{userID}"))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.jsonPath("userId").value(userID));

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	void getUser_userNotFound_fail() {
		//When
		Mockito.doThrow(new UserNotFoundException(StatusMessage.USER_NOT_FOUND))
				.when(userService).getUserById(ArgumentMatchers.any());

		try{
			mockMvc.perform(MockMvcRequestBuilders
							.get(STR."\{userUrl}\{userID}"))
					.andExpect(MockMvcResultMatchers.status().isNotFound())
					.andExpect(MockMvcResultMatchers.jsonPath("message").value(StatusMessage.USER_NOT_FOUND));

			//Then
			assertThrows(UserNotFoundException.class, () -> userService.getUserById(ArgumentMatchers.any()));
		}catch(Exception e){
			fail(e.getMessage());
		}
	}

	@Test
	void updatePassword_validRequest_success() throws JsonProcessingException {
		//Given
		ObjectMapper mapper = new ObjectMapper();
		String content = mapper.writeValueAsString(passwdUpdateRequest);

		//When, Then
		try{
			mockMvc.perform(MockMvcRequestBuilders
							.patch(STR."\{userUrl}\{userID}/password")
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.content(content))
					.andExpect(MockMvcResultMatchers.status().isOk());
		}catch(Exception e){
			fail(e.getMessage());
		}
	}

	@Test
	void updatePassword_oldPasswordIncorrect_fail() throws JsonProcessingException {
		//Given
		ObjectMapper mapper = new ObjectMapper();
		String content = mapper.writeValueAsString(passwdUpdateRequest);

		//When, Then
		Mockito.doThrow(new UserInvalidException(StatusMessage.WRONG_PASSWORD))
			.when(userService).updatePassword(ArgumentMatchers.any(), ArgumentMatchers.any());

		try{
			mockMvc.perform(MockMvcRequestBuilders
							.patch(STR."\{userUrl}\{userID}/password")
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.content(content))
					.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
					.andExpect(MockMvcResultMatchers.jsonPath("message").value(StatusMessage.WRONG_PASSWORD));
		}catch(Exception e){
			fail(e.getMessage());
		}
	}

	@Test
	void updatePassword_oldPasswordNotEnoughCharacter_fail() throws JsonProcessingException {
		//Given
		passwdUpdateRequest.setOldPassword("123");
		ObjectMapper mapper = new ObjectMapper();
		String content = mapper.writeValueAsString(passwdUpdateRequest);

		//When, Then
		try{
			mockMvc.perform(MockMvcRequestBuilders
							.patch(STR."\{userUrl}\{userID}/password")
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.content(content))
					.andExpect(MockMvcResultMatchers.status().isBadRequest())
					.andExpect(MockMvcResultMatchers.jsonPath("message").value(StatusMessage.PASSWORD_MINIMUM));
		}catch(Exception e){
			fail(e.getMessage());
		}
	}

	@Test
	void updatePassword_newPasswordNotEnoughCharacter_fail() throws JsonProcessingException {
		//Given
		passwdUpdateRequest.setNewPassword("123");
		ObjectMapper mapper = new ObjectMapper();
		String content = mapper.writeValueAsString(passwdUpdateRequest);

		//When, Then
		try{
			mockMvc.perform(MockMvcRequestBuilders
							.patch(STR."\{userUrl}\{userID}/password")
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.content(content))
					.andExpect(MockMvcResultMatchers.status().isBadRequest())
					.andExpect(MockMvcResultMatchers.jsonPath("message").value(StatusMessage.PASSWORD_MINIMUM));
		}catch(Exception e){
			fail(e.getMessage());
		}
	}

}
