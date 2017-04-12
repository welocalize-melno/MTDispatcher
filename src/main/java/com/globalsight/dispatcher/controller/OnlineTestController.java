/**
 *  Copyright 2013 Welocalize, Inc. 
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

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.globalsight.dispatcher.bo.Account;
import com.globalsight.dispatcher.bo.AppConstants;
import com.globalsight.dispatcher.bo.GlobalSightLocale;
import com.globalsight.dispatcher.dao.AccountDAO;
import com.globalsight.dispatcher.dao.CommonDAO;
import com.globalsight.dispatcher.dao.DispatcherDAOFactory;
import com.globalsight.dispatcher.dao.MTPLanguagesDAO;
import com.globalsight.dispatcher.dao.MTProfilesDAO;
import com.globalsight.dispatcher.util.SessionUtil;
import com.globalsight.machineTranslation.MTHelper;

/**
 * Dispatcher Controller for 'Test' Pages
 * 
 * @author Joey
 *
 */
@Controller
@RequestMapping("/onlineTest")
public class OnlineTestController implements AppConstants{

    private static final Logger logger = Logger.getLogger(OnlineTestController.class);
    AccountDAO accountDAO = DispatcherDAOFactory.getAccountDAO();
    
    @Autowired
    private MTProfilesDAO mtProfilesDAO;
    @Autowired
    private MTPLanguagesDAO mtpLangDAO;
    
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String listAllMTProfiles(HttpServletRequest p_request, ModelMap p_model)
    {
        HttpSession session = p_request.getSession(false);
        p_model.put("gAdministrator", AppConstants.USER_TYPE_ADMIN);
        if(!SessionUtil.validateSession(session)) {
            return "../error/408";
        }

        StringBuffer basicURl = p_request.getRequestURL();
        Account theAccount = (Account)session.getAttribute("theAccount");
        p_model.put("basicURL", basicURl.substring(0, basicURl.indexOf("/onlineTest")));
        p_model.put("allGlobalSightLocale", CommonDAO.getAllGlobalSightLocale());
        p_model.put("langCodes", MTHelper.getMTConfig("language.codes"));
        p_model.put("theAccount", theAccount);
        return "onlineTestMain";
    }
    
    
    /*
     * function to get language pairs for the selected source or target
     * @param HttpServletRequest object(p_request)
     * @param ModelMap object(p_model)
     * 
     * return String
     */
    
    @RequestMapping(value = "/getLocales", method = RequestMethod.POST)
    public String getLocales(HttpServletRequest p_request, ModelMap p_model)
    {
    	logger.info("Method start OnlinTestController - getLocales()");
    	HttpSession session = p_request.getSession(false);
        if(!SessionUtil.validateSession(session)) {
        	logger.info("session timeout from OnlinTestController - getLocales()");
        	p_model.put("status", "timeout");
            return "../error/401";
        }
        
    	p_model.put("gAdministrator", AppConstants.USER_TYPE_ADMIN);
    	String accountID = p_request.getParameter(JSONPN_ACCOUNT_ID);
    	String srcLocaleShortName = p_request.getParameter("selectedLocale");
    	String domain = p_request.getParameter(JSONPN_DOMAIN);
    	long domainId = Long.parseLong(domain);
    	String accountName = accountDAO.getAccountName(accountID);
    	if(null == accountName) {
    		return "onlineTestMain";
    	}
    	Set<GlobalSightLocale> trgGLS = DispatcherDAOFactory.getMTPLanguagesDAO().getLanguagePairsByDomain(domainId,srcLocaleShortName);
    	p_model.put("trgLocales", trgGLS);
    	return "onlineTestMain";
    }
    
    /*
     * function to check feedback button access for selected language pair
     * @param HttpServletRequest object(p_request)
     * @param ModelMap object(p_model)
     * 
     * return String
     */
    
    @RequestMapping(value = "/checkFeedbackAccess", method = RequestMethod.POST)
    public String checkFeedbackAccess(HttpServletRequest p_request, ModelMap p_model)
    {
    	HttpSession session = p_request.getSession(false);
        if(!SessionUtil.validateSession(session)) {
        	logger.info("session timeout from OnlinTestController - checkFeedbackAccess()");
        	p_model.put("status", "timeout");
            return "../error/401";
        }
        
        String accountID =  p_request.getParameter(JSONPN_ACCOUNT_ID);
        String srcLocaleShortName = p_request.getParameter("srcLocale");
        GlobalSightLocale srcGL = CommonDAO.getGlobalSightLocaleByShortName(srcLocaleShortName);
        String trgLocaleShortName = p_request.getParameter("trgLocale");
        GlobalSightLocale trgGL = CommonDAO.getGlobalSightLocaleByShortName(trgLocaleShortName);
        String accountName = accountDAO.getAccountName(accountID);
        if(null == accountName) {
            return "onlineTestMain";
        }
        long domain =  Long.parseLong(p_request.getParameter("domain"));
        String accessStatus = DispatcherDAOFactory.getMTPLanguagesDAO().chkMTProfileCategory(accountName,srcGL,trgGL,domain);       
        p_model.put("accessStatus", accessStatus);        
        return "onlineTestMain";
    }
    /*
     * function to check swap button access for selected language pair
     * @param HttpServletRequest object(p_request)
     * @param ModelMap object(p_model)
     * 
     * return String
     */
    @RequestMapping(value = "/checkSwapBtnAccess", method = RequestMethod.POST)
    public String checkSwapBtnAccess(HttpServletRequest p_request, ModelMap p_model)
    {
    	HttpSession session = p_request.getSession(false);
        p_model.put("gAdministrator", AppConstants.USER_TYPE_ADMIN);
        if(!SessionUtil.validateSession(session)) {
        	logger.info("session timeout from OnlinTestController - checkSwapBtnAccess()");
        	p_model.put("status", "timeout");
            return "../error/401";
        }
        String accountID =  p_request.getParameter(JSONPN_ACCOUNT_ID);
        String domain =  p_request.getParameter(JSONPN_DOMAIN);
        long domainId = Long.parseLong(domain);
        String srcLocaleShortName = p_request.getParameter("srcLocale");
        GlobalSightLocale srcGL = CommonDAO.getGlobalSightLocaleByShortName(srcLocaleShortName);
        String trgLocaleShortName = p_request.getParameter("trgLocale");
        GlobalSightLocale trgGL = CommonDAO.getGlobalSightLocaleByShortName(trgLocaleShortName);
        String accountName = accountDAO.getAccountName(accountID);
        if(null == accountName) {
            return "onlineTestMain";
        }
        String accessStatus = DispatcherDAOFactory.getMTPLanguagesDAO().checkLanguagePair(srcGL,trgGL,domainId);       
        p_model.put("accessStatus", accessStatus);        
        return "onlineTestMain";
    }
    
}