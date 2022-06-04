package com.flab.foodrun.web.user;

import com.flab.foodrun.domain.user.dao.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserMapper userMapper;

	@GetMapping("/add")
	public String addForm(){
		return "GET:addForm";
	}

	@PostMapping("/add")
	public String addUser(){
		return "POST:addUser";
	}
}
