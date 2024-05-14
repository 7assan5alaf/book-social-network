package com.hks.book.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class RegistrationRequest {

	@NotEmpty(message = "firstName is mandatory")
	@NotBlank(message = "firstName is mandatory")
	private String firstName;
	@NotEmpty(message = "lasttName is mandatory")
	@NotBlank(message = "lasttName is mandatory")
	private String lastName;
	@NotEmpty(message = "email is mandatory")
	@NotBlank(message = "email is mandatory")
	@Email(message = "Email is not valid")
	private String email;
	@NotEmpty(message = "password is mandatory")
	@NotBlank(message = "password is mandatory")
	@Size(min = 8,message = "please should be 8 characters long minimum")
	private String password;
	@NotEmpty(message = "Role is mandatory")
	@NotBlank(message = "Role is mandatory")
	private String roleName;
}
