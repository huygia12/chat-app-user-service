package org.service.user.chat_app_user_service.configurations;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.service.user.chat_app_user_service.utils.UserDTOMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Getter
public class AppConfiguration {

	private final String environment;

	private final int serverPort;

	private final int bcryptStrength;

	private final String apiVersion;

	private final Logger logger;

	public AppConfiguration() {
		this.logger = LogManager.getLogger(AppConfiguration.class);
		this.environment = System.getenv("ENVIRONMENT") != null ? System.getenv("EVIRONMENT") : "development";
		this.serverPort = Integer.parseInt(System.getenv("PORT"));
		this.bcryptStrength = System.getenv("BCRYPT_STRENGTH") != null
				? Integer.parseInt(System.getenv("BCRYPT_STRENGTH")) : 12;
		this.apiVersion = System.getenv("API_VERSION");
	}

	@Bean
	public UserDTOMapper userDTOMapper() {
		return new UserDTOMapper();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(this.bcryptStrength);
	}

}
