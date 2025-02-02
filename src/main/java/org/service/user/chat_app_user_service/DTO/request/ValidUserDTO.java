package org.service.user.chat_app_user_service.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
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
public class ValidUserDTO {

	static final String EMPTY_CHECKING_PATTERN = "^(?!\s*$).+";

	@Schema(example = "abc@gmail.com")
	@Email(message = StatusMessage.EMAIL_INVALID)
	@NotNull
	@Pattern(regexp = EMPTY_CHECKING_PATTERN, message = "email is mandatory")
	private String email;

	@Schema(example = "123456")
	@Size(min = 6, message = StatusMessage.PASSWORD_MINIMUM)
	@NotNull
	@Pattern(regexp = EMPTY_CHECKING_PATTERN, message = "password is mandatory")
	private String password;

}
