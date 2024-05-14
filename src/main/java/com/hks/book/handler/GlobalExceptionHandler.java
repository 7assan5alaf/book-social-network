package com.hks.book.handler;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hks.book.exception.OperationPermittedException;
import com.hks.book.exception.RecordNotFound;
import com.hks.book.response.ExceptionResponse;

import jakarta.mail.MessagingException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(LockedException.class)
	public ResponseEntity<ExceptionResponse> handleException(LockedException exp) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(ExceptionResponse.builder().bussniessErrorCode(BussniessErrorCodes.ACCOUNT_LOCKED.getCode())
						.bussniessErrorDescription(BussniessErrorCodes.ACCOUNT_LOCKED.getDescription())
						.error(exp.getMessage()).build());

	}

	
	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<ExceptionResponse> handleException(DisabledException exp) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(ExceptionResponse.builder().bussniessErrorCode(BussniessErrorCodes.ACCOUNT_DISABLED.getCode())
						.bussniessErrorDescription(BussniessErrorCodes.ACCOUNT_DISABLED.getDescription())
						.error(exp.getMessage()).build());

	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException exp) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(ExceptionResponse.builder().bussniessErrorCode(BussniessErrorCodes.BAD_CREDENTIALS.getCode())
						.bussniessErrorDescription(BussniessErrorCodes.BAD_CREDENTIALS.getDescription())
						.error("Invalid email or password").build());

	}

	@ExceptionHandler(MessagingException.class)
	public ResponseEntity<ExceptionResponse> handleException(MessagingException exp) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ExceptionResponse.builder().error(exp.getMessage()).build());

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException exp) {
		Set<String> errors = new HashSet<>();
		exp.getBindingResult().getAllErrors().forEach(e -> errors.add(e.getDefaultMessage()));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ExceptionResponse.builder().validationError(errors).build());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleException(Exception exp) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionResponse.builder()
				.bussniessErrorDescription("Internal error, please contact the admin").error(exp.getMessage()).build());
	}

	@ExceptionHandler(RecordNotFound.class)
	public ResponseEntity<ExceptionResponse> handleException(RecordNotFound exp) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionResponse.builder()
				.error(exp.getMessage()).build());
	}
	@ExceptionHandler(OperationPermittedException.class)
	public ResponseEntity<ExceptionResponse> handleException(OperationPermittedException exp) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder()
				.error(exp.getMessage()).build());
	}

}
