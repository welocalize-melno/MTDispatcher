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
import com.globalsight.dispatcher.util.StringUtils;


@Controller
@RequestMapping("/")
public class SingleSignOnController implements AppConstants
{
    private static final Logger logger = Logger.getLogger(SingleSignOnController.class);
    AccountDAO accountDAO = DispatcherDAOFactory.getAccountDAO();
    
    @RequestMapping(value = "/",method = {RequestMethod.POST,RequestMethod.GET},produces = "text/html")
    public String validate(@RequestParam Map<String, String> p_reqMap, HttpServletRequest p_request, ModelMap p_model)throws  JAXBException
    {
        String screenName = (String)p_request.getHeader("ajp_saml-screenName");
        Account account = null;
        if(null != screenName){ // SSO login
        	logger.info("Screen Name: "+ screenName);
        	account = accountDAO.getAccountByAccountName(screenName.toString());
        }  
        else{ // Direct login
        	return "login";
        }
       
        if(null == account) {
        		logger.info("Account object is null");
        		account = new Account();
        		account.setType("User");
        }
        
        String accountName = screenName;
        String accountFullName = (StringUtils.isNull(p_request.getHeader("ajp_saml-firstName"))?"":(String)p_request.getHeader("ajp_saml-firstName")+" ")+(String)p_request.getHeader("ajp_saml-lastName");
        String eMail = (String)p_request.getHeader("ajp_saml-emailAddress");

        account.setAccountName(accountName);
        account.setFullName(accountFullName);            
        account.setPassword(new String());             
        account.setEmail(eMail);
        account.setDescription(null);         
        account.setSsoUser("Yes");   
        
        //Update for each user login. As some times we will get the updated fname and lname and email address. 
        accountDAO.saveOrUpdateAccount(account);
         
        p_model.put("theAccount", account);
        HttpSession session = p_request.getSession();
        session.setAttribute("theAccount",account);
        
        logger.info("SSO login "+account);
        //Variable to check the user type in home page.
        p_model.put("gAdministrator", AppConstants.USER_TYPE_ADMIN);
        return "homeSso";
    }
    
}