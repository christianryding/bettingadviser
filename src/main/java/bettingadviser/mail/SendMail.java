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

	private final String from, password;
	private final ArrayList<String> mailTo;
	private Properties properties;
	private Session session;
	
	public SendMail(ArrayList<String> gmailTo, String gmailFrom, String pass) {
		from = gmailFrom;
		mailTo = gmailTo;
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

		// add e-mailToAddresses to string
		String mailAddresses = "";
		if(mailTo.size() > 0) {
			for(int i = 0; i < mailTo.size(); i++) {
				mailAddresses += mailTo.get(i); 
				if(i != (mailTo.size()-1) ) {
					mailAddresses += ",";
				}
			}
		}
		
		try {
			
			// create message
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			InternetAddress[] iAdressArray = InternetAddress.parse(mailAddresses);
			message.setRecipients(Message.RecipientType.TO, iAdressArray);
			
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
				
				// add event info to string
				tmp += m.getEventInfo() + " | (" + hoursToGame + "h " + minutesToGame + "min -> Gamestart) " + "\n";
				
				// DEBUGGING
				System.out.println(m.getEventInfo());
				System.out.println("HOME: " + m.getHomeStr() + "  AWAY: " + m.getAwayStr());
				System.out.println("LIVESTATUS: " + m.getLiveStatus());
				System.out.println("cutoff: " + m.getCutoff());
				System.out.println(hoursToGame + "h " + minutesToGame + "min -> Gamestart\n");
			}
			
			// add links
			tmp += "\nhttps://beta.pinnacle.com/en/Sports/" + mailMoneylineEvent.get(0).getSportID(); 
			tmp += "\nhttps://beta.pinnacle.com/en/Sports/" + mailMoneylineEvent.get(0).getSportID() + "/Live";
			
			// set content and send mail
			message.setSubject("Good Bets/Odds");
			message.setText(tmp);
			Transport.send(message);
			System.out.println("E-mail sent");
			
		}catch(MessagingException me) {
			me.printStackTrace();
		}
	}
		
}
