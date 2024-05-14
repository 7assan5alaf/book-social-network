package com.hks.book.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hks.book.request.AuthentcatinRequest;
import com.hks.book.request.RegistrationRequest;
import com.hks.book.service.AuthentecationService;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthentecationController {

	private final AuthentecationService authService;
	
	@PostMapping("/register")
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	public ResponseEntity<?>register(@RequestBody @Valid RegistrationRequest request) throws MessagingException{
		authService.register(request);
		return ResponseEntity.accepted().build();
	}
	
	@PostMapping("/signin")
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	public ResponseEntity<?>signIn(@RequestBody @Valid AuthentcatinRequest request){
		return ResponseEntity.ok(authService.signIn(request));
	}
	@PutMapping("/activate/{id}")
	public ResponseEntity<?>acctivationCode(@PathVariable Long id,@RequestParam String code) throws MessagingException{
		return ResponseEntity.ok(authService.acctivateAccount(code));
	}
}
