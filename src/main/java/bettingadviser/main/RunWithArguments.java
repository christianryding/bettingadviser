package bettingadviser.main;

import java.util.Arrays;
import java.util.List;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import bettingadviser.compare.Compare;
import bettingadviser.enums.DEFAULT_SETTINGS;
import bettingadviser.enums.SPORT_IDS;

public class RunWithArguments {

	ParameterSettings jCommDef;
	String [] jCommArgs;

	/**
	 * Constructor, sets the arguments
	 * 
	 * @param args arguments given by user
	 */
	public RunWithArguments(String [] args){
		jCommDef = new ParameterSettings();
		jCommArgs = args;
	}
	
	
	/**
	 * Run program with arguments given
	 */
	public void run(){
		
		JCommander jCommander = new JCommander(jCommDef);
		jCommander.setProgramName("Betting Adviser");
		
		try {
			jCommander.parse(jCommArgs);
		} catch(ParameterException exception){
			System.out.println(exception.getMessage());
			jCommander.usage();
		}
		
		// settings
		String str = jCommDef.mailto;
		List<String> mailToList = Arrays.asList(str.split(","));
		
		// CHECK THESE 5 FIRST, THEN START INSTANCE
		
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
			compare.setSportID(new DEFAULT_SETTINGS().SPORT_ID);
		}
		
		if(jCommDef.interval > 0 && jCommDef.interval <= 30) {
			compare.setTimeInterval(jCommDef.interval);
		}else {
			compare.setTimeInterval(new DEFAULT_SETTINGS().INTERVAL_MIN);
		}
		
		if(jCommDef.percentage > 0 && jCommDef.percentage < 1) {
			compare.setPercent(jCommDef.percentage);
		}else {
			compare.setPercent(new DEFAULT_SETTINGS().PERCENT_MARGIN);
		}
		
		if(jCommDef.lowerMargins > 1.1 && jCommDef.lowerMargins < 50) {
			compare.setLowerMargin(jCommDef.lowerMargins);
		}else {
			compare.setLowerMargin(new DEFAULT_SETTINGS().LOWER_MARGIN);
		}
		
		if(jCommDef.upperMargin > 1.2 && jCommDef.upperMargin < 100) {
			compare.setUpperMargin(jCommDef.upperMargin);
		}else {
			compare.setUpperMargin(new DEFAULT_SETTINGS().UPPER_MARGIN);
		}
		
		if(jCommDef.checkLive != new DEFAULT_SETTINGS().CHECK_LIVE_EVENTS) {
			compare.setCheckLiveEvents(jCommDef.checkLive);
		}else {
			compare.setCheckLiveEvents(new DEFAULT_SETTINGS().CHECK_LIVE_EVENTS);
		}
		
		if(jCommDef.rangeMinutes > 0 && jCommDef.rangeMinutes < 1000) {
			compare.setTimeRange(jCommDef.rangeMinutes);
		}else {
			compare.setTimeRange(new DEFAULT_SETTINGS().TIME_RANGE);
		}
		
		
		System.out.println(jCommDef.username);
		System.out.println(jCommDef.password);
		System.out.println(jCommDef.mailfrom);
		System.out.println(jCommDef.mailfrompswd);
		System.out.println(jCommDef.mailto);
		System.out.println(jCommDef.checkLive);
		System.out.println(jCommDef.interval);
		System.out.println(jCommDef.lowerMargins);
		System.out.println(jCommDef.percentage);
		System.out.println(jCommDef.rangeMinutes);
		System.out.println(jCommDef.sport);
		System.out.println(jCommDef.upperMargin);
		
		
		compare.start();
	}	
}