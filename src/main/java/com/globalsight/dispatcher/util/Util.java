package com.globalsight.dispatcher.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.globalsight.dispatcher.bo.Account;
import com.globalsight.dispatcher.bo.MachineTranslationProfile;
import com.globalsight.machineTranslation.MTHelper;

public class Util implements Serializable {

	private static final long serialVersionUID = -6053201355509408025L;
	
	private static final Logger LOGGER = Logger.getLogger(Util.class);
	
	private static final String SSP_REPORT_USAGE_TEMPLATE_PATH = MTHelper.getMTConfig("feedback.template");
	
	/***
	 * To read HTML template file content from file system
	 * @param fileName
	 * @return
	 */
	public static String readHtml(String fileName) throws Exception {
		LOGGER.info("Started reading the mail template for confirmation email... ");
		StringBuffer content = new StringBuffer("");
		String s = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("" + SSP_REPORT_USAGE_TEMPLATE_PATH + "/" + fileName));
			while ((s=br.readLine()) != null) {
				content.append(s);
			} 
			LOGGER.info("Confirmation email template reading is done.");
		} catch (Exception ex) {
			LOGGER.error("Exception occured while running readHtml() method : ", ex);
			throw ex;
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e) {
				// No need to throw
			}
		}
		return content.toString();
	}
	
	/***
	 * To set the Email Content to HTML template  
	 * @param htmlMsg
	 * @param account obj
	 * @return
	 * @throws Exception 
	 */
	public static String setValuesToHtml (String htmlMsg, Account account, String srcLocaleTxt, String trgLocaleTxt,MachineTranslationProfile machineTranslationProfile, String srcText, String trgText, String usrFeedback) throws Exception {
		StringBuffer finalContent = new StringBuffer();  
 		// Get current year
		Integer year = Calendar.getInstance().get(Calendar.YEAR);
		if (null != htmlMsg && !htmlMsg.isEmpty()) {
			// Replace #CURRENT_YEAR in HTML template with the current Year
			htmlMsg = htmlMsg.replace("#CURRENT_YEAR", year.toString());
			htmlMsg = htmlMsg.replace("#NAME", account.getFullName());
			htmlMsg = htmlMsg.replace("#EMAIL_ADDRESS", account.getEmail());
			htmlMsg = htmlMsg.replace("#SOURCE_LOCALE", StringUtils.isNull(srcLocaleTxt)?"":srcLocaleTxt);
			htmlMsg = htmlMsg.replace("#TARGET_LOCALE", StringUtils.isNull(trgLocaleTxt)?"":trgLocaleTxt);
			htmlMsg = htmlMsg.replace("#CATEGORY_MS_TRANSLATOR", machineTranslationProfile.getCategory());
			htmlMsg = htmlMsg.replace("#SOURCE_TEXT", srcText);
			htmlMsg = htmlMsg.replace("#TARGET_TEXT", trgText);
			htmlMsg = htmlMsg.replace("#UserFeedback", usrFeedback);
			
			finalContent.append(htmlMsg);
 		}
		return finalContent.toString();
	}

}
