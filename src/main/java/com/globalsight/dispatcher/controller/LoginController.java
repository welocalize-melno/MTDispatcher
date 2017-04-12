package com.globalsight.dispatcher.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.globalsight.dispatcher.bo.Account;
import com.globalsight.dispatcher.bo.AppConstants;
import com.globalsight.dispatcher.dao.AccountDAO;
import com.globalsight.dispatcher.dao.DispatcherDAOFactory;
import com.globalsight.dispatcher.util.PasswordUtils;

@Controller
@RequestMapping("/login")
public class LoginController implements AppConstants {
    private static final Logger logger = Logger.getLogger(LoginController.class);
    AccountDAO accountDAO = DispatcherDAOFactory.getAccountDAO();
    @RequestMapping(value = "/main",method = RequestMethod.POST)
    public String validate(@RequestParam Map<String, String> p_reqMap, HttpServletRequest p_request, ModelMap p_model)
    {
        HttpSession session = p_request.getSession();
        p_model.put("gAdministrator", AppConstants.USER_TYPE_ADMIN);
        Account account = accountDAO.getAccountByAccountName(p_reqMap.get("accountName"));
        if(null == account) {
            p_model.addAttribute("error", "<br/><p>Invalid User Name or password!</p>");
            return "login";
        }
        String password = p_reqMap.get("password");
        if(null == password || 0 == password.length() || !PasswordUtils.md5(password).equals(account.getPassword())) {
            p_model.addAttribute("error", "<br/><p>Invalid User Name or password!</p>");
            return "login";
        }
        p_model.put("theAccount", account);
        session.setAttribute("theAccount",account);
        logger.info("Direct Login");
        return "home";
    }
    
    @RequestMapping(value = "/adminLogin",method = RequestMethod.GET,produces = "text/html")
  	public String displayLogin(HttpServletRequest request, HttpServletResponse response)
  	{
  		return "adminLogin";
  	} 
    
    
}
