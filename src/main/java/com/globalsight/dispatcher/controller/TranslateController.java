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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.globalsight.dispatcher.bo.Account;
import com.globalsight.dispatcher.bo.AppConstants;
import com.globalsight.dispatcher.bo.GlobalSightLocale;
import com.globalsight.dispatcher.bo.MTPLanguage;
import com.globalsight.dispatcher.bo.MachineTranslationProfile;
import com.globalsight.dispatcher.dao.AccountDAO;
import com.globalsight.dispatcher.dao.CommonDAO;
import com.globalsight.dispatcher.dao.DispatcherDAOFactory;
import com.globalsight.dispatcher.dao.MTPLanguagesDAO;
import com.globalsight.dispatcher.dao.TranslateDAO;
import com.globalsight.dispatcher.model.Translate;
import com.globalsight.dispatcher.util.Mailer;
import com.globalsight.dispatcher.util.SessionUtil;
import com.globalsight.dispatcher.util.StringUtils;
import com.globalsight.dispatcher.util.TranslateUtil;
import com.globalsight.dispatcher.util.Util;
import com.globalsight.machineTranslation.MachineTranslationException;
import com.globalsight.machineTranslation.MachineTranslator;
 

/**
 * Dispatcher Controller for 'RESTful WebService'.
 * 
 * @author Joey
 *
 */
@Controller
@RequestMapping("/translate")
public class TranslateController implements AppConstants {
	
    private static final Logger logger = Logger.getLogger(TranslateController.class);

    private AccountDAO accountDAO = DispatcherDAOFactory.getAccountDAO();
    
    @Autowired
    private TranslateDAO translateDAO;
    
    @Autowired
    private MTPLanguagesDAO mtpLangDAO;
    
    @RequestMapping("/")
    public String doTranslate(@RequestParam Map<String, String> map, HttpServletRequest p_request,ModelMap p_model) {
    	
    	HttpSession session = p_request.getSession(false);
		if (!SessionUtil.validateSession(session)) {
			logger.info("no session found");
			p_model.addAttribute(JSONPN_STATUS, "timeout");
			return "../error/401";
		}
        String sourceLanguage = map.get(JSONPN_SOURCE_LANGUAGE);
        String targetLanguage = map.get(JSONPN_TARGET_LANGUAGE);
        String oriSourceText = map.get(JSONPN_SOURCE_TEXT);
        String accountName = map.get(JSONPN_ACCOUNT_NAME);
		long domainId = Long.parseLong(map.get("domainId")); 
        Account account = accountDAO.getAccountByAccountName(accountName);
        p_model.put("gAdministrator", AppConstants.USER_TYPE_ADMIN);

        Map<String, String> result =
                getTranslatedText(sourceLanguage, targetLanguage, oriSourceText, account, domainId);

        logger.info("After Translation Result Data");
        logger.info("Target Text: "+result.get(JSONPN_TARGET_TEXT));
        logger.info("Status: "+result.get(JSONPN_STATUS));
        logger.info("Error Message: "+result.get(JSONPN_ERROR_MESSAGE));

        p_model.addAttribute(JSONPN_SOURCE_LANGUAGE, sourceLanguage);
        p_model.addAttribute(JSONPN_TARGET_LANGUAGE, targetLanguage);
        p_model.addAttribute(JSONPN_SOURCE_TEXT, oriSourceText);
        p_model.addAttribute(JSONPN_TARGET_TEXT, result.get(JSONPN_TARGET_TEXT));
        p_model.addAttribute(JSONPN_STATUS, result.get(JSONPN_STATUS));
        p_model.addAttribute(JSONPN_ERROR_MESSAGE, result.get(JSONPN_ERROR_MESSAGE));
        
        return "translate";
    }
    
