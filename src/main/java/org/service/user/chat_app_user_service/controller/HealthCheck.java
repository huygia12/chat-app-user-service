package org.service.user.chat_app_user_service.controller;

import org.service.user.chat_app_user_service.constants.StatusMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/healthcheck")
@Tag(name = "Actuator")
public class HealthCheck {

	@Operation(summary = "healthcheck",
			responses = { @ApiResponse(responseCode = "200", description = StatusMessage.SUCCESS) })
	@GetMapping
	public ResponseEntity healthCheck() {
		return ResponseEntity.ok("Healthy");
	};

}
