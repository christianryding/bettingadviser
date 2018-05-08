package bettingadviser.compare;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import bettingadviser.api.GetEvents;
import bettingadviser.api.GetOdds;
import bettingadviser.mail.SendMail;
import bettingadviser.compare.ParseEventsAndOdds;
import pinnacle.api.dataobjects.Fixtures.Event;

/**
 * 
 * @author christian ryding
 */
public class Compare extends TimerTask{
	
	private final String mailFrom, mailFromPassw, username, password;// constructor values
	private final ArrayList<String> mailTo;
	private int counter;
	private ParseEventsAndOdds printOut;
	private GetEvents getEvents;
	private GetOdds getOdds;
	private Timer timer;
	private DecimalFormat df;// print out nicely
	private ArrayList<ArrayList<Moneyline>> lastEventsForCompare;
	private ArrayList<Moneyline> mailMoneylineEvent;
	private ArrayList<Moneyline> currentEvents;
	
	// default values
	private int SPORT_ID;
	private int INTERVAL_MIN;
	private double PERCENT_MARGIN;
	private double UPPER_MARGIN;
	private double LOWER_MARGIN;
	private boolean CHECK_LIVE_EVENTS;
	
	
	/**
	 * Constructor, initialize values
	 * 
	 * @param username Username for Pinnacle API
	 * @param password Password for Pinnacle API
	 * @param mailTo
	 * @param mailFrom
	 * @param mailFromPassw
	 */
	public Compare(String username, String password, String mailFrom, String mailFromPassw, ArrayList<String> mailTo) {
		timer = new Timer();
		this.username = username;
		this.password = password;
		this.mailTo = mailTo;
		this.mailFrom = mailFrom;
		this.mailFromPassw = mailFromPassw;
		
		counter = 0; // iteration counter
		lastEventsForCompare = new ArrayList<ArrayList<Moneyline>>(); // holds previous odds for comparison
		df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
		// default values
		SPORT_ID = 29;
		INTERVAL_MIN = 10;
		PERCENT_MARGIN = 0.9;
		UPPER_MARGIN = 3.5;
		LOWER_MARGIN = 1.2;
		CHECK_LIVE_EVENTS = false;
	}

	
	/**
	 * Set sport to get events and odds for 
	 * 
	 * @param sportID
	 */
	public void setSportID(int sportID) { SPORT_ID = sportID; }
	
	
	/**
	 * Set interval rate for getting and comparing odds
	 * 
	 * @param interval
	 */
	public void setTimeInterval(int interval) { INTERVAL_MIN = interval; }
	
	
	/**
	 * Set percent value 
	 * 
	 * @param percentValue
	 */
	public void setPercent(double percentValue) { PERCENT_MARGIN = percentValue; }
	
	
	/**
	 * Set upper margin
	 * 
	 * @param margin
	 */
	public void setUpperMargin(double margin) { UPPER_MARGIN = margin; }
	
	
	/**
	 * Set lower margin
	 * 
	 * @param margin
	 */
	public void setLowerMargin(double margin) { LOWER_MARGIN = margin; }
	
	
	/**
	 * Set if live events should be covered
	 * 
	 * @param checkLiveEvents
	 */
	public void setCheckLiveEvents(boolean checkLiveEvents){ CHECK_LIVE_EVENTS = checkLiveEvents; }
	
	
	/**
	 * Abstract method TimerTask.run
	 */
	@Override
	public void run() {}
	
	
	/**
	 * Start timertask that runs every INTERVAL_MIN minutes
	 */
	public void start () {timer.scheduleAtFixedRate(task, 0, 1000*60*INTERVAL_MIN);}
	
	
	/**
	 * Every INTERVAL_MIN after third iteration, check if better odds have appeared.
	 * First,second and third iteration, add odds to list. 
	 * Fourth iteration add odds to list and compare odds from previous iterations.
	 */
	TimerTask task = new TimerTask() {
		
		@Override
		public void run() {
			printOut = new ParseEventsAndOdds(CHECK_LIVE_EVENTS);
			getEvents = new GetEvents(username, password);
			getOdds = new GetOdds(username, password);
			currentEvents = new ArrayList<Moneyline>();
			mailMoneylineEvent = new ArrayList<Moneyline>();
			
			// get events
			ArrayList<Event> fixtureEvents = new ArrayList<Event>(); 
			fixtureEvents = getEvents.getListOfEvents(SPORT_ID);
		
			//  get odds
			String jsonOdds = null;
			jsonOdds = getOdds.getListOfEvents(SPORT_ID);
			
			// print/save odds and event information
			currentEvents = printOut.parseAndPrint(fixtureEvents, jsonOdds);	
			
			// fourth iteration, compare 5, 10 and 15 minutes old odds
			if(counter > 2) {
				
				// add events to list
				lastEventsForCompare.add(currentEvents);
				
				// compare and get list of 5 minutes good odds
				compareEvents(lastEventsForCompare.get(0), lastEventsForCompare.get(1), INTERVAL_MIN);
				// compare and get list of 10 minutes good odds
				compareEvents(lastEventsForCompare.get(0), lastEventsForCompare.get(2), INTERVAL_MIN*2);
				// compare and get list of 15 minutes good odds
				compareEvents(lastEventsForCompare.get(0), lastEventsForCompare.get(3), INTERVAL_MIN*3);
				
				// remove old list of events
				lastEventsForCompare.remove(0);
			}
			
			//first iterations, add events to list and compare and get list of 5 minutes good odds
			else if(counter <= 2) {
				counter++;
				lastEventsForCompare.add(currentEvents);
			}	
			
			// if good odds exist, send mail
			if(mailMoneylineEvent.size() > 0) {
				SendMail sm = new SendMail(mailTo, mailFrom, mailFromPassw);
				sm.sendEvents(mailMoneylineEvent);
			}
		}
	};
	
	
	/**
	 * Compare events old and new odds, if better odds are given add it to list
	 * 
	 * @param previousEvents list of old events
	 * @param currentEvents list of current events
	 * @param min how old compared events are
	 */
	public void compareEvents(ArrayList<Moneyline> previousEvents, ArrayList<Moneyline> currentEvents, int min) {
		
		Moneyline m = new Moneyline();

		// for every event, check if there is a better odds
		for (Moneyline current : currentEvents) {
			for (Moneyline previous : previousEvents) {
				
				// same event for comparison
				if(current.getEventID() == previous.getEventID()) {
					
					// check away odds
					if(current.getAway() < UPPER_MARGIN && current.getAway() > LOWER_MARGIN && current.getAway() <= (PERCENT_MARGIN * previous.getAway()) ){	
						
						// if event id do not exist, add moneylineinfo to list
						if(!m.exist(mailMoneylineEvent, current.getEventID())) {
							Moneyline eventInfo = new Moneyline();
							eventInfo.setEventInfoID(current.getAwayStr() + " | " + previous.getAway() + " -> " + current.getAway() 
																+ " (-" + df.format(100*(1-(current.getAway()/previous.getAway()))) + "%)" 
																+ "("+ min +"min)");
							eventInfo.setEventID(current.getEventID());
							eventInfo.setLeagueID(current.getLeagueID());
							eventInfo.setSportID(SPORT_ID);
							eventInfo.setCutoff(current.getCutoff());
							eventInfo.setLiveStatus(current.getLiveStatus());
							mailMoneylineEvent.add(eventInfo);
						}
					}
					
					// check home odds
					if(current.getHome() < UPPER_MARGIN && current.getHome() > LOWER_MARGIN && current.getHome() <= (PERCENT_MARGIN * previous.getHome()) ){	
						
						// if event id do not exist, add moneylineinfo to list
						if(!m.exist(mailMoneylineEvent, current.getEventID())) {
							Moneyline eventInfo = new Moneyline();
							eventInfo.setEventInfoID(current.getHomeStr() + " | "  + " " + previous.getHome() + " -> " + current.getHome() 
														+ " (-"	+ df.format(100*(1-(current.getHome()/previous.getHome()))) + "%)" 
														+ "("+ min + "min)");
							eventInfo.setEventID(current.getEventID());
							eventInfo.setLeagueID(current.getLeagueID());
							eventInfo.setSportID(SPORT_ID);
							eventInfo.setCutoff(current.getCutoff());
							eventInfo.setLiveStatus(current.getLiveStatus());
							mailMoneylineEvent.add(eventInfo);
						}
					}
					
					// check draw odds
					if(current.getDraw() < UPPER_MARGIN && current.getDraw() > LOWER_MARGIN && current.getDraw() <= (PERCENT_MARGIN * previous.getDraw()) ){
						
						// if event id do not exist, add it
						if(!m.exist(mailMoneylineEvent, current.getEventID())) {
							
							Moneyline eventInfo = new Moneyline();
							eventInfo.setEventInfoID("Draw -> Home: " + current.getHomeStr() + " Away: " + current.getAwayStr() + " | "	
														+ previous.getDraw() + " -> " + current.getDraw() 
														+ " (-" + df.format(100 * (1-(current.getDraw()/previous.getDraw()))) + "%)" 
														+ "(" + min + "min)");
							eventInfo.setEventID(current.getEventID());
							eventInfo.setLeagueID(current.getLeagueID());
							eventInfo.setSportID(SPORT_ID);
							eventInfo.setCutoff(current.getCutoff());
							eventInfo.setLiveStatus(current.getLiveStatus());
							mailMoneylineEvent.add(eventInfo);
						}
					}
					break;
				}
			}	
		}
	}
	
}// class ends