    /**
     * Translate the source text, return translate result Map<String, String>.
     * 
     * @param p_sourceLanguage
     *            Source Locale
     * @param p_targetLanguage
     *             Target Locale
     * @param p_srcText
     *            Source Text
     * @param p_account - Account
     */
    private Map<String, String> getTranslatedText(String p_sourceLanguage, 
            String p_targetLanguage, String p_srcText, Account p_account, long domainId)
    {
        Map<String, String> result = new HashMap<String, String>();
       // MTPLanguagesDAO mtpLangDAO = DispatcherDAOFactory.getMTPLanguagesDAO();
        GlobalSightLocale srcLocale = CommonDAO.getGlobalSightLocaleByShortName(p_sourceLanguage);
        GlobalSightLocale trgLocale = CommonDAO.getGlobalSightLocaleByShortName(p_targetLanguage);
        if (srcLocale == null || trgLocale == null)
        {
            result.put(JSONPN_STATUS, STATUS_FAILED);
            result.put(JSONPN_ERROR_MESSAGE, ERROR_NO_LOCALE + p_sourceLanguage + ", " + p_targetLanguage);
            return result;
        }
        MTPLanguage mtpLang = mtpLangDAO.getMTPLanguageObj(srcLocale, trgLocale, domainId);//, p_account.getId()
        if (mtpLang == null)
        {
            result.put(JSONPN_STATUS, STATUS_FAILED);
            result.put(JSONPN_ERROR_MESSAGE, ERROR_NO_MTPROFILE);
            return result;
        }
        
        try
        {
            MachineTranslator translator = TranslateUtil.getMachineTranslator(mtpLang.getMtProfile());
            logger.info("Before Translation");
            logger.info("Source language: "+p_sourceLanguage);
            logger.info("Target language: "+p_targetLanguage);
            logger.info("Source Text: "+p_srcText);
            logger.info("MT profile name:"+ mtpLang.getMtProfile().getMtProfileName());
            
           String[] target = translator.translateBatchSegments(srcLocale.getLocale(), trgLocale.getLocale(), new String[]{p_srcText}, true, false);
            if (target == null || target.length == 0 || target[0].trim().length() == 0)
            {
                result.put(JSONPN_STATUS, STATUS_FAILED);
                result.put(JSONPN_ERROR_MESSAGE, ERROR_NO_RESULT);
            }
            else
            {
                result.put(JSONPN_STATUS, STATUS_SUCCESS);
                result.put(JSONPN_TARGET_TEXT, target[0]);
            }
        }
        catch (MachineTranslationException e)
        {
            result.put(JSONPN_STATUS, STATUS_FAILED);
            result.put(JSONPN_ERROR_MESSAGE, ERROR_NO_RESULT);
        }
        
        return result;
    }
    /*
     * function to send feedback email to admin
     * @param HttpServletRequest object(p_request)
     * @param ModelMap object(p_model)
     * 
     * return String
     */
    @RequestMapping("/feedback")
    public String feedback(HttpServletRequest p_request, ModelMap p_model)
      {
		try{
			
				logger.info("feedback functionality");
	
				HttpSession session = p_request.getSession(false);
				p_model.addAttribute(JSONPN_STATUS, "error");

				// Validate authentication
				if (!SessionUtil.validateSession(session)) {
					p_model.addAttribute(JSONPN_STATUS, "timeout");
					return "../error/401";
				}
			 	
				// feedback functionality for except administrator
				// if(!AppConstants.USER_TYPE_ADMIN.equals(account.getType())){		
				Object accountObj = session.getAttribute("theAccount");
				if (null == accountObj)
					return "false";
				if (!(accountObj instanceof Account))
					return "false";
				Account account = (Account) accountObj;
				
				 String srcLocale = p_request.getParameter(JSONPN_SOURCE_LOCALE);
				 String trgLocale = p_request.getParameter(JSONPN_TARGET_LOCALE);
				 MTPLanguagesDAO mtpLangDAO = DispatcherDAOFactory.getMTPLanguagesDAO();
				 
				MachineTranslationProfile machineTranslationProfile = mtpLangDAO.getMTProfileObj(srcLocale,trgLocale);
				if(StringUtils.isNull(machineTranslationProfile)){
					logger.info("Empty object found for Translation Profile");
					return "Error";
				}
				 
				 String srcLocaleTxt = p_request.getParameter("srcLocaleTxt");
				 String trgLocaleTxt = p_request.getParameter("trgLocaleTxt");
				 
				 String srcText = p_request.getParameter("srcText");
				 String trgText = p_request.getParameter("trgText");
				 String usrFeedback = p_request.getParameter("usrFeedback");
				 
				logger.info("Preparing mail body...");
				logger.info("MT Dispatcher’s User "+account.getFullName()+" (e-mail: "+account.getEmail() +") submitted feedback on performance of Microsoft Translator Hub.");
				logger.info("Language Pair: "+srcLocaleTxt+" - "+trgLocaleTxt);
				logger.info("Category ID: "+machineTranslationProfile.getCategory());
				logger.info("Text: "+srcText);
				logger.info("Translation: "+trgText);
				logger.info("Feedback: "+usrFeedback);
				
				// Start Reading template file
				StringBuilder finalContent = new StringBuilder();
				String htmlMsg = "";
				try {
					// Read template file
					htmlMsg = Util.readHtml("feedback_email_template.html");
					// Add email content to the template text and set template text to finalContent
					finalContent.append(Util.setValuesToHtml(htmlMsg, account,srcLocaleTxt,trgLocaleTxt,machineTranslationProfile,srcText,trgText,usrFeedback));
				} catch (Exception e) {
					logger.error("Exception occured : ", e);
					throw e;
				}
				
				String emailBodyTxt = finalContent.toString();
				// End Reading template file 
				
				logger.info(trgText);
				logger.info("Sending email regarding translation feedback...");
	
				Mailer mailer = new Mailer();
				mailer.sendMail(emailBodyTxt.toString(),account.getEmail());
				 
				p_model.addAttribute(JSONPN_STATUS, "success");
				
		}catch(Exception e){
					p_model.addAttribute(JSONPN_STATUS, "error");
					logger.error("Unable to send email for Feedback: "+e.getMessage());
		}
    			   	 
         return "success";
    }
    
