package com.hks.book.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@Builder
public class BookRequest {
	@NotEmpty(message = "100")
	@NotNull(message = "100")
	private String title;
	@NotEmpty(message = "101")
	@NotNull(message = "101")
	private String authorName;
	@NotEmpty(message = "102")
	@NotNull(message = "102")
	private String isbn;
	@NotEmpty(message = "103")
	@NotNull(message = "103")
	private String synopsis;		
    private boolean shareable;
}
