package org.service.user.chat_app_user_service.configurations;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Profile;

@OpenAPIDefinition(
		info = @Info(contact = @Contact(name = "Dzung", email = "luudung0806@gmail.com"),
				description = "Open api documentation for char-app-user-service", title = "User service apis",
				license = @License(name = "Api license"), termsOfService = "Terms of service"),
		tags = { @Tag(name = "User Management", description = "edit, delete and get users information") })
@Configuration
public class OpenAPIConfiguration {

	public GroupedOpenApi groupedOpenApi() {
		return GroupedOpenApi.builder()
			.group("user-service-api")
			.packagesToScan("org.service.user.chat_app_user_service.controller", "org.springframework.boot")
			.build();
	}

}
