package com.flab.foodrun.web.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor //모든 인자가 존재하는 생성자를 만들어줌
public class ErrorResult {
	private String code;
	private String message;
}
