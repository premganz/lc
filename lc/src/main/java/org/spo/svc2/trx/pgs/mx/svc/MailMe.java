package org.spo.svc2.trx.pgs.mx.svc;


import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.spo.svc2.trx.pgs.mx.model.ContactForm;

public class MailMe {

	public void mailContactForm(ContactForm form) {

		final String username = "lemonytreetech@gmail.com";
		final String password = "tse-3067";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("lemonytreetech@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("misterprem@gmail.com"));
			message.setSubject("Mail Mergers and amalgamations");
			StringBuffer buf = new StringBuffer();
			buf.append(form.getName()+'\n'+form.getMessage()+'\n'+form.getEmailId()+'\n'+form.getPhoneNumber());
			message.setText(buf.toString());

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}