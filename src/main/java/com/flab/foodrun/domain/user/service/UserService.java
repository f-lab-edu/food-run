package com.flab.foodrun.domain.user.service;

import com.flab.foodrun.domain.user.User;
import com.flab.foodrun.web.user.form.UserSaveForm;

public interface UserService {
	User addUser(UserSaveForm form);
}
