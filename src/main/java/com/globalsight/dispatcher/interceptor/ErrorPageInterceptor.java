package com.globalsight.dispatcher.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.globalsight.dispatcher.controller.MTProfilesController;

public class ErrorPageInterceptor extends HandlerInterceptorAdapter{
    private static final Logger logger = Logger.getLogger(MTProfilesController.class);

	public boolean preHandle(HttpServletRequest request,
				HttpServletResponse response, Object handler)
		    throws Exception {
		logger.info("interceptor method");
		response.sendError(HttpServletResponse.SC_REQUEST_TIMEOUT);
			return false;
	}
}