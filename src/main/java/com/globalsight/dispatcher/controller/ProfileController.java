package com.globalsight.dispatcher.controller;
import java.io.FileNotFoundException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;

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
import com.globalsight.dispatcher.util.SessionUtil;



@Controller
@RequestMapping("/profile")
public class ProfileController implements AppConstants {

    private static final Logger logger = Logger.getLogger(ProfileController.class);
    AccountDAO accountDAO = DispatcherDAOFactory.getAccountDAO();

    @RequestMapping(value = "/main")
    public String listAll(HttpServletRequest p_request, ModelMap p_model)
    {
        HttpSession session = p_request.getSession(false);
        p_model.put("gAdministrator", AppConstants.USER_TYPE_ADMIN);
        if(!SessionUtil.validatePermission(session, false)) {
            return "../error/401";
        }
        return "userProfile";
    }

    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public String updateProfile(@RequestParam Map<String, String> p_reqMap, HttpServletRequest p_request, ModelMap p_model)
            throws FileNotFoundException, JAXBException
    {
    		logger.info("update profile info");
        HttpSession session = p_request.getSession(false);
        p_model.put("gAdministrator", AppConstants.USER_TYPE_ADMIN);
        if(!SessionUtil.validatePermission(session, false)) {
            return "../error/401";
        }
        
        Account theAccount = (Account)session.getAttribute("theAccount");
        String accountFullName = p_reqMap.get("fullName");
        String oldpassword = p_reqMap.get("oldpassword");
        String password = p_reqMap.get("password");
        String eMail = p_reqMap.get("email");

        if (password != null && password.length() > 0
                && !theAccount.getPassword().equals(PasswordUtils.md5(oldpassword)))
        {
            p_model.addAttribute("error", "Old password does not match current password on the account");
            return "";
        }
        else
        {
            theAccount.setFullName(accountFullName);
            theAccount.setEmail(eMail);
            if(password != null && password.length() > 0) {
                theAccount.setPassword(PasswordUtils.md5(password));
            }
            accountDAO.saveOrUpdateAccount(theAccount);
        }
        return "home";
    }

}
