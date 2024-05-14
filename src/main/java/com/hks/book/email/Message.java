package com.hks.book.email;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class Message {

	private final String messageUser = "Welcome to our site\n"
			+ "You can write alot of blogs in our site and see all users" + "We wish you all the best\n"
			+ "Thank you for registering on our site";
	private final String subject = "Welcome to book social network online";
	private final String from = "artgallery623@gmail.com";


}
