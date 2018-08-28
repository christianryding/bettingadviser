package bettingadviser.main;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

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
	
	}
	

	
}
