/**
 *  Copyright 2014 Welocalize, Inc. 
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  
 *  You may obtain a copy of the License at 
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  
 */
package com.globalsight.dispatcher.controller;

import java.io.FileNotFoundException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;

import com.globalsight.dispatcher.util.PasswordUtils;
import com.globalsight.dispatcher.util.SessionUtil;
import com.globalsight.dispatcher.util.StringUtils;

//import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
//import org.json.JSONException;
//import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.globalsight.dispatcher.bo.Account;
import com.globalsight.dispatcher.bo.AppConstants;
import com.globalsight.dispatcher.dao.AccountDAO;
import com.globalsight.dispatcher.dao.DispatcherDAOFactory;
import com.globalsight.dispatcher.dao.MTPLanguagesDAO;

/**
 * Dispatcher Controller for 'Account' Pages
 * 
 * @author Joey
 *
 */
@Controller
@RequestMapping("/account")
public class AccountController implements AppConstants
{
    private static final Logger logger = Logger.getLogger(AccountController.class);
    private static final String INVALID_PASSWORD = "Paasword's minimum length is 7 characters, maximum length is 25 characters, and HTML special characters are not allowed.";
    //final int securityCodeLength = 20;
    AccountDAO accountDAO = DispatcherDAOFactory.getAccountDAO();
    MTPLanguagesDAO langDAO = DispatcherDAOFactory.getMTPLanguagesDAO();
    
    @RequestMapping(value = "/main")
    public String listAll(HttpServletRequest p_request, ModelMap p_model)
    {
        HttpSession session = p_request.getSession(false);
        if(!SessionUtil.validatePermission(session, true)) {
            return "../error/401";
        }
        p_model.put("allAccounts", accountDAO.getAllAccounts());
        p_model.put("gAdministrator", AppConstants.USER_TYPE_ADMIN);
        p_model.put("theAccount", (Account)session.getAttribute("theAccount"));
        return "accountMain";
    }

    @RequestMapping(value = "/viewDetail", method = RequestMethod.POST)
    public String viewDetail(@RequestParam Map<String, String> p_reqMap, HttpServletRequest p_request, ModelMap p_model)
            throws FileNotFoundException, JAXBException
    {

        HttpSession session = p_request.getSession(false);
        if(!SessionUtil.validatePermission(session, true)) {
            return "../error/401";
        }

        Account account = null;
        String idStr = p_reqMap.get("selectedID");
        if (idStr != null)
        {
            long id = Long.valueOf(idStr);
            if (id >= 0)
                account = accountDAO.getAccount(id);
        }
        
        if (account == null)
        {
            account = new Account();
            //TODO
            //data.setSecurityCode(RandomStringUtils.randomAlphabetic(securityCodeLength));
        }

        p_model.put("account", account);
        p_model.put("globalAdmin", AppConstants.GADMIN_USER_NAME);
        p_model.put("gAdministrator", AppConstants.USER_TYPE_ADMIN);
        p_model.put("ssoUser", "Yes");

        return "accountDetail";
    }
    
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public String saveOrUpdate(@RequestParam Map<String, String> p_reqMap, HttpServletRequest p_request, ModelMap p_model)
            throws FileNotFoundException, JAXBException
    {
        HttpSession session = p_request.getSession(false);
        if(!SessionUtil.validatePermission(session, true)) {
            return "../error/401";
        }

        Account theAccount = (Account)session.getAttribute("theAccount");
        Account account = null;
        String accountIDStr = p_reqMap.get("accountId");
        String accountName = p_reqMap.get("accountName");
        String accountFullName = p_reqMap.get("fullName");
        String password = (String)p_reqMap.get("password");
        String type = p_reqMap.get("type");
        String eMail = p_reqMap.get("email");
        String description = p_reqMap.get("description");
        String userType = "No";
        boolean newAccount = false;
        String oldType = null;
        if(accountIDStr == null || accountIDStr.equals("-1"))
        {
            account = new Account();
            newAccount = true;
            account.setSsoUser(userType);
            account.setPassword(new String());
        }
        else
        {
            account = new Account(accountDAO.getAccount(Long.valueOf(accountIDStr)));
            oldType = account.getType();
            userType = (GADMIN_USER_NAME.equalsIgnoreCase(accountName))?"No":p_reqMap.get("userType");
            account.setSsoUser(userType);
        }
        account.setAccountName(accountName);
        account.setFullName(accountFullName);
        
        if(userType.equalsIgnoreCase("No") && (StringUtils.isNull(password) || password.isEmpty())){
        	logger.debug("Normal user/Admin");
        	p_model.addAttribute("error", "Password should not be empty.");
        }
        if((newAccount || (!account.getPassword().equals(password))) && userType.equalsIgnoreCase("No")) {
            if(!PasswordUtils.isValidPassword(password)) {
                p_model.addAttribute("error", INVALID_PASSWORD);
                return "accountDetail";
            }
            account.setPassword(PasswordUtils.md5(password));
        }
        else{
        	//for global admin and sso users
        	account.setPassword(password.isEmpty()?new String():password);
        }
        account.setEmail(eMail);
        account.setDescription(description);

        if (newAccount && isExistAccountName(account))
        {
            p_model.addAttribute("error", "Account name already exists.");
        }
        else if(USER_TYPE_ADMIN.equals(oldType) &&
                (USER_TYPE_USER.equals(type) ) &&
                (GADMIN_USER_NAME.equalsIgnoreCase(accountName) || theAccount.getAccountName().equalsIgnoreCase(accountName)) 
                ) {
            p_model.addAttribute("error", "Administrator's account for current user and Global Admin can't be downgraded.");
        }
        else
        {
            account.setType(type);
            accountDAO.saveOrUpdateAccount(account);
            if(account.getId() == theAccount.getId()) {
                p_model.put("theAccount", account);
                session.setAttribute("theAccount",account);
            }
        }  
        return "accountDetail";
    }
    
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String remove(HttpServletRequest p_request, ModelMap p_model) throws FileNotFoundException,
            JAXBException
    {
        HttpSession session = p_request.getSession(false);
        if(!SessionUtil.validatePermission(session, true)) {
            return "../error/401";
        }

        Account theAccount = (Account)session.getAttribute("theAccount");
        String SelectedIDArr = p_request.getParameter("selectedIDS");
        for(String idStr : SelectedIDArr.split(","))
        {
            long accountID = Long.valueOf(idStr);
            if (accountID < 0)
                continue;
            Account account = accountDAO.getAccount(accountID);
           
            if(account.getId() == theAccount.getId()) {
                p_model.addAttribute("error",
                        "User can't delete his own account.");
                return "";
            }
            if(GADMIN_USER_NAME.equalsIgnoreCase(account.getAccountName())) {
                p_model.addAttribute("error",
                        "Global Admin's account can't be deleted");
                return "";
            }


            accountDAO.deleteAccount(accountID);
        }

        return "main.htm";
    }

    /*
    @RequestMapping(value = "/getRandom", method = RequestMethod.GET)
    public void getRandom(HttpServletRequest p_request, HttpServletResponse p_response) throws IOException, JSONException
    { 
        JSONObject obj = new JSONObject();
        String securityCode = RandomStringUtils.randomAlphabetic(securityCodeLength);        
        obj.put("securityCode", securityCode);
        obj.put("length", securityCodeLength);
        logger.info("Generate Security Code: " + obj.toString());
        p_response.getWriter().write(obj.toString());
    }*/
    
    private boolean isExistAccountName(Account p_account)
    {
        Account account = accountDAO.getAccountByAccountName(p_account.getAccountName());        
        return (account != null && account.getId() != p_account.getId());
    }
}