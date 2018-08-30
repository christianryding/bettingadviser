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
	}
}