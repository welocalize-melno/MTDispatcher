package com.globalsight.dispatcher.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.globalsight.dispatcher.bo.AppConstants;
import com.globalsight.dispatcher.util.SessionUtil;

@Controller
@RequestMapping("/home")
public class HomeController implements AppConstants {
    private static final Logger logger = Logger.getLogger(HomeController.class);

    @RequestMapping(value = "/main")
    public String home(@RequestParam Map<String, String> p_reqMap, HttpServletRequest p_request, ModelMap p_model) {
    	HttpSession session = p_request.getSession(false);
        p_model.put("gAdministrator", AppConstants.USER_TYPE_ADMIN);
        if(!SessionUtil.validateSession(session)){
        	logger.info("session expired");
        		return "../error/408";
        }
        return "home";
    }
    
    @RequestMapping("/error")
    public String error(@RequestParam Map<String, String> map,HttpServletRequest p_request,HttpServletResponse p_response, ModelMap p_model) {
		logger.error("Error page redirection");
		 return  "../error/408";
    }	 
}