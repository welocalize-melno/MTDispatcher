package com.globalsight.dispatcher.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.globalsight.dispatcher.bo.AppConstants;
import com.globalsight.dispatcher.bo.Domain;
import com.globalsight.dispatcher.dao.DomainDAO;
import com.globalsight.dispatcher.util.SessionUtil;
import com.globalsight.dispatcher.util.StringUtils;

@Controller
@RequestMapping("/domain")
public class DomainController implements AppConstants {
    private static final Logger logger = Logger.getLogger(DomainController.class);
    
    @Autowired
    private DomainDAO domainDAO;

    @RequestMapping(value = "/addDomain")
    public String home(@RequestParam Map<String, String> p_reqMap, HttpServletRequest p_request, ModelMap p_model) {
    	try{
    		String domainName = p_reqMap.get("domainName");
    		p_model.put("isAlreadyExists",false );
	    	HttpSession session = p_request.getSession(false);
	        if(!SessionUtil.validateSession(session)){
	        	logger.info("session expired");
	        	p_model.put("status", "timeout");
	        		return "../error/401";
	        }
	        if(!domainDAO.getDomainObj(domainName)){
	        	p_model.put("isAlreadyExists",true );
	        	return "home";
	        }
	        Domain domain = new Domain();
   	        domain.setDomainName(domainName);
   	        domainDAO.saveDomain(domain);
	        p_model.put("status", "success");
	        p_model.put("msg", "Domain added successfully");
    	}catch(Exception e){
    		logger.info(e.getMessage());
    		p_model.put("status", "error");
    		p_model.put("msg", "Failed to add domain");
    	}
        return "home";
 
    }
    
	@RequestMapping(value = "/getDomains", method = RequestMethod.GET)
    public String getDomains(HttpServletRequest p_request, ModelMap p_model)
    {
    	HttpSession session = p_request.getSession(false);
        if(!SessionUtil.validateSession(session)) {
            return "../error/401";
        }
        List<Domain> list = null;
        if(!StringUtils.isNull(domainDAO.getAllDomains())){
        	list = new ArrayList<Domain>(domainDAO.getAllDomains());
            Collections.sort(list);
        }
        p_model.addAttribute("allDomains", list);
        return "onlineTestMain";
    }
  
}