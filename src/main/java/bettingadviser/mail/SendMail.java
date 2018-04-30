package bettingadviser.mail;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

import bettingadviser.compare.Moneyline;

/**
 * 
 * @author christian ryding
 */
public class SendMail {

	private final String to, from, password;
	private Properties properties;
	private Session session;
	
	public SendMail(String gmailTo, String gmailFrom, String pass) {
		from = gmailFrom;
		to = gmailTo;
		password = pass;
		properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", 587);
		session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
		});
	}
	
	/**
	 * Send event with good odds to mail
	 * 
	 * @param mailMoneylineEvent
	 */
	public void sendEvents(ArrayList<Moneyline> mailMoneylineEvent) {
		
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("Good Bets/Odds");
			
			String tmp = "Events:\n";// mail string
			
			for(Moneyline m : mailMoneylineEvent) {
				
				// get current time and start time for event
				Instant instantNow = Instant.now();
				Instant i2; 
				i2 = Instant.parse(m.getCutoff());
				
				// calculate time left to game starts
				long minutesToGame = Duration.between(instantNow, i2).toMinutes();
				long hoursToGame = minutesToGame / 60;
				minutesToGame = minutesToGame % 60;
				
				// debugging
				System.out.println(m.getEventInfo());
				System.out.println("LIVESTATUS: " + m.getLiveStatus());

				// add event info
				tmp += m.getEventInfo() + " | (" + hoursToGame + "h " + minutesToGame + "min -> Gamestart) " + "\n";
			}
			
			// add links
			if(mailMoneylineEvent.size() > 0) {
				tmp += "\nhttps://beta.pinnacle.com/en/Sports/" + mailMoneylineEvent.get(0).getSportID(); 
				tmp += "\nhttps://beta.pinnacle.com/en/Sports/" + mailMoneylineEvent.get(0).getSportID() + "/Live";
			}
			
			message.setText(tmp);
			Transport.send(message);
			System.out.println("message sent, check inbox");
		}catch(MessagingException me) {
			me.printStackTrace();
		}
	}
	
	
}
