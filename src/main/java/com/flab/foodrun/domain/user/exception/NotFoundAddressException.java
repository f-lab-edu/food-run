package com.flab.foodrun.domain.user.exception;

public class NotFoundAddressException extends RuntimeException {

	public NotFoundAddressException() {
		super();
	}

	public NotFoundAddressException(String message) {
		super(message);
	}

	public NotFoundAddressException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundAddressException(Throwable cause) {
		super(cause);
	}
}
