package bettingadviser.main;

import java.util.Arrays;
import java.util.List;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import bettingadviser.compare.Compare;
import bettingadviser.enums.SPORT_IDS;

public class RunWithArguments {

	ParameterSettings jCommDef = new ParameterSettings();
	String [] jCommArgs;
	
	/**
	 * Constructor that sets the arguments
	 * 
	 * @param args arguments given by user
	 */
	public RunWithArguments(String [] args){
		jCommArgs = args;
	}
	
	
	/**
	 * Run program with arguments given
	 */
	public void run(){
		
		JCommander jCommander = new JCommander(jCommDef);
		jCommander.setProgramName("Betting Adviseeeer");
		
		try {
			jCommander.parse(jCommArgs);
		} catch(ParameterException exception){
			System.out.println(exception.getMessage());
			//showUsage(jCommDef);
			jCommander.usage();
		}
		
		System.out.println(jCommDef.username);
		System.out.println(jCommDef.password);
		System.out.println(jCommDef.mailfrom);
		System.out.println(jCommDef.mailfrompswd);
		System.out.println(jCommDef.mailto);
		
		// settings
		String str = jCommDef.mailto;
		List<String> mailToList = Arrays.asList(str.split(","));
		
		// run program with settings
		Compare compare = new Compare(	jCommDef.username, 
										jCommDef.password, 
										jCommDef.mailfrom, 
										jCommDef.mailfrompswd, 
										mailToList);
	
		compare.setSportID(new SPORT_IDS().SOCCER);
		compare.setTimeInterval(5);
		compare.setPercent(0.97);
		//compare.setLowerMargin(1.3);
		//compare.setUpperMargin(3.4);
		//compare.setCheckLiveEvents(false);
		compare.setTimeRange(30);
		compare.start();
	}
	

	
}
