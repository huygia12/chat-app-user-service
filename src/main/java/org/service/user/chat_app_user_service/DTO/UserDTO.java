package org.service.user.chat_app_user_service.DTO;

import lombok.Builder;

import org.service.user.chat_app_user_service.constants.Gender;
import org.service.user.chat_app_user_service.constants.Role;
import org.service.user.chat_app_user_service.constants.UserStatus;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record UserDTO(BigInteger userId, String email, String username, String firstName, String lastName,
		LocalDate birthday, String gender, String role, String phoneNumber, String privacy, String status,
		LocalDateTime lastActiveAt, String avatarUrl, LocalDateTime createdAt, LocalDateTime updatedAt,
		LocalDateTime deletedAt) {
}
