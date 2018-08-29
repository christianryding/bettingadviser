package bettingadviser.main;

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
		
		RunWithArguments rwa = new RunWithArguments(args);
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