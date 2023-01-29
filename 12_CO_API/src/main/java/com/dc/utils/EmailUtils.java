package com.dc.utils;
import java.io.File;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * @author Mausoof.azam
 * i have taken one class as Email utils in which, in this class i have injected javaMailSender, it is a predefined interface in the springframework
 * i have annotated this class as @component to represent this class as spring bean.
 * 
 *
 */
@Component
public class EmailUtils {

	
	
	@Autowired
	private JavaMailSender javaMailSender;

	public boolean sendEmail(String to, String subject, String body, File file) {
		boolean isMailSent = false;
		// sometime we got issue from smtp server do for the safe side we are using try
		// and catch block
		try {
			
			//mime message is a class to sent the message
			MimeMessage MimeMessage = javaMailSender.createMimeMessage();
			//constructor injection
			MimeMessageHelper helper = new MimeMessageHelper(MimeMessage);
			helper.setTo(to);
//			helper.setCc(cc);
//			helper.setBcc(null);
			helper.setSubject(subject);
			helper.setText(body, true);
			helper.addAttachment("His-Elig-Notice", file);

			javaMailSender.send(MimeMessage);
			isMailSent = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isMailSent;
	}
}