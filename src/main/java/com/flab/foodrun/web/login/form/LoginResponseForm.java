package com.flab.foodrun.web.login.form;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseForm {

	private String loginId;
	private String name;
	private String phoneNumber;
}
