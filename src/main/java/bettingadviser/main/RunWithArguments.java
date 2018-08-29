package bettingadviser.main;

import java.util.Arrays;
import java.util.List;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import bettingadviser.compare.Compare;
import bettingadviser.enums.SPORT_IDS;

public class RunWithArguments {

	ParameterSettings jCommDef = new ParameterSettings();
	String [] jCommArgs;

	// default values
	private int SPORT_ID = new SPORT_IDS().SOCCER;
	private int INTERVAL_MIN = 10;
	private double PERCENT_MARGIN = 0.9;
	private double UPPER_MARGIN = 3.5;
	private double LOWER_MARGIN = 1.2;
	private boolean CHECK_LIVE_EVENTS = false;
	private int TIME_RANGE = 30;

	
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
	
		// set arguments if given, else set default value
		if(jCommDef.sport == 29 || jCommDef.sport == 12 || jCommDef.sport == 33 || jCommDef.sport == 3 || jCommDef.sport == 4 || 
				jCommDef.sport == 19 ||jCommDef.sport == 18 || jCommDef.sport == 28 || jCommDef.sport == 15) {
			compare.setSportID(jCommDef.sport);
		}else {
			compare.setSportID(SPORT_ID);
		}
		
		if(jCommDef.interval > 0 && jCommDef.interval <= 30) {
			compare.setTimeInterval(jCommDef.interval);
		}else {
			compare.setTimeInterval(INTERVAL_MIN);
		}
		
		if(jCommDef.percentage > 0 && jCommDef.percentage < 1) {
			compare.setPercent(jCommDef.percentage);
		}else {
			compare.setPercent(PERCENT_MARGIN);
		}
		
		if(jCommDef.lowerMargins > 1.1 && jCommDef.lowerMargins < 50) {
			compare.setLowerMargin(jCommDef.lowerMargins);
		}else {
			compare.setLowerMargin(LOWER_MARGIN);
		}
		
		if(jCommDef.upperMargin > 1.2 && jCommDef.upperMargin < 100) {
			compare.setUpperMargin(jCommDef.upperMargin);
		}else {
			compare.setUpperMargin(UPPER_MARGIN);
		}
		
		if(jCommDef.checkLive != CHECK_LIVE_EVENTS) {
			compare.setCheckLiveEvents(jCommDef.checkLive);
		}else {
			compare.setCheckLiveEvents(CHECK_LIVE_EVENTS);
		}
		
		if(jCommDef.rangeMinutes > 0 && jCommDef.rangeMinutes < 1000) {
			compare.setTimeRange(jCommDef.rangeMinutes);
		}else {
			compare.setTimeRange(TIME_RANGE);
		}
		
		compare.start();
	}	
}