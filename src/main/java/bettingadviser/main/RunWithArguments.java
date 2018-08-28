package bettingadviser.main;

import java.util.ArrayList;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import bettingadviser.compare.Compare;
import bettingadviser.enums.SPORT_IDS;

public class RunWithArguments {

	JCommanderDefinitions jCommDef = new JCommanderDefinitions();
	String [] jCommArgs;
	
	/**
	 * 
	 * @param args
	 */
	public RunWithArguments(String [] args){
		jCommArgs = args;
	}
	
	
	/**
	 * 
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
	*/
		
		ArrayList<String> mailTo = new ArrayList<String>();
		mailTo.add(jCommDef.mailto);
		
		
		//CUT EACHMAILADDRESS AT COMMA
		
		
		
		/*
		// run program with settings
		Compare compare = new Compare(	jCommDef.username, 
										jCommDef.password, 
										jCommDef.mailfrom, 
										jCommDef.mailfrompswd, 
										mailTo);
		*/
		//compare.setSportID(new SPORT_IDS().SOCCER);
		//compare.setTimeInterval(5);
		//compare.setPercent(0.97);
		//compare.setLowerMargin(1.3);
		//compare.setUpperMargin(3.4);
		//compare.setCheckLiveEvents(false);
		//compare.setTimeRange(8);
		//compare.start();
	}
	

	
}
