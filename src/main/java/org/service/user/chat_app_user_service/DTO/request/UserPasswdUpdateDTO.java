package org.service.user.chat_app_user_service.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPasswdUpdateDTO {

	@Schema(example = "123456")
	@Size(min = 6, message = "Old password must contain at least 6 character")
	private String oldPassword;

	@Schema(example = "12345@")
	@Size(min = 6, message = "New password must contain at least 6 character")
	private String newPassword;

}
