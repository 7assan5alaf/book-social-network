package com.hks.book.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BorrowedBookResponse {
	private String title;
	private String authorName;
	private String owner;
    private String isbn;
	private double rate;
	private boolean returned;
	private boolean returnApproved;
}
