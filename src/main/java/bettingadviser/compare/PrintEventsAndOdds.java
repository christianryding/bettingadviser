package bettingadviser.compare;

import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import pinnacle.api.dataobjects.Fixtures.Event;
import pinnacle.api.enums.LIVE_STATUS;

/**
 * 
 * @author christian ryding
 */
public class PrintEventsAndOdds {

	private ArrayList<Event> fixtEvents;
	private ArrayList<Moneyline> compareEvents;

	
	/**
	 * Constructor
	 * 
	 * Initialize arrayslists
	 */
	public PrintEventsAndOdds(){
		compareEvents = new ArrayList<Moneyline>();
		fixtEvents = new ArrayList<Event>();
	}
	

	/**
	 * Parse JSON file of odds and list of events and print them out
	 * 
	 * @param fixtEvents List of fixture events
	 * @param jsonOdds List of odds in JSON format
	 * @return
	 */
	public ArrayList<Moneyline> parseAndPrint(ArrayList<Event> fixtEvents, String jsonOdds) {
		
		this.fixtEvents = fixtEvents;
		compareEvents.clear();
		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = jsonParser.parse(jsonOdds).getAsJsonObject();
		JsonArray leagueArr = null;
		JsonObject leagueObj = null;
		
		long sportID = jsonObject.get("sportId").getAsLong();
		System.out.println("Sport ID: " + sportID);
		long last = jsonObject.get("last").getAsLong();
		System.out.println("Last: " + last);

		if(jsonObject.has("leagues")){
			leagueArr = jsonObject.get("leagues").getAsJsonArray();  
			
			// for every league
			for(int i = 0; i<leagueArr.size(); i++) {		
				leagueObj = leagueArr.get(i).getAsJsonObject();
				checkEvents(leagueObj);
			}	
		}

		return compareEvents;
	}
	
	
	/**
	 * Print out event ID and who plays, after that check events period
	 * 
	 * @param leagueObj
	 */
	public void checkEvents(JsonObject leagueObj) {	
		
		JsonObject eventsObj = null;
		
		if(leagueObj.has("events")){
			
			JsonArray eventsArr = leagueObj.get("events").getAsJsonArray();
			eventsObj = eventsArr.get(0).getAsJsonObject();
			
			String homeStr = null;
			String awayStr = null;
			LIVE_STATUS liveStatus = null;
			
			for(int i = 0; i<eventsArr.size(); i++) {
				
				eventsObj = eventsArr.get(i).getAsJsonObject();
				boolean addEvent = false;
				
				for (Event e : fixtEvents) {						
					if(e.id().equals(eventsObj.get("id").getAsLong())) {	
						
						// add event that will have live betting
						if(e.liveStatus().get().equals(LIVE_STATUS.WILL_BE_OFFERED)) {
							liveStatus = e.liveStatus().get();
							addEvent = true;
						}
						// add event with no live betting
						else if(e.liveStatus().get().equals(LIVE_STATUS.NO_LIVE_BETTING)) {
							liveStatus = e.liveStatus().get();
							addEvent = true;
						}
						// add live event
						else if(e.liveStatus().get().equals(LIVE_STATUS.LIVE_BETTING)) {
							//liveStatus = e.liveStatus().get();
							//addEvent = true;
						}
							
						homeStr = e.home().toString();
						awayStr = e.away().toString();
						break;
					}
				}
				
				if(addEvent == true) {
					JsonObject periodsObj = null;
					if(eventsObj.has("periods")){
						JsonArray periodsArr = eventsObj.get("periods").getAsJsonArray();
						periodsObj = periodsArr.get(0).getAsJsonObject();
						checkMoneyline(periodsObj, eventsObj.get("id").getAsLong(), homeStr, awayStr,
								periodsObj.get("cutoff").getAsString(), leagueObj.get("id").getAsInt(), liveStatus);
					}	
				}
			}	
		}
	}
	
