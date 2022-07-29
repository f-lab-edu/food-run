package com.flab.foodrun.web.exceptionhandler.advice;

import com.flab.foodrun.domain.login.exception.InvalidPasswordException;
import com.flab.foodrun.domain.login.exception.LoginIdNotFoundException;
import com.flab.foodrun.domain.login.exception.UnauthenticatedUserAccessException;
import com.flab.foodrun.domain.user.exception.DuplicatedUserIdException;
import com.flab.foodrun.domain.user.exception.NotFoundAddressException;
import com.flab.foodrun.web.exceptionhandler.ErrorResult;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 로깅에 대한 추상 레이어를 제공하는 인터페이스 모음 애노테이션 인터페이스를 사용하면 나중에 로깅 라이브러리를 변경해도 코드의 변경 없이 실행 가능
 */
@Slf4j
@RestControllerAdvice // @ResponseBody + @ControllerAdvice
public class WebExceptionControllerAdvice {

	public static final String DUPLICATED_USER_ID_EX_MESSAGE = "이미 존재하는 회원입니다.";
	public static final String LOGIN_ID_NOT_FOUND_EX_MESSAGE = "아이디를 찾을 수 없습니다.";
	public static final String INVALID_PASSWORD_EX_MESSAGE = "비밀번호가 일치하지 않습니다.";
	public static final String UNAUTHENTICATED_USER_EX_MESSAGE = "허가되지 않은 사용자입니다.";
	public static final String NOT_FOUND_ADDRESS_EX_MESSAGE = "해당 주소를 찾을 수 없습니다.";

	@ExceptionHandler // Controller 계층에서 발생하는 에러를 잡아주는 기능을 가진 애노테이션
	public ResponseEntity<ErrorResult> bindFieldErrorExceptionHandler(BindException e) {
		ErrorResult errorResult = new ErrorResult("FieldErrorException",
			String.valueOf(Objects.requireNonNull(e.getFieldError()).getDefaultMessage()));
		return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler // Controller 계층에서 발생하는 에러를 잡아주는 기능을 가진 애노테이션
	public ResponseEntity<ErrorResult> duplicatedUserIdExceptionHandler(
		DuplicatedUserIdException e) {
		ErrorResult errorResult = new ErrorResult("DuplicatedUserIdException",
			DUPLICATED_USER_ID_EX_MESSAGE);
		return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResult> loginIdNotFoundException(LoginIdNotFoundException e) {
		ErrorResult errorResult = new ErrorResult("LoginIdNotFoundException",
			LOGIN_ID_NOT_FOUND_EX_MESSAGE);
		return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResult> invalidPasswordException(InvalidPasswordException e) {
		ErrorResult errorResult = new ErrorResult("InvalidPasswordException",
			INVALID_PASSWORD_EX_MESSAGE);
		return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResult> unauthenticatedUserAccess(
		UnauthenticatedUserAccessException e) {

		ErrorResult errorResult = new ErrorResult("UnauthenticatedUserAccessException",
			UNAUTHENTICATED_USER_EX_MESSAGE);
		return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResult> notFoundAddressException(NotFoundAddressException e) {
		ErrorResult errorResult = new ErrorResult("NotFoundAddressException",
			NOT_FOUND_ADDRESS_EX_MESSAGE);
		return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
	}
}
