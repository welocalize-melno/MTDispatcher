package com.globalsight.dispatcher.util;
 
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.globalsight.machineTranslation.MTHelper;

public class Mailer   {

  private static final Logger logger = Logger.getLogger(Mailer.class);
	
  /*
   *  function to send feedback mail to admin
   *  @param String mailbody
   *  @param String fromAddressEmail
   *  
   */
	public void sendMail (String body, String fromAddressEmail) {

		logger.info("Sending mail...");
		 
		
		// Get mail credentials from config.properties
		final String username = MTHelper.getMTConfig("mail.username");
		final String password = MTHelper.getMTConfig("mail.password");

		String smtpHost = MTHelper.getMTConfig("mail.host");
		Integer smtpPort =  Integer.parseInt(MTHelper.getMTConfig("mail.port"));
		
		String fromAddress =  MTHelper.getMTConfig("mail.from");//(StringUtils.isNull(fromAddressEmail))?MTHelper.getMTConfig("mail.from"):fromAddressEmail;
		String toAddress =  MTHelper.getMTConfig("mail.to");

		logger.info("Account email : " + fromAddressEmail);
		logger.info("FROM address : " + fromAddress);
		logger.info("TO address : " + toAddress);

		logger.info("smtp user name : " + username);
		logger.info("smtp host : " + smtpHost);
		logger.info("smtp port : " + smtpPort);

		
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.port", smtpPort);

		try {
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {

				protected PasswordAuthentication getPasswordAuthentication () {
					return new PasswordAuthentication(username, password);
				}
			});

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAddress));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));

			message.setSubject("MT Dispatcher for John Deere: User’s Feedback");

		  StringBuilder messageBody = new StringBuilder();
		 
			messageBody.append(body);
			
			// Create a message part to represent the body text 
			BodyPart messageBodyPart = new MimeBodyPart(); 
			messageBodyPart.setContent( messageBody.toString(), "text/html; charset=utf-8" );

			// use a MimeMultipart as we need to handle the file attachments 
			Multipart multipart = new MimeMultipart(); 

			// add the message body to the mime message 
			multipart.addBodyPart( messageBodyPart ); 

			// Put all message parts in the message 
			message.setContent( multipart );  
			 	
		
  		Transport.send(message);
			logger.info("Email send successfuly");
		}
		catch (MessagingException e) {
			logger.error("Exception while sending email " + e);
		}
		catch (Exception e) {
			logger.error("Exception while sending email " + e);
		}
	}
 
	
	 

}