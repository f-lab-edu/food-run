package com.flab.foodrun.web.user;

import com.flab.foodrun.domain.user.service.UserService;
import com.flab.foodrun.web.user.form.UserSaveForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/users")
//@Controller, @ResponseBody 가 합쳐진 애노테이션
@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/signup")
	public Object addUser(@Validated @ModelAttribute("user") UserSaveForm form,
		BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			log.info("error={}", bindingResult);
			return bindingResult.getAllErrors();
		}
		return userService.addUser(form);
	}
}
