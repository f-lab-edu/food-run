package com.flab.foodrun.web.interceptor;

import com.flab.foodrun.domain.login.exception.UnauthenticatedUserAccessException;
import com.flab.foodrun.web.SessionConst;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
		Object handler) {

		HttpSession httpSession = request.getSession();

		if (httpSession == null || httpSession.getAttribute(SessionConst.LOGIN_SESSION) == null) {
			throw new UnauthenticatedUserAccessException();
		}
		return true;
	}
}
