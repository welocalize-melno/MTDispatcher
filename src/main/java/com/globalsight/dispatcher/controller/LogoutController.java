package com.globalsight.dispatcher.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.globalsight.dispatcher.bo.AppConstants;

@Controller
@RequestMapping("/")
public class LogoutController implements AppConstants {
    private static final Logger logger = Logger.getLogger(LogoutController.class);

    @RequestMapping(value = "/logout")
    public String logout(@RequestParam Map<String, String> p_reqMap, HttpServletRequest p_request, ModelMap p_model){
        p_model.put("gAdministrator", AppConstants.USER_TYPE_ADMIN);
        p_request.getSession(false).invalidate();
        return "login";
    }

}