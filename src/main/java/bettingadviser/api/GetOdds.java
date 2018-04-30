package bettingadviser.api;

import pinnacle.api.Parameter;
import pinnacle.api.PinnacleAPI;
import pinnacle.api.PinnacleException;
import pinnacle.api.enums.ODDS_FORMAT;

/**
 * Get odds from PinnacleAPI in JSON format
 * 
 * @author christian ryding
 */
public class GetOdds {

	private PinnacleAPI api;
	
	/**
	 * Constructor
	 * 
	 * @param username for pinnacle
	 * @param password
	 */
	public GetOdds(String username, String password) {
		
		try {
			api = PinnacleAPI.open(username, password);
		} catch (PinnacleException e) {
			e.printStackTrace();
		}	
	}
	

	/**
	 * Get odds for events
	 * 
	 * @return JSON string of odds for events
	 */
	public String getListOfEvents(int sportID){
		
		Parameter parameterOdds;
		String jsonOdds = null;
		
		try {
			parameterOdds = Parameter.newInstance();
			parameterOdds.sportId(sportID);
			parameterOdds.since(0);
			parameterOdds.oddsFormat(ODDS_FORMAT.DECIMAL);
			jsonOdds = api.getOddsAsJson(parameterOdds);
		}catch (PinnacleException e) {
			e.printStackTrace();
		}
		
		return jsonOdds;
	}
}