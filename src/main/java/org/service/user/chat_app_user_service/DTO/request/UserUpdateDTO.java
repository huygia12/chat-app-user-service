package org.service.user.chat_app_user_service.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.service.user.chat_app_user_service.constants.Gender;
import org.service.user.chat_app_user_service.constants.StatusMessage;
import org.service.user.chat_app_user_service.utils.ValueOfEnum;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateDTO {

	static final String EMPTY_CHECKING_PATTERN = "^(?!\s*$).+";

	@Schema(example = "abc@gmail.com")
	@Email(message = StatusMessage.EMAIL_INVALID)
	@Pattern(regexp = EMPTY_CHECKING_PATTERN, message = "email is mandatory")
	private String email;

	@Schema(example = "Nguyen van A")
	@Pattern(regexp = EMPTY_CHECKING_PATTERN, message = "username is mandatory")
	private String username;

	@Schema(example = "Nguyen")
	@Pattern(regexp = EMPTY_CHECKING_PATTERN, message = "firstName is mandatory")
	private String firstName;

	@Schema(example = "A")
	@Pattern(regexp = EMPTY_CHECKING_PATTERN, message = "lastName is mandatory")
	private String lastName;

	@Schema(example = "2004-07-14")
	@Past(message = StatusMessage.BIRTH_DATE_INVALID)
	private LocalDate birthday;

	@Schema(example = "MALE")
	@ValueOfEnum(enumClass = Gender.class, message = StatusMessage.GENDER_INVALID)
	@Pattern(regexp = EMPTY_CHECKING_PATTERN, message = "gender is mandatory")
	private String gender;

	@Schema(example = "0506777333")
	@Pattern(regexp = EMPTY_CHECKING_PATTERN, message = "phoneNumber is mandatory")
	private String phoneNumber;

	@Schema(example = "avt.jpg")
	@Pattern(regexp = EMPTY_CHECKING_PATTERN, message = "avatarUrl is mandatory")
	private String avatarUrl;

}
