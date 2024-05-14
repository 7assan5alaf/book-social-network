package com.hks.book.response;

import java.util.List;

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
public class PageResponse<T> {
	
	private List<T>content;
	private int number;
	private int size;
	private Long totalElement;
	private int totalPage;
	private boolean first;
	private boolean last;

}
