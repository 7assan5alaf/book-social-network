package com.hks.book.exception;

public class OperationPermittedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OperationPermittedException(String msg) {
		super(msg);
	}
}
