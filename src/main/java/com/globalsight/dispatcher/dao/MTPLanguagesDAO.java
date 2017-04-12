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
package com.globalsight.dispatcher.dao;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.globalsight.dispatcher.bo.Account;
import com.globalsight.dispatcher.bo.GlobalSightLocale;
import com.globalsight.dispatcher.bo.MTPLanguage;
import com.globalsight.dispatcher.bo.MTPLanguages;
import com.globalsight.dispatcher.bo.MachineTranslationProfile;
import com.globalsight.dispatcher.util.StringUtils;

/**
 * Machine Translation Profile Language DAO.
 * 
 * @author Joey
 * 
 */
@Repository
public class MTPLanguagesDAO {
	
	
	protected static final Logger logger = Logger.getLogger(MTPLanguagesDAO.class);

	protected final String fileName = "Languages.xml";

	protected String filePath;

	protected MTPLanguages mtpLanguages;
	
	@Autowired
	MTProfilesDAO mtProfilesDAO;

	public MTPLanguagesDAO () {
	}
	

	// Get the File Path in Server.
	public String getFilePath () {
		if (filePath == null) {
			filePath = CommonDAO.getDataFolderPath() + fileName;
		}

		return filePath;
	}

	protected void saveMTPLanguages (MTPLanguages p_langs) throws JAXBException {
		// create JAXB context and instantiate marshaller
		JAXBContext context = JAXBContext.newInstance(MTPLanguages.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		// Write to File
		m.marshal(p_langs, new File(getFilePath()));
	}

	public void saveMTPLanguage (MTPLanguage p_lang) throws JAXBException {
		if (p_lang.getId() < 0) {
			if (mtpLanguages == null)
				getMTPLanguages();
			p_lang.setId(mtpLanguages.getAndIncrement());
		}

		getMTPLanguages().add(p_lang);
		saveMTPLanguages(mtpLanguages);
	}

	public void updateMTPLanguage (MTPLanguage p_lang) throws JAXBException {
		for (MTPLanguage lang : getMTPLanguages()) {
			if (lang.getId() == p_lang.getId()) {
				mtpLanguages.getLanguageSet().remove(lang);
				mtpLanguages.getLanguageSet().add(p_lang);
				saveMTPLanguages(mtpLanguages);
				break;
			}
		}
	}

	public void saveOrUpdateMTPLanguage (MTPLanguage p_lang) {
		try {
			if (p_lang.getId() < 0) {
				saveMTPLanguage(p_lang);
			}
			else {
				updateMTPLanguage(p_lang);
			}
		}
		catch (JAXBException e) {
			String message = "saveOrUpdateMTPLanguage error";
			message += "[ID:" + p_lang.getId() + ", mtProfileID:" + p_lang.getMtProfile().getId() + "]";
			logger.info(message, e);
		}
	}

	public void deleteMTPLanguage (long p_id) throws JAXBException {
		for (MTPLanguage lang : getMTPLanguages()) {
			if (lang.getId() == p_id) {
				mtpLanguages.getLanguageSet().remove(lang);
				saveMTPLanguages(mtpLanguages);
				break;
			}
		}
	}

	public void deleteMTPLanguage (String[] p_removeIDS) throws JAXBException {
		if (p_removeIDS == null || p_removeIDS.length == 0) {
			return;
		}

		Set <Long> remoceIDSet = new HashSet <Long>();
		for (String id : p_removeIDS) {
			remoceIDSet.add(Long.valueOf(id));
		}

		for (MTPLanguage mtProfile : getMTPLanguages()) {
			if (remoceIDSet.contains(mtProfile.getId())) {
				mtpLanguages.getLanguageSet().remove(mtProfile);
			}
		}

		saveMTPLanguages(mtpLanguages);
	}

	// Get MTP Language Business Object
	public MTPLanguage getMTPLanguage (long p_languageId) {
		for (MTPLanguage lang : getMTPLanguages()) {
			if (lang.getId() == p_languageId) {
				return lang;
			}
		}

		return null;
	}

	// Get MTP Language Business Object
	public MTPLanguage getMTPLanguage (String p_mtpLangName) {
		for (MTPLanguage lang : getMTPLanguages()) {
			if (lang.getName().equals(p_mtpLangName)) {
				return lang;
			}
		}

		return null;
	}

	/*
	 * public MTPLanguage getMTPLanguage(GlobalSightLocale p_srcLocale, GlobalSightLocale p_trgLocale, String p_securityCode) { for(MTPLanguage lang : getMTPLanguages()) { if(lang.getSrcLocale().equals(p_srcLocale) && lang.getTrgLocale().equals(p_trgLocale) && isEqualSecurityCode(lang,
	 * p_securityCode)) { return lang; } }
	 * 
	 * return null; }
	 */

	public MTPLanguage getMTPLanguage (GlobalSightLocale p_srcLocale, GlobalSightLocale p_trgLocale) {
		for (MTPLanguage lang : getMTPLanguages()) {
			if (lang.getSrcLocale().equals(p_srcLocale) && lang.getTrgLocale().equals(p_trgLocale)) {
				return lang;
			}
		}

		return null;
	}
	public MTPLanguage getMTPLanguage (GlobalSightLocale p_srcLocale, GlobalSightLocale p_trgLocale,long profileId) {
		for (MTPLanguage lang : getMTPLanguages()) {
			if (lang.getSrcLocale().equals(p_srcLocale) && lang.getTrgLocale().equals(p_trgLocale) && lang.getMtProfileId() == profileId) {
				return lang;
			}
		}

		return null;
	}

	// Get first matched MTPLanguage.
	public MTPLanguage getFirstMTPLanguage (long p_mtProfileID) {
		for (MTPLanguage lang : getMTPLanguages()) {
			if (lang.getMtProfileId() == p_mtProfileID) {
				return lang;
			}
		}

		return null;
	}

	// Get MTPLanguages By Account Name.
	public Set <MTPLanguage> getMTPLanguageByAccount (String p_accountName) {
		Set <MTPLanguage> langs = new HashSet <MTPLanguage>();
		Account account = DispatcherDAOFactory.getAccountDAO().getAccountByAccountName(p_accountName);
		if (account == null)
			return langs;

		for (MTPLanguage lang : getMTPLanguages()) {
			langs.add(lang);
		}

		return langs;
	}

	public Set <MTPLanguage> getMTPLanguages () {
		if (mtpLanguages == null) {
			try {
				File file = new File(getFilePath());
				if (!file.exists()) {
					file.createNewFile();
					mtpLanguages = new MTPLanguages();
					return mtpLanguages.getLanguageSet();
				}
				else if (file.length() == 0) {
					mtpLanguages = new MTPLanguages();
					return mtpLanguages.getLanguageSet();
				}

				JAXBContext context = JAXBContext.newInstance(MTPLanguages.class);
				Unmarshaller um = context.createUnmarshaller();
				mtpLanguages = (MTPLanguages) um.unmarshal(new FileReader(getFilePath()));
			}
			catch (JAXBException jaxbEx) {
				String message = "getMTPLanguages --> JAXBException:" + getFilePath();
				logger.error(message, jaxbEx);
				return null;
			}
			catch (IOException ioEx) {
				String message = "getMTPLanguages --> IOException:" + getFilePath();
				logger.error(message, ioEx);
			}
		}

		return mtpLanguages.getLanguageSet();
	}

	public Map <GlobalSightLocale, Set <GlobalSightLocale>> getSupportedLocalePairs (String p_securityCode) {
		Map <GlobalSightLocale, Set <GlobalSightLocale>> pairs = new HashMap <GlobalSightLocale, Set <GlobalSightLocale>>();
		for (MTPLanguage lang : getMTPLanguages()) {
			GlobalSightLocale srcLocale = lang.getSrcLocale();
			Set <GlobalSightLocale> trgLocaleSet = pairs.get(srcLocale);
			if (trgLocaleSet == null) {
				trgLocaleSet = new HashSet <GlobalSightLocale>();
			}
			trgLocaleSet.add(lang.getTrgLocale());
			pairs.put(srcLocale, trgLocaleSet);
		}
		return pairs;
	}

	public Set <GlobalSightLocale> getSupportedSourceLocales (String p_accountName) {
		SortedSet <GlobalSightLocale> result = new TreeSet <GlobalSightLocale>();
		if (StringUtils.isBlank(p_accountName))
			return result;

		for (MTPLanguage lang : getMTPLanguages()) {
			result.add(lang.getSrcLocale());
		}
		return result;
	}

	public Set <GlobalSightLocale> getSupportedTargetLocales (String p_accountName, GlobalSightLocale p_srcLocale) {
		SortedSet <GlobalSightLocale> result = new TreeSet <GlobalSightLocale>();
		if (StringUtils.isBlank(p_accountName))
			return result;

		for (MTPLanguage lang : getMTPLanguages()) {
			result.add(lang.getTrgLocale());
		}
		return result;
	}

	public String chkMTProfileCategory (String p_accountName, GlobalSightLocale p_srcLocale, GlobalSightLocale p_trgLocale, long domain) {

		if (StringUtils.isBlank(p_accountName))
			return "Account name not found.Invalid user.";
		
		List<Long> mtProfileIdList = new ArrayList<Long>();
		MTProfilesDAO mtProfilesDAO = DispatcherDAOFactory.getMTPRofileDAO();
       	for (MachineTranslationProfile mtProfile : mtProfilesDAO.getAllMTProfiles())
        {
            if (domain == (mtProfile.getDomain())){
                 
            	mtProfileIdList.add(mtProfile.getId());
            }
        }
       	
		for (MTPLanguage lang : getMTPLanguages()) {
			if (lang.getTrgLocale().equals(p_trgLocale) && lang.getSrcLocale().equals(p_srcLocale)) {
				logger.info("checking profile category for Source Locale : " + p_srcLocale+" and Target Locale : "+p_trgLocale);
				
				if(mtProfileIdList.contains(lang.getMtProfileId())){
					
					MachineTranslationProfile machineTranslationProfile = lang.getMtProfile();
				
					if (StringUtils.isNull(machineTranslationProfile))
						return "MTProfile containg null";
	
					if (!(machineTranslationProfile.getCategory()).equalsIgnoreCase("general")) {
						return "true";
					}
				}
			}
		}

		return "false";
	}

	/*
   * function to check language pairs exists or not for the selected locales
   * @param p_srcLocale
   * @param p_trgLocale
   * return String
   */
	public String checkLanguagePair (GlobalSightLocale p_srcLocale, GlobalSightLocale p_trgLocale, long domain) {
		logger.info("checking language pair exists or not for Source Locale : " + p_srcLocale+" and Target Locale : "+p_trgLocale);
		if(StringUtils.isNull(domain)){
			return null;
		}
 		List<Long> mtProfileIdList = new ArrayList<Long>();
		MTProfilesDAO mtProfilesDAO = DispatcherDAOFactory.getMTPRofileDAO();
       	for (MachineTranslationProfile mtProfile : mtProfilesDAO.getAllMTProfiles())
        {
            if (domain == (mtProfile.getDomain())){
                 
            	mtProfileIdList.add(mtProfile.getId());
            }
        }
        for (MTPLanguage lang : getMTPLanguages()) {
       		if(!StringUtils.isNull(p_srcLocale) && p_srcLocale.equals(lang.getSrcLocale()) && !StringUtils.isNull(p_trgLocale) && p_trgLocale.equals(lang.getTrgLocale())){
       			if(mtProfileIdList.contains(lang.getMtProfileId())){
	       			MachineTranslationProfile machineTranslationProfile = lang.getMtProfile();
					if (StringUtils.isNull(machineTranslationProfile)) {
						return "false";
					}
					return "true";
       			}
       		}
		}
		return "false";
	}
	/*
   * function to get MachineTranslationProfile object
   * @param String srcLocale
   * @param String trgLocale
   * return MachineTranslationProfile
   */
	public MachineTranslationProfile getMTProfileObj (String srcLocale, String trgLocale) {

		GlobalSightLocale p_srcLocale = CommonDAO.getGlobalSightLocaleByShortName(srcLocale);
		GlobalSightLocale p_trgLocale = CommonDAO.getGlobalSightLocaleByShortName(trgLocale);

		for (MTPLanguage lang : getMTPLanguages()) {
			if (lang.getTrgLocale().equals(p_trgLocale) && lang.getSrcLocale().equals(p_srcLocale)) {

				logger.info("Source Locale : " + p_srcLocale);
				logger.info("Target Locale : " + p_trgLocale);
				MachineTranslationProfile machineTranslationProfile = lang.getMtProfile();
				if (!StringUtils.isNull(machineTranslationProfile))
					return machineTranslationProfile;
			}
		}
		return null;
	}
  /*
   * function to get language pairs related non general category MTProfile
   * 
   * return String
   */
	public String getFeedbackSupportedLanguagePairs () {
		StringBuilder nonGenCategoryPairs = new StringBuilder();
		for (MTPLanguage lang : getMTPLanguages()) {
			MachineTranslationProfile machineTranslationProfile = lang.getMtProfile();
			if (!StringUtils.isNull(machineTranslationProfile) && (!machineTranslationProfile.getCategory().equalsIgnoreCase("general"))) {

				String languagePair = lang.getSrcLocale().getDisplayName() + " - " + lang.getTrgLocale().getDisplayName();
				nonGenCategoryPairs.append(languagePair);
				nonGenCategoryPairs.append(" ");
				nonGenCategoryPairs.append(",");
			}
		}
		String languagePairs = nonGenCategoryPairs.toString();
		return languagePairs.length() > 0 ? languagePairs.substring(0, languagePairs.length() - 1) : "";
	}
	
	/*
	 * function to get Locale(source or target)
	 * @String domain
	 * @String srcLocale
	 * return Set<GlobalSightLocale>
	 */
	public Set<GlobalSightLocale> getLanguagePairsByDomain (long domain,String srcLocale ) {
		List<Long> mtProfileIdList = new ArrayList<Long>();
		
		//Get all MT profile id's with the given domain		
		MTProfilesDAO mtProfilesDAO = DispatcherDAOFactory.getMTPRofileDAO();
       	for (MachineTranslationProfile mtProfile : mtProfilesDAO.getAllMTProfiles())
        {
            if (domain == mtProfile.getDomain()){
            	mtProfileIdList.add(mtProfile.getId());
            }
        }
       	//get locale
       	Set<GlobalSightLocale> localeSet = new TreeSet<GlobalSightLocale>();  
       	for (MTPLanguage lang : getMTPLanguages()) {
       		//get target locale
       		if(!StringUtils.isNull(srcLocale) && srcLocale.equalsIgnoreCase(lang.getSrcLocale().getLanguage()+"_"+lang.getSrcLocale().getCountry())  &&  mtProfileIdList.contains(lang.getMtProfileId())){
       			localeSet.add(lang.getTrgLocale());
       		}
       		//get source locale
       		if(StringUtils.isNull(srcLocale) &&  mtProfileIdList.contains(lang.getMtProfileId())){
       			localeSet.add(lang.getSrcLocale());
			}
		}
       	return localeSet;
	}
	public MachineTranslationProfile getMtProfileObjByDomainAndLocale (String srcLocale, String trgLocale, long domainId) {

		logger.info("s Locale : " + srcLocale);
		logger.info("t Locale : " + trgLocale);
		logger.info("Domain : " + domainId);
		
		//Get all MT profile id's with the given domain	
		List<Long> mtProfileIdList = new ArrayList<Long>();
       	for (MachineTranslationProfile mtProfile : mtProfilesDAO.getAllMTProfiles())
        {
            if (domainId == mtProfile.getDomain()){
            	mtProfileIdList.add(mtProfile.getId());
            }
        }
		       	
		for (MTPLanguage lang : getMTPLanguages()) {
				if(!StringUtils.isNull(srcLocale) && srcLocale.equalsIgnoreCase(lang.getSrcLocale().getLanguage()+"_"+lang.getSrcLocale().getCountry()) && trgLocale.equalsIgnoreCase(lang.getTrgLocale().getLanguage()+"_"+lang.getTrgLocale().getCountry()) &&  mtProfileIdList.contains(lang.getMtProfileId())){
					return	lang.getMtProfile();
			}
		}
		return null;
	}
	
	public MTPLanguage getMTPLanguageObj (GlobalSightLocale p_srcLocale, GlobalSightLocale p_trgLocale, long domain) {

		//Get all MT profile id's with the given domain	
		List<Long> mtProfileIdList = new ArrayList<Long>();
       	for (MachineTranslationProfile mtProfile : mtProfilesDAO.getAllMTProfiles())
        {
            if (domain == mtProfile.getDomain()){
            	mtProfileIdList.add(mtProfile.getId());
            }
        }
		logger.info(mtProfileIdList);
       	for (MTPLanguage lang : getMTPLanguages()) {
			if (lang.getSrcLocale().equals(p_srcLocale) && lang.getTrgLocale().equals(p_trgLocale) && mtProfileIdList.contains(lang.getMtProfileId())) {
				return lang;
			}
		}
    	logger.info("No matching language pair found");
		return null;
	}
}
