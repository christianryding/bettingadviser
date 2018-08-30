package bettingadviser.compare;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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
	private final List<String> mailTo;
	private int counter;
	private ParseEventsAndOdds printOut;
	private GetEvents getEvents;
	private GetOdds getOdds;
	private Timer timer;
	private DecimalFormat df;// print out nicely
	private ArrayList<ArrayList<Moneyline>> lastEventsForCompare;
	private ArrayList<Moneyline> mailMoneylineEvent;
	private ArrayList<Moneyline> currentEvents;
	private int sportID;
	private int interval_min;
	private double percent_margin;
	private double upper_margin;
	private double lower_margin;
	private boolean check_live_events;
	private int time_range;
	
	
	/**
	 * Constructor, initialize values
	 * 
	 * @param username Username for Pinnacle API
	 * @param password Password for Pinnacle API
	 * @param mailTo
	 * @param mailFrom
	 * @param mailFromPassw
	 */
	public Compare(String username, String password, String mailFrom, String mailFromPassw, List<String> mailTo) {
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
	}

	
	/**
	 * Set sport to get events and odds for 
	 * 
	 * @param sportID
	 */
	public void setSportID(int sportID) { this.sportID = sportID; }
	
	
	/**
	 * Set interval rate for getting and comparing odds
	 * 
	 * @param interval
	 */
	public void setTimeInterval(int interval) { this.interval_min = interval; }
	
	
	/**
	 * Set percent value 
	 * 
	 * @param percentValue
	 */
	public void setPercent(double percentValue) { this.percent_margin = percentValue; }
	
	
	/**
	 * Set upper margin
	 * 
	 * @param margin
	 */
	public void setUpperMargin(double margin) { this.upper_margin = margin; }
	
	
	/**
	 * Set lower margin
	 * 
	 * @param margin
	 */
	public void setLowerMargin(double margin) { this.lower_margin = margin; }
	
	
	/**
	 * Set if live events should be covered
	 * 
	 * @param checkLiveEvents
	 */
	public void setCheckLiveEvents(boolean checkLiveEvents){ this.check_live_events = checkLiveEvents; }
	
	
	/**
	 * Set time range for events
	 * 
	 * @param timeRange
	 */
	public void setTimeRange(int timeRange) { this.time_range = timeRange; }
	
	
	/**
	 * Abstract method TimerTask.run
	 */
	@Override
	public void run() {}
	
	
	/**
	 * Start timertask that runs every INTERVAL_MIN minutes
	 */
	public void start () {timer.scheduleAtFixedRate(task, 0, 1000*60*interval_min);}
	
	
	/**
	 * Every INTERVAL_MIN after third iteration, check if better odds have appeared by comparing them to older odds.
	 */
	TimerTask task = new TimerTask() {
		
		@Override
		public void run() {
			printOut = new ParseEventsAndOdds(check_live_events);
			getEvents = new GetEvents(username, password);
			getOdds = new GetOdds(username, password);
			currentEvents = new ArrayList<Moneyline>();
			mailMoneylineEvent = new ArrayList<Moneyline>();
			
			// get events
			ArrayList<Event> fixtureEvents = new ArrayList<Event>(); 
			fixtureEvents = getEvents.getListOfEvents(sportID);
		
			//  get odds
			String jsonOdds = null;
			jsonOdds = getOdds.getListOfEvents(sportID);
			
			// print/save odds and event information
			currentEvents = printOut.parseAndPrint(fixtureEvents, jsonOdds);	
			
			// fourth iteration, compare 5, 10 and 15 minutes old odds
			if(counter > 2) {
				
				// add events to list
				lastEventsForCompare.add(currentEvents);
				
				// compare and get list of 5 minutes good odds
				compareEvents(lastEventsForCompare.get(0), lastEventsForCompare.get(1), interval_min);
				// compare and get list of 10 minutes good odds
				compareEvents(lastEventsForCompare.get(0), lastEventsForCompare.get(2), interval_min*2);
				// compare and get list of 15 minutes good odds
				compareEvents(lastEventsForCompare.get(0), lastEventsForCompare.get(3), interval_min*3);
				
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
					if(current.getAway() < upper_margin && current.getAway() > lower_margin && current.getAway() <= (percent_margin * previous.getAway()) ){	
						
						// if event id do not exist, and is in time range, add moneylineinfo to list
						if(!m.exist(mailMoneylineEvent, current.getEventID()) && inTimeRange(current)) {
							Moneyline eventInfo = new Moneyline();
							eventInfo.setEventInfoID(current.getAwayStr() + " | " + previous.getAway() + " -> " + current.getAway() 
																+ " (-" + df.format(100*(1-(current.getAway()/previous.getAway()))) + "%)" 
																+ "("+ min +"min)");
							eventInfo.setAwayStr(current.getAwayStr());
							eventInfo.setHomeStr(current.getHomeStr());
							eventInfo.setEventID(current.getEventID());
							eventInfo.setLeagueID(current.getLeagueID());
							eventInfo.setSportID(sportID);
							eventInfo.setCutoff(current.getCutoff());
							eventInfo.setLiveStatus(current.getLiveStatus());
							mailMoneylineEvent.add(eventInfo);
						}
					}
					
					// check home odds
					if(current.getHome() < upper_margin && current.getHome() > lower_margin && current.getHome() <= (percent_margin * previous.getHome()) ){	
						
						// if event id do not exist, and is in time range, add moneylineinfo to list
						if(!m.exist(mailMoneylineEvent, current.getEventID()) && inTimeRange(current)) {
							Moneyline eventInfo = new Moneyline();
							eventInfo.setEventInfoID(current.getHomeStr() + " | "  + " " + previous.getHome() + " -> " + current.getHome() 
														+ " (-"	+ df.format(100*(1-(current.getHome()/previous.getHome()))) + "%)" 
														+ "("+ min + "min)");
							eventInfo.setAwayStr(current.getAwayStr());
							eventInfo.setHomeStr(current.getHomeStr());
							eventInfo.setEventID(current.getEventID());
							eventInfo.setLeagueID(current.getLeagueID());
							eventInfo.setSportID(sportID);
							eventInfo.setCutoff(current.getCutoff());
							eventInfo.setLiveStatus(current.getLiveStatus());
							mailMoneylineEvent.add(eventInfo);
						}
					}
					
					// check draw odds
					if(current.getDraw() < upper_margin && current.getDraw() > lower_margin && current.getDraw() <= (percent_margin * previous.getDraw()) ){
						
						// if event id do not exist, and is in time range, add it
						if(!m.exist(mailMoneylineEvent, current.getEventID()) && inTimeRange(current)) {
							Moneyline eventInfo = new Moneyline();
							eventInfo.setEventInfoID("Draw -> Home: " + current.getHomeStr() + " Away: " + current.getAwayStr() + " | "	
														+ previous.getDraw() + " -> " + current.getDraw() 
														+ " (-" + df.format(100 * (1-(current.getDraw()/previous.getDraw()))) + "%)" 
														+ "(" + min + "min)");
							eventInfo.setAwayStr(current.getAwayStr());
							eventInfo.setHomeStr(current.getHomeStr());
							eventInfo.setEventID(current.getEventID());
							eventInfo.setLeagueID(current.getLeagueID());
							eventInfo.setSportID(sportID);
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
	
	/**
	 * See if event is within given time range
	 * 
	 * @param m Moneyline event
	 * @return true if event is within TIME_RANGE, false if not
	 */
	public boolean inTimeRange(Moneyline m) {
		
		// get current time and start time for event
		Instant instantNow = Instant.now();
		Instant i2; 
		i2 = Instant.parse(m.getCutoff());
		
		// calculate time left to game starts
		long minutesToGame = Duration.between(instantNow, i2).toMinutes();
		long hoursToGame = minutesToGame / 60;
		minutesToGame = minutesToGame % 60;
		
		// check if event gamestart is under TIME_RANGE given
		if(hoursToGame < time_range && minutesToGame >= 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
}// class ends


