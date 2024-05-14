package com.hks.book.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hks.book.email.EmailService;
import com.hks.book.email.Message;
import com.hks.book.entity.Token;
import com.hks.book.entity.User;
import com.hks.book.exception.RecordNotFound;
import com.hks.book.repository.RoleRepo;
import com.hks.book.repository.TokenRepo;
import com.hks.book.repository.UserRepo;
import com.hks.book.request.AuthentcatinRequest;
import com.hks.book.request.RegistrationRequest;
import com.hks.book.response.AuthentcatinResponse;
import com.hks.book.security.JwtService;

import jakarta.mail.MessagingException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthentecationService {

	private final UserRepo userRepo;
	private final PasswordEncoder passwordEncoder;
	private final RoleRepo roleRepo;
	private final TokenRepo tokenRepo;
	private final EmailService emailService;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	// sign up user
	public void register(RegistrationRequest request) throws MessagingException {
		var userRole = roleRepo.findByName(request.getRoleName().toUpperCase())
				.orElseThrow(() -> new RecordNotFound("USER ROLE is not found"));

		var user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName())
				.email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).accountLocked(false)
				.enable(false).roles(List.of(userRole)).build();
		userRepo.save(user);
		sendValidationEmail(user);
	}

	// sign in user
	public AuthentcatinResponse signIn(AuthentcatinRequest request) {
		var auth = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		var user = (User) auth.getPrincipal();
		var claims = new HashMap<String, Object>();
		claims.put("fullName", user.getFullName());
		var token = jwtService.generateToken(claims, user);
		return AuthentcatinResponse.builder().token(token).build();
	}

	// activate account by code
	public String acctivateAccount(String code) throws MessagingException {
		Token token = tokenRepo.findByToken(code).orElseThrow(() -> new RecordNotFound("Token not found"));
		if (token.getUser().isEnable() == true)
			return "account is acctivate";
		if (LocalDateTime.now().isAfter(token.getExpiredAt())) {
			sendValidationEmail(token.getUser());
			return "Activation code has expired, A new code has been sent to the same email address";
		}

		if (token.getToken().equals(code)) {
			token.setValidatedAt(LocalDateTime.now());
			token.getUser().setEnable(true);
			userRepo.save(token.getUser());
			tokenRepo.save(token);
			return "acctivation successfully";
		}
		return "code is wrong";
	}

	// send email to user
	private void sendValidationEmail(User user) throws MessagingException {
		Message message = new Message();
		var newToken = generateAndSaveActivationToken(user);
		emailService.sendEmail(user.getEmail(),
				user.getFullName() + "\n" + message.getMessageUser() + "\n" + "\nActivation Code is : " + newToken
						+ "\nTo activate your account click on this link : "
						+ "http://localhost:8081/api/v1/auth/activate/" + user.getId());
	}

	private String generateAndSaveActivationToken(User user) {
		// generate a token
		var generatToken = generateActivationCode(6);
		var token = Token.builder().createdAt(LocalDateTime.now()).token(generatToken)
				.expiredAt(LocalDateTime.now().plusMinutes(15)).user(user).build();
		tokenRepo.save(token);
		return generatToken;
	}

	private String generateActivationCode(int len) {
		String digits = "0123456789";
		SecureRandom random = new SecureRandom();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < len; i++) {
			int randomIndex = random.nextInt(digits.length());
			builder.append(randomIndex);
		}
		return builder.toString();
	}

}
