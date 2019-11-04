package com.businesscard.exception;

public class BusinessCardNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public BusinessCardNotFoundException(String exception) {
		super(exception);
	}
}
