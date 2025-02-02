package org.service.user.chat_app_user_service.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.service.user.chat_app_user_service.constants.StatusMessage;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInsertionDTO {

	static final String EMPTY_CHECKING_PATTERN = "^(?!\s*$).+";

	@Schema(example = "abc@gmail.com")
	@Email(message = StatusMessage.EMAIL_INVALID)
	@NotNull
	@Pattern(regexp = EMPTY_CHECKING_PATTERN, message = "email is mandatory")
	private String email;

	@Schema(example = "123456")
	@NotNull
	@Pattern(regexp = EMPTY_CHECKING_PATTERN, message = "password is mandatory")
	private String password;

	@Schema(example = "Nguyen van A")
	@NotNull
	@Pattern(regexp = EMPTY_CHECKING_PATTERN, message = "username is mandatory")
	private String username;

}
