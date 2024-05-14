package com.hks.book.exception;

public class RecordNotFound extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RecordNotFound(String msg) {
		super(msg);
	}
}
