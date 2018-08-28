package bettingadviser.main;

import java.util.ArrayList;

import com.beust.jcommander.JCommander;

import bettingadviser.compare.Compare;
import bettingadviser.enums.SPORT_IDS;



/**
 * 
 * @author christian ryding
 */
public class ShowBets {


	/**
	 * Main program
	 * 
	 * @param args username and password for pinnacle, mailaddressFrom and mailaddressFromPasswd and mailaddresses to send to
	 */
	public static void main(String[] args){
		
		String[] argv = { 
				"-log", "2", 
	            "-username", "chry",
	            "-password", "pswd", 
	            "-mailfrom", "chry@gmail.com",
	            "-mailfrompswd", "mailfrompswd", 
				"-mailto", "mail1,mail2,mail3",

	            "-Doption=value", "a", "b", "c" };
		
		RunWithArguments rwa = new RunWithArguments(argv);
		rwa.run();

		
		
/*		// settings
		String username = args[0];
		String password = args[1];
		String mailFrom = args[2];
		String mailFromPassw = args[3];

		// get every email addresses to send to
		ArrayList<String> mailTo = new ArrayList<String>();
		for(int i = 4; i < args.length; i++) {
			mailTo.add(args[i]);
		}

		// run program with settings
		Compare compare = new Compare(username, password, mailFrom, mailFromPassw, mailTo);
		compare.setSportID(new SPORT_IDS().SOCCER);
		compare.setTimeInterval(5);
		compare.setPercent(0.97);
		//compare.setLowerMargin(1.3);
		//compare.setUpperMargin(3.4);
		//compare.setCheckLiveEvents(false);
		//compare.setTimeRange(8);
		compare.start();*/
	}
}