package com.flab.foodrun.web.interceptor;

import com.flab.foodrun.domain.login.exception.UnauthenticatedUserAccessException;
import com.flab.foodrun.web.SessionConst;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @Slf4j: 로깅에 대한 추상 레이어를 제공하는 인터페이스 모음 애노테이션 인터페이스를 사용하면 나중에 로깅 라이브러리를 변경해도 코드의 변경 없이 실행 가능
 */

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
		Object handler) {

		log.info(":::LoginInterceptor start:::");

		HttpSession httpSession = request.getSession();
		String method = request.getMethod();

		if (isNullUserSession(httpSession) && !HttpMethod.POST.matches(method)) {
			throw new UnauthenticatedUserAccessException();
		}
		return true;
	}

	private boolean isNullUserSession(HttpSession httpSession) {
		return httpSession == null || httpSession.getAttribute(SessionConst.LOGIN_SESSION) == null;
	}
}
