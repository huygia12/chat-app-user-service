package org.service.user.chat_app_user_service.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.service.user.chat_app_user_service.constants.StatusMessage;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPasswdUpdateDTO {

	@Schema(example = "123456")
	private String oldPassword;

	@Schema(example = "12345@")
	private String newPassword;

}
