package com.hks.book.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FeedbackRequest(
		@Positive(message = "201")
		@Min(value = 0,message = "201")
		@Max(value = 5,message = "202")
		Double note,
		@NotBlank(message = "203")
		@NotNull(message = "203")
		@NotEmpty(message = "203")
		String comment,
		@NotNull(message = "204")
		Long bookId
		
		){

}
