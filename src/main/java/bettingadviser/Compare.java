package bettingadviser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import mail.SendMail;
import pinnacle.api.dataobjects.Fixtures.Event;

/**
 * 
 * @author christian ryding
 */
public class Compare extends TimerTask{
	
	private final String mailTo, mailFrom, mailFromPassw;
	private final String username;
	private final String password;
	private PrintEventsAndOdds printOut;
	private GetEvents getEvents;
	private GetOdds getOdds;
	private ArrayList<Moneyline> currentEvents;
	private Timer timer;
	private int counter;
	private DecimalFormat df;
	private ArrayList<ArrayList<Moneyline>> lastEventsForCompare;
	private ArrayList<Moneyline> mailMoneylineEvent;
	private int SPORT_ID;
	private int INTERVAL_MIN;
	
	
	/**
	 * Constructor
	 * 
	 * @param username Username for Pinnacle API
	 * @param password Password for Pinnacle API
	 * @param mailTo
	 * @param mailFrom
	 * @param mailFromPassw
	 */
	public Compare(String username, String password, String mailTo, String mailFrom, String mailFromPassw) {
		timer = new Timer();
		this.username = username;
		this.password = password;
		this.mailTo = mailTo;
		this.mailFrom = mailFrom;
		this.mailFromPassw = mailFromPassw;
		counter = 0;
		lastEventsForCompare = new ArrayList<ArrayList<Moneyline>>();
		df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
		// default values
		SPORT_ID = 29;
		INTERVAL_MIN = 10;
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
	 * Abstract method TimerTask.run
	 */
	@Override
	public void run() {}
	
	/**
	 * Start timertask that runs every INTERVAL_MIN minutes
	 */
	public void start () {timer.scheduleAtFixedRate(task, 0, 1000*60*INTERVAL_MIN);}
	
	/**
	 * Every INTERVAL_MIN, check if better odds have appeared.
	 * 
	 * First,second and third iteration, add odds to list. 
	 * Fourth iteration add odds to list and compare odds from previous iterations.
	 */
	TimerTask task = new TimerTask() {
		
		@Override
		public void run() {
			printOut = new PrintEventsAndOdds();
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
					if(current.getAway() < 3.5 && current.getAway() > 1.2 && current.getAway() <= (0.9 * previous.getAway()) ){	
						
						// if event id do not exist, add it
						if(!m.exist(mailMoneylineEvent, current.getEventID())) {
							
							Moneyline eventInfo = new Moneyline();
							eventInfo.setEventInfoID(current.getAwayStr() + " | " 
																+ previous.getAway() + " -> " + current.getAway() 
																+ " (-" + df.format(100*(1-(current.getAway()/previous.getAway()))) + "%)" 
																+ "("+ min +"min)" + " | " + current.getCutoff());
							
							eventInfo.setEventID(current.getEventID());
							eventInfo.setLeagueID(current.getLeagueID());
							eventInfo.setSportID(SPORT_ID);
							eventInfo.setCutoff(current.getCutoff());
							eventInfo.setLiveStatus(current.getLiveStatus());
							mailMoneylineEvent.add(eventInfo);
						}
					}
					
					// check home odds
					if(current.getHome() < 3.5 && current.getHome() > 1.2 && current.getHome() <= (0.9 * previous.getHome()) ){	
						
						// if event id do not exist, add it
						if(!m.exist(mailMoneylineEvent, current.getEventID())) {
							
							Moneyline eventInfo = new Moneyline();
							eventInfo.setEventInfoID(current.getHomeStr() + " | "  
														+ " " + previous.getHome() + " -> " + current.getHome() 
														+ " (-"	+ df.format(100*(1-(current.getHome()/previous.getHome()))) + "%)" 
														+ "("+ min + "min)" + " | " + current.getCutoff());
							
							eventInfo.setEventID(current.getEventID());
							eventInfo.setLeagueID(current.getLeagueID());
							eventInfo.setSportID(SPORT_ID);
							eventInfo.setCutoff(current.getCutoff());
							eventInfo.setLiveStatus(current.getLiveStatus());
							mailMoneylineEvent.add(eventInfo);
						}
					}
					
					// check draw odds
					if(current.getDraw() < 3.5 && current.getDraw() > 1.2 && current.getDraw() <= (0.9 * previous.getDraw()) ){
						
						// if event id do not exist, add it
						if(!m.exist(mailMoneylineEvent, current.getEventID())) {
							
							Moneyline eventInfo = new Moneyline();
							eventInfo.setEventInfoID("Draw -> Home: " + current.getHomeStr() + " Away: " + current.getAwayStr() + " | "	
														+ previous.getDraw() + " -> " + current.getDraw() 
														+ " (-" + df.format(100 * (1-(current.getDraw()/previous.getDraw()))) + "%)" 
														+ "(" + min + "min)" + " | " + current.getCutoff());
							
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


