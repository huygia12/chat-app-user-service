package org.service.user.chat_app_user_service.exception.server;

import org.postgresql.util.PSQLException;
import org.service.user.chat_app_user_service.exception.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DatabaseExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(PSQLException exception) {
		if (exception.getMessage().contains("duplicate key value violates unique constraint \"users_email_key\"")) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(new ErrorResponse("Email has already existed", System.currentTimeMillis()));
		}
		return ResponseEntity.internalServerError()
			.body(new ErrorResponse("There is something wrong with the server", System.currentTimeMillis()));
	}

}