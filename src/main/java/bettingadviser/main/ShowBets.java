package bettingadviser.main;

import java.util.ArrayList;
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
	 * @param args username and password for pinnacle mailaddressTo, mailaddressFrom and mailaddressFromPasswd
	 */
	public static void main(String[] args){
		
		// settings
		String username = args[0];
		String password = args[1];
		String mailFrom = args[2];
		String mailFromPassw = args[3];
		ArrayList<String> mailTo = new ArrayList<String>();
		mailTo.add(args[4]);
		mailTo.add(args[5]);

		
		// run program with settings
		Compare compare = new Compare(username, password, mailFrom, mailFromPassw, mailTo);
		compare.setSportID(new SPORT_IDS().ESPORT);
		compare.setTimeInterval(10);
		compare.setPercent(0.9);
		//compare.setLowerMargin(1.3);
		//compare.setUpperMargin(3.4);
		compare.start();
	}
}