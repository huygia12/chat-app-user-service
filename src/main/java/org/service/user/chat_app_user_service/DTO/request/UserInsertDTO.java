package org.service.user.chat_app_user_service.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.service.user.chat_app_user_service.constants.Gender;
import org.service.user.chat_app_user_service.utils.ValueOfEnum;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInsertDTO {

	static final String EMPTY_CHECKING_PATTERN = "^(?!\s*$).+";

	@Schema(example = "abc@gmail.com")
	@Email(message = "Email is not valid")
	@NotNull
	@Pattern(regexp = EMPTY_CHECKING_PATTERN, message = "email is mandatory")
	private String email;

	@Schema(example = "123456")
	@NotNull
	@Min(6)
	@Pattern(regexp = EMPTY_CHECKING_PATTERN, message = "password is mandatory")
	private String password;

	@Schema(example = "Nguyen van A")
	@NotNull
	@Pattern(regexp = EMPTY_CHECKING_PATTERN, message = "username is mandatory")
	private String username;

	@Schema(example = "Nguyen")
	@NotNull
	@Pattern(regexp = EMPTY_CHECKING_PATTERN, message = "firstName is mandatory")
	private String firstName;

	@Schema(example = "A")
	@NotNull
	@Pattern(regexp = EMPTY_CHECKING_PATTERN, message = "lastName is mandatory")
	private String lastName;

	@Schema(example = "2004-07-14")
	@NotNull
	@Past(message = "Date of birth must be a day in the past")
	private LocalDate birthday;

	@Schema(example = "MALE")
	@ValueOfEnum(enumClass = Gender.class, message = "Gender invalid")
	@NotNull
	@Pattern(regexp = EMPTY_CHECKING_PATTERN, message = "gender is mandatory")
	private String gender;

	@Schema(example = "0506777333")
	@NotNull
	@Pattern(regexp = EMPTY_CHECKING_PATTERN, message = "phoneNumber is mandatory")
	private String phoneNumber;

	@Schema(example = "avt.jpg")
	@Pattern(regexp = EMPTY_CHECKING_PATTERN, message = "avatarUrl is mandatory")
	private String avatarUrl;

}