    @RequestMapping("/saveTranslatedData")
    public String saveTranslatedData(@RequestParam Map<String, String> map,HttpServletRequest p_request, ModelMap p_model) {
    	HttpSession session = p_request.getSession(false);
		if (!SessionUtil.validateSession(session)) {
			logger.error("redirection save translate functionality");
			p_model.addAttribute(JSONPN_STATUS, "timeout");
			return "../error/401";
		}
		Account theAccount = (Account)session.getAttribute("theAccount");
      
        String sourceLanguage = map.get(JSONPN_SOURCE_LOCALE);
        String targetLanguage = map.get(JSONPN_TARGET_LOCALE);
        String oriSourceText = map.get(JSONPN_SOURCE_TEXT);
        String oriTargetText = map.get(JSONPN_TARGET_TEXT);
        String accountId = map.get(JSONPN_ACCOUNT_ID);
        
        String sourceLocale = map.get(JSONPN_SOURCE_LANGUAGE);
        String targetLocale = map.get(JSONPN_TARGET_LANGUAGE);
        long domainId = Long.parseLong(map.get("domainId"));
       
        try { 
        	//get MtProfile obj
        	MachineTranslationProfile mtProfile = mtpLangDAO.getMtProfileObjByDomainAndLocale(sourceLocale,targetLocale,domainId);
        	if(StringUtils.isNull(mtProfile)){
        		logger.info("Mt profile object not found");
        		p_model.addAttribute(JSONPN_STATUS, "error");
        		p_model.addAttribute("msg", "");
        	}
	        Translate translate =  new Translate();
	        translate.setSourceLocale(sourceLanguage);
	        translate.setTargetLocale(targetLanguage);
	        translate.setSource(oriSourceText);
	        translate.setTarget(oriTargetText);
	        translate.setAccountId(Integer.parseInt(accountId));
	        translate.setAccountName(theAccount.getAccountName());
	        translate.setDomain(map.get("domain"));
	        translate.setMtCategory((StringUtils.isNull(mtProfile))?"":mtProfile.getCategory());
	        translate.setCharactersCount(oriSourceText.length());
	        
			logger.info("Source : "+oriSourceText);
			logger.info("Target : "+oriTargetText);
			logger.info("Account Name : "+theAccount.getAccountName());
			logger.info("Domain : "+map.get("domain"));
			logger.info("Category : "+((StringUtils.isNull(mtProfile))?"":mtProfile.getCategory()));
			logger.info("Date : "+new Date());
			 
	        translateDAO.saveTranslatedData(translate);
	        
	        p_model.addAttribute(JSONPN_STATUS, "success");
       
        }catch(Exception e){
        	logger.error("Error while saving translated data: "+e);
        	p_model.addAttribute("msg", "Data not saved successfully");
        	p_model.addAttribute(JSONPN_STATUS, "error");
        }
        return "success";
    }
    
    
}