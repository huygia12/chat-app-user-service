package org.service.user.chat_app_user_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.service.user.chat_app_user_service.DTO.request.UserPasswdUpdateDTO;
import org.service.user.chat_app_user_service.constants.StatusMessage;
import org.service.user.chat_app_user_service.entity.User;
import org.service.user.chat_app_user_service.exception.user.UserInvalidException;
import org.service.user.chat_app_user_service.exception.user.UserNotFoundException;
import org.service.user.chat_app_user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class ChatAppUserServiceApplicationTests {

	private static final int PORT = 8030;

	private static final String VERSION = "v1";

	private static final String PROTOCOL = "http";

	private static final String DOMAIN = "localhost";

	private static final String API_URL;

	private static final String SIGNUP_URL;

	private static List<User> tempUsers;

	static {
        API_URL = STR."\{PROTOCOL}://\{DOMAIN}:\{PORT}/api/\{VERSION}/users";
        SIGNUP_URL = STR."\{API_URL}/signup";
    }

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserService userService;

	private UserPasswdUpdateDTO passwdUpdateRequest;

	private User randomUser;

	@BeforeEach
	public void setup() {
		tempUsers = getSampleUserFromJson();
		int size = tempUsers.size();
		randomUser = tempUsers.get((int) (Math.random() * size));
	}

	private static List<User> getSampleUserFromJson() {
		try (InputStream is = new ClassPathResource("user-sample.json").getInputStream()) {
			ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
			return Arrays.asList(mapper.readValue(is, User[].class));
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static String buildUpdatePasswordBodyRequest(User user, String newPassword) throws JsonProcessingException {
		String password = newPassword == null ? "123123" : newPassword;

		ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
		ObjectNode root = mapper.createObjectNode();

		ObjectNode pwJson = mapper.createObjectNode();
		pwJson.put("oldPassword", user.getPassword());
		pwJson.put("newPassword", password);

		root.setAll(pwJson);

		return mapper.writeValueAsString(root);
	}

	private static String buildUpdateUserBodyRequest(User user) throws JsonProcessingException {
		ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
		ObjectNode root = mapper.createObjectNode();

		ObjectNode userJson = mapper.createObjectNode();
		userJson.put("email", user.getEmail());
		userJson.put("username", user.getUsername());
		userJson.put("firstName", user.getFirstName());
		userJson.put("lastName", user.getLastName());
		userJson.put("gender", user.getGender().getName());
		userJson.put("phoneNumber", user.getPhoneNumber());
		userJson.put("avatarUrl", user.getAvatarUrl());
		userJson.put("birthday", user.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

		root.setAll(userJson);

		return mapper.writeValueAsString(root);
	}

	private static String buildSignupUserBodyRequest(User user) throws JsonProcessingException {
		ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
		ObjectNode root = mapper.createObjectNode();

		ObjectNode userJson = mapper.createObjectNode();
		userJson.put("email", user.getEmail());
		userJson.put("username", user.getUsername());
		userJson.put("password", user.getPassword());
		userJson.put("firstName", user.getFirstName());
		userJson.put("lastName", user.getLastName());
		userJson.put("gender", user.getGender().getName());
		userJson.put("phoneNumber", user.getPhoneNumber());
		userJson.put("avatarUrl", user.getAvatarUrl());
		userJson.put("birthday", user.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

		root.setAll(userJson);

		return mapper.writeValueAsString(root);
	}

	private void addTempUsers() {
		tempUsers.forEach(user -> userService.add(user.clone()));
	}

	private void removeTempUsers() {
		tempUsers.forEach(user -> userService.deleteUserById(user.getUserId()));
	}

	public static JsonNode convertStringToJson(String msg) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readTree(msg);
	}

	@Test
	void updateUser_200ValidRequest_success() throws JsonProcessingException {
	 	try{
	 	//Given
	 	addTempUsers();
	 	randomUser.setEmail("r+@example.com");
	 	randomUser.setUsername("Jimmy");

	 	mockMvc.perform(MockMvcRequestBuilders
	 		.put(STR."\{API_URL}/\{randomUser.getUserId()}")
	 		.contentType(MediaType.APPLICATION_JSON_VALUE)
	 		.content(buildUpdateUserBodyRequest(randomUser)))
	 		.andExpect(MockMvcResultMatchers.status().isOk());

	 	}catch(Exception e){
	 	fail(e.getMessage());
	 }finally{
	 	removeTempUsers();
	 }
	}

	@Test
	void updateUser_400InvalidEmail_fail() throws JsonProcessingException {
		try{
			//Given
			addTempUsers();
			randomUser.setEmail("123gmail.com");

			mockMvc.perform(MockMvcRequestBuilders
							.put(STR."\{API_URL}/\{randomUser.getUserId()}")
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.content(buildUpdateUserBodyRequest(randomUser)))
					.andExpect(MockMvcResultMatchers.status().isBadRequest())
					.andExpect(MockMvcResultMatchers.jsonPath("message").value(StatusMessage.EMAIL_INVALID));
		}catch(Exception e){
			fail(e.getMessage());
		}
		finally {
			removeTempUsers();
		}
	}

	@Test
	void updateUser_400InvalidBirthDate_fail() throws JsonProcessingException {
		try{
			//Given
			addTempUsers();
			randomUser.setBirthday(LocalDate.of(2999, 2, 1));

			mockMvc.perform(MockMvcRequestBuilders
							.put(STR."\{API_URL}/\{randomUser.getUserId()}")
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.content(buildUpdateUserBodyRequest(randomUser)))
					.andExpect(MockMvcResultMatchers.status().isBadRequest())
					.andExpect(MockMvcResultMatchers.jsonPath("message").value(StatusMessage.BIRTH_DATE_INVALID));
		}catch(Exception e){
			fail(e.getMessage());
		}
		finally {
			removeTempUsers();
		}
	}

	@Test
	void updateUser_404UserNotFound_fail() throws JsonProcessingException {
		try{
			//Given
			addTempUsers();

			mockMvc.perform(MockMvcRequestBuilders
							.put(STR."\{API_URL}/1111112222")
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.content(buildUpdateUserBodyRequest(randomUser)))
					.andExpect(MockMvcResultMatchers.status().isNotFound())
					.andExpect(MockMvcResultMatchers.jsonPath("message").value(StatusMessage.USER_NOT_FOUND));
		}catch(Exception e){
			fail(e.getMessage());
		}
		finally {
			removeTempUsers();
		}
	}

	@Test
	void updateUser_409EmailAlreadyBeenUsed_fail() throws JsonProcessingException {
		try{
			//Given
			addTempUsers();

			mockMvc.perform(MockMvcRequestBuilders
							.put(STR."\{API_URL}/\{randomUser.getUserId()}")
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.content(buildUpdateUserBodyRequest(randomUser)))
					.andExpect(MockMvcResultMatchers.status().isConflict())
					.andExpect(MockMvcResultMatchers.jsonPath("message").value(StatusMessage.EMAIL_EXISTED));
		}catch(Exception e){
			fail(e.getMessage());
		}
		finally {
			removeTempUsers();
		}
	}

	@Test
	void deleteUser_200ValidRequest_success() {
		try {
			//When
			addTempUsers();

			mockMvc.perform(MockMvcRequestBuilders
							.delete(STR."\{API_URL}/\{randomUser.getUserId()}"))
					.andExpect(MockMvcResultMatchers.status().isOk());
		} catch (Exception e) {
			fail(e.getMessage());
		}
		finally {
			removeTempUsers();
		}
	}

	@Test
	void deleteUser_404UserNotFound_fail() {
		//When
		try{
			addTempUsers();

			mockMvc.perform(MockMvcRequestBuilders
							.delete(STR."\{API_URL}/1111112222"))
					.andExpect(MockMvcResultMatchers.status().isNotFound())
					.andExpect(MockMvcResultMatchers.jsonPath("message").value(StatusMessage.USER_NOT_FOUND));
		}catch(Exception e){
			fail(e.getMessage());
		}
		finally {
			removeTempUsers();
		}
	}

	@Test
	void getUser_200ValidRequest_success() {
		try {
			addTempUsers();

			String response = mockMvc.perform(MockMvcRequestBuilders
							.get(STR."\{API_URL}/\{randomUser.getUserId()}"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();

			JsonNode body = convertStringToJson(response);
			assertEquals(randomUser.getUserId(), new BigInteger(body.get("userId").asText()));
		} catch (Exception e) {
			fail(e.getMessage());
		}
		finally {
			removeTempUsers();
		}
	}

	@Test
	void getUser_404UserNotFound_fail() {
		try{
			//When
			addTempUsers();

			mockMvc.perform(MockMvcRequestBuilders
							.get(STR."\{API_URL}/1111112222"))
					.andExpect(MockMvcResultMatchers.status().isNotFound())
					.andExpect(MockMvcResultMatchers.jsonPath("message").value(StatusMessage.USER_NOT_FOUND));
		}catch(Exception e){
			fail(e.getMessage());
		}
		finally {
			removeTempUsers();
		}
	}

	@Test
	void updatePassword_200ValidRequest_success() throws JsonProcessingException {
	 try{
	 //Given
	 addTempUsers();

	 mockMvc.perform(MockMvcRequestBuilders
	 .patch(STR."\{API_URL}/\{randomUser.getUserId()}/password")
	 .contentType(MediaType.APPLICATION_JSON_VALUE)
	 .content(buildUpdatePasswordBodyRequest(randomUser, null)))
	 .andExpect(MockMvcResultMatchers.status().isOk());
	 }catch(Exception e){
	 	fail(e.getMessage());
	 }
	 finally {
	 removeTempUsers();
	 }
	 }

	@Test
	void updatePassword_422OldPasswordIncorrect_fail() throws JsonProcessingException {
		try{
			//Given
			addTempUsers();
			randomUser.setPassword("121123");

			mockMvc.perform(MockMvcRequestBuilders
							.patch(STR."\{API_URL}/\{randomUser.getUserId()}/password")
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.content(buildUpdatePasswordBodyRequest(randomUser, null)))
					.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
					.andExpect(MockMvcResultMatchers.jsonPath("message").value(StatusMessage.WRONG_PASSWORD));
		}catch(Exception e){
			fail(e.getMessage());
		}
		finally {
			removeTempUsers();
		}
	}

	@Test
	void updatePassword_400OldPasswordNotEnoughCharacter_fail() {
		try{
			//Given
			addTempUsers();
			randomUser.setPassword("123");

			mockMvc.perform(MockMvcRequestBuilders
							.patch(STR."\{API_URL}/\{randomUser.getUserId()}/password")
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.content(buildUpdatePasswordBodyRequest(randomUser, null)))
					.andExpect(MockMvcResultMatchers.status().isBadRequest())
					.andExpect(MockMvcResultMatchers.jsonPath("message").value(StatusMessage.PASSWORD_MINIMUM));
		}catch(Exception e){
			fail(e.getMessage());
		}
		finally {
			removeTempUsers();
		}
	}

	@Test
	void updatePassword_400NewPasswordNotEnoughCharacter_fail() throws JsonProcessingException {
		try{
			//Given
			addTempUsers();

			mockMvc.perform(MockMvcRequestBuilders
							.patch(STR."\{API_URL}/\{randomUser.getUserId()}/password")
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.content(buildUpdatePasswordBodyRequest(randomUser, "123")))
					.andExpect(MockMvcResultMatchers.status().isBadRequest())
					.andExpect(MockMvcResultMatchers.jsonPath("message").value(StatusMessage.PASSWORD_MINIMUM));
		}catch(Exception e){
			fail(e.getMessage());
		}
		finally {
			removeTempUsers();
		}
	}

	@Test
	void signup_200ValidRequest_success() throws JsonProcessingException {
		// Start snowflake-service to do test this
		String newEmail = "r+@example.com";
		try {
			// Given
			addTempUsers();

			randomUser.setEmail(newEmail);
			randomUser.setUsername("Jimmy");

			mockMvc
				.perform(MockMvcRequestBuilders.post(SIGNUP_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(buildSignupUserBodyRequest(randomUser)))
				.andExpect(MockMvcResultMatchers.status().isOk());
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		finally {
			userService.deleteUserByEmail(newEmail);
			removeTempUsers();
		}
	}

	@Test
	void signup_409EmailAlreadyBeenUsed_fail() throws JsonProcessingException {
		try {
			// Given
			addTempUsers();

			mockMvc
				.perform(MockMvcRequestBuilders.post(SIGNUP_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(buildSignupUserBodyRequest(randomUser)))
				.andExpect(MockMvcResultMatchers.status().isConflict())
				.andExpect(MockMvcResultMatchers.jsonPath("message").value(StatusMessage.EMAIL_EXISTED));
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		finally {
			removeTempUsers();
		}
	}

	@Test
	void signup_400InvalidEmail_fail() throws JsonProcessingException {
		try {
			// Given
			addTempUsers();
			randomUser.setEmail("123gmail.com");

			mockMvc
				.perform(MockMvcRequestBuilders.post(SIGNUP_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(buildSignupUserBodyRequest(randomUser)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("message").value(StatusMessage.EMAIL_INVALID));
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		finally {
			removeTempUsers();
		}
	}

	@Test
	void signup_400InvalidBirthDate_fail() throws JsonProcessingException {
		try {
			// Given
			addTempUsers();
			randomUser.setBirthday(LocalDate.of(2999, 2, 1));
			mockMvc
				.perform(MockMvcRequestBuilders.post(SIGNUP_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(buildSignupUserBodyRequest(randomUser)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("message").value(StatusMessage.BIRTH_DATE_INVALID));
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		finally {
			removeTempUsers();
		}
	}

	@Test
	void signup_400PasswordNotEnoughCharacter_fail() throws JsonProcessingException {
		try {
			// Given
			addTempUsers();
			randomUser.setPassword("123");

			mockMvc
				.perform(MockMvcRequestBuilders.post(SIGNUP_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(buildSignupUserBodyRequest(randomUser)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("message").value(StatusMessage.PASSWORD_MINIMUM));
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		finally {
			removeTempUsers();
		}
	}

}
