package com.flab.foodrun.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
public class HomeController {

	@GetMapping("/")
	public String home(
		@SessionAttribute(name = SessionConst.LOGIN_SESSION, required = false) String loginUserId) {

		return loginUserId;
	}
}
