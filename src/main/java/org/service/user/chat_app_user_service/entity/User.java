package org.service.user.chat_app_user_service.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.service.user.chat_app_user_service.constants.Gender;
import org.service.user.chat_app_user_service.constants.Role;
import org.service.user.chat_app_user_service.constants.UserStatus;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User implements Cloneable {

	@Id
	@JsonAlias({ "id" })
	@Column(name = "id")
	private BigInteger userId;

	@Column(nullable = false, unique = true, columnDefinition = "text")
	private String email;

	@Column(nullable = false, unique = true, columnDefinition = "text")
	private String username;

	@Column(nullable = false, columnDefinition = "text")
	private String password;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@JdbcType(PostgreSQLEnumJdbcType.class)
	private Role role;

	@Column(name = "first_name", nullable = true, columnDefinition = "text")
	@JsonAlias({ "first_name" })
	private String firstName;

	@Column(name = "last_name", nullable = true, columnDefinition = "text")
	@JsonAlias({ "last_name" })
	private String lastName;

	@Column(nullable = true)
	private LocalDate birthday;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@JdbcType(PostgreSQLEnumJdbcType.class)
	private Gender gender;

	@Column(name = "phone_number", nullable = true, columnDefinition = "text")
	@JsonAlias({ "phone_number" })
	private String phoneNumber;

	@Column(nullable = true, columnDefinition = "text")
	private String privacy;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@JdbcType(PostgreSQLEnumJdbcType.class)
	private UserStatus status;

	@Column(name = "last_active_at", nullable = false)
	@JsonAlias({ "last_active_at" })
	@Builder.Default
	private LocalDateTime lastActiveAt = LocalDateTime.now();

	@Column(name = "avatar_url", nullable = true, columnDefinition = "text")
	@JsonAlias({ "avatar_url" })
	private String avatarUrl;

	@Column(name = "refresh_tokens", nullable = false, columnDefinition = "text[]")
	@JsonAlias({ "refresh_tokens" })
	@Builder.Default
	private String[] refreshTokens = new String[0];

	@Column(name = "created_at", nullable = false)
	@JsonAlias({ "created_at" })
	@Builder.Default
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = "updated_at", nullable = true)
	@JsonAlias({ "updated_at" })
	private LocalDateTime updatedAt;

	@Column(name = "deleted_at", nullable = true)
	@JsonAlias({ "deleted_at" })
	private LocalDateTime deletedAt;

	@Override
	public User clone() {
		try {
			return (User) super.clone();
		}
		catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}

}
