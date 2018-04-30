package bettingadviser.main;

import bettingadviser.compare.Compare;

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
		String mailTo = args[2];
		String mailFrom = args[3];
		String mailFromPassw = args[4];
		
		// run program with settings
		Compare compare = new Compare(username, password, mailTo, mailFrom, mailFromPassw);
		compare.setSportID(12);
		compare.setTimeInterval(10);
		compare.setPercent(0.9);
		compare.start();
	}
}