package org.service.user.chat_app_user_service.utils;

import jakarta.persistence.AttributeConverter;
import org.service.user.chat_app_user_service.constants.Role;

import java.util.stream.Stream;

public class RoleConverter implements AttributeConverter<Role, String> {

	@Override
	public String convertToDatabaseColumn(Role role) {
		if (role == null) {
			return null;
		}

		return role.getName();
	}

	@Override
	public Role convertToEntityAttribute(String name) {
		if (name == null) {
			return null;
		}

		return Stream.of(Role.values())
			.filter(role -> role.getName().equals(name))
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}

}
