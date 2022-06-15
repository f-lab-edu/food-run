package com.flab.foodrun.web.exceptionhandler;

import lombok.AllArgsConstructor;

@AllArgsConstructor //모든 인자가 존재하는 생성자를 만들어줌
public class ErrorResult {
	public String code;
	public String message;
}
