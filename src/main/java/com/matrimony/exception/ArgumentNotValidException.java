package com.matrimony.exception;

public class ArgumentNotValidException extends RuntimeException {
	
	public ArgumentNotValidException (String string)
	{
		super(string);
	}

}
