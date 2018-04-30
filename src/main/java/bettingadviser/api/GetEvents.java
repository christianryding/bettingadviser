package bettingadviser.api;

import java.util.ArrayList;
import java.util.List;
import pinnacle.api.Parameter;
import pinnacle.api.PinnacleAPI;
import pinnacle.api.PinnacleException;
import pinnacle.api.dataobjects.Fixtures;
import pinnacle.api.dataobjects.Fixtures.Event;
import pinnacle.api.dataobjects.Fixtures.League;

/**
 * Get fixture events from PinnacleAPI	
 * 		
 * @author christian ryding
 */
public class GetEvents {

	private PinnacleAPI api;
	private ArrayList<Event> fixtEvents;
	
	/**
	 * Constructor
	 * 
	 * @param username for pinnacle
	 * @param password for pinnacle
	 */
	public GetEvents(String username, String password){

		try {
			api = PinnacleAPI.open(username, password);
		} catch (PinnacleException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get list of fixture events with sport specified sport ID
	 * 
	 * @param sportID id number of sport to get events for
	 * @return list of events
	 */
	public ArrayList<Event> getListOfEvents(int sportID) {
		
		Fixtures fixtures = null;
		fixtEvents = new ArrayList<Event>();
		Parameter parameter = Parameter.newInstance();
		parameter.sportId(sportID);
		parameter.since(0);
		
		// get events
		try {
			fixtures = api.getFixturesAsObject(parameter);
		} catch (PinnacleException e) {
			e.printStackTrace();
		}
		
		// save events
		List<League> leagues = fixtures.league();			// list of leagues
		for(int i = 0; i < leagues.size(); i++) {	
			List<Event> events = leagues.get(i).events();	// list of events
			for(int j = 0; j < events.size(); j++) {
				fixtEvents.add(events.get(j));				// add event
			}
		}
		
		// return events
		return fixtEvents;
	}
}
