package com.flab.foodrun.web.login.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Getter : getter 메서드 자동 생성 애노테이션
 * @AllArgsConstructor : 모든 필드를 받는 생성자를 생성함
 * @NotBlank : null 값이 되면 안되고, 공백이 아닌 문자를 포함해야 한다
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

	@NotBlank
	private String loginId;

	@NotBlank
	private String password;
}