	/**
	 * Print out moneyline for event
	 * 
	 * @param periodsObj
	 */
	public void checkMoneyline(JsonObject periodsObj, long eventID, String home, String away, String cutoff, int leagueID, LIVE_STATUS liveStatus) {
		
		if(periodsObj.has("moneyline")){
			
			JsonObject moneyLineObj = periodsObj.get("moneyline").getAsJsonObject();

			if(moneyLineObj.get("draw") != null){
				Moneyline moneyline = new Moneyline(eventID, moneyLineObj.get("draw").getAsDouble(),
													moneyLineObj.get("home").getAsDouble(), moneyLineObj.get("away").getAsDouble());
				moneyline.setHomeStr(home);
				moneyline.setAwayStr(away);
				moneyline.setCutoff(cutoff);
				moneyline.setLeagueID(leagueID);
				moneyline.setLiveStatus(liveStatus);
				compareEvents.add(moneyline);
			}
			else {
				Moneyline moneyline = new Moneyline(eventID, Double.NaN, moneyLineObj.get("home").getAsDouble(),
													moneyLineObj.get("away").getAsDouble());
				moneyline.setHomeStr(home);
				moneyline.setAwayStr(away);
				moneyline.setCutoff(cutoff);
				moneyline.setLeagueID(leagueID);
				moneyline.setLiveStatus(liveStatus);
				compareEvents.add(moneyline);
			}
		}
	}
	
}// class PrintEventsAndOdds ends
	
	
//	/**
//	 * Check if totals, spread and moneyline exist for event, then print it out alongside cutoff
//	 * 
//	 * @param eventsObj
//	 */
//	public void checkPeriods(JsonObject eventsObj, String home, String away, int leagueID) {
//		JsonObject periodsObj = null;
//		
//		if(eventsObj.has("periods")){
//			JsonArray periodsArr = eventsObj.get("periods").getAsJsonArray();
//			periodsObj = periodsArr.get(0).getAsJsonObject();
//			//checkTotals(periodsObj);
//			//checkSpread(periodsObj);
//			//checkMoneyline(periodsObj, eventsObj.get("id").getAsLong(), home, away, periodsObj.get("cutoff").getAsString(), leagueID);
//			//System.out.println("Period cutoff: " + periodsObj.get("cutoff"));// pst(webpage) -> cutoff = (+7h) 
//		}
//	}	
	
//	/**
//	 * Print out every totals for event
//	 * 
//	 * @param periodsObj
//	 */
//	public void checkTotals(JsonObject periodsObj) {
//		if(periodsObj.has("totals")){ 
//			JsonArray totalArr = periodsObj.get("totals").getAsJsonArray();
//			JsonObject totalObj = totalArr.get(0).getAsJsonObject();
//			
//			for(int i = 0; i < totalArr.size(); i++) {
//				totalArr = periodsObj.get("totals").getAsJsonArray();
//				totalObj = totalArr.get(i).getAsJsonObject();
//				System.out.println("Totals \t\tpoints: " + totalObj.get("points")
//								+ "\tover: " + totalObj.get("over")
//								+ "\tunder: " + totalObj.get("under"));
//			}
//		}
//	}

//	/**
//	 * Print out every spread for event
//	 *  
//	 * @param periodsObj
//	 */
//	 public void checkSpread(JsonObject periodsObj) {
//		if(periodsObj.has("spreads")){
//			JsonArray spreadsArr = periodsObj.get("spreads").getAsJsonArray();
//			JsonObject spreadsObj = spreadsArr.get(0).getAsJsonObject();
//		
//			for(int i = 0; i < spreadsArr.size(); i++) {
//				spreadsArr = periodsObj.get("spreads").getAsJsonArray();
//				spreadsObj = spreadsArr.get(i).getAsJsonObject();
//				System.out.println("Spreads \thandicap: " + spreadsObj.get("hdp") 
//						+ "\thome: " + spreadsObj.get("home") 
//						+ "\taway: " + spreadsObj.get("away"));
//			}
//		}
//	}
	

