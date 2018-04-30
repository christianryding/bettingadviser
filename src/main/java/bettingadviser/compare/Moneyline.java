package bettingadviser;

import java.util.ArrayList;

import pinnacle.api.enums.LIVE_STATUS;

/**
 * Holds information regarding moneyline event
 * 
 * @author christian ryding
 */
public class Moneyline {

	private long eventID;
	private int leagueID;
	private double draw = Double.NaN;
	private double home = Double.NaN;
	private double away = Double.NaN;
	private String homeStr = null;
	private String awayStr = null;
	private String cutoff = null;
    private String eventInfo;
    private int sportID;
    private LIVE_STATUS liveStatus;
    
	/**
	 * Constructor
	 */
	public Moneyline() {}
	
	/**
	 * Constructor 
	 * 
	 * @param eventID id number for event
	 * @param draw odds for draw
	 * @param home odds for home team
	 * @param away odds for away team
	 * @param homeStr home team name
	 * @param awayStr away team name
	 */
	public Moneyline(long eventID, double draw, double home, double away, String homeStr, String awayStr) {
		this.eventID = eventID;
		this.draw = draw;
		this.home = home;
		this.away = away;
		this.homeStr = homeStr;
		this.awayStr = awayStr;
		this.sportID = 29;
		this.liveStatus = LIVE_STATUS.UNDEFINED;//default value
		
	}
	
	/**
	 * Constructor
	 * 
	 * @param eventID
	 * @param draw
	 * @param home
	 * @param away
	 */
	public Moneyline(long eventID, double draw, double home, double away) {
		this.eventID = eventID;
		this.draw = draw;
		this.home = home;
		this.away = away;
	}
	
    /**
     * Check if eventID exist already
     * 
     * @param mailMoneylineEvents
     * @param eventID
     * @return
     */
    public boolean exist(ArrayList<Moneyline> mailMoneylineEvents, long eventID) {
    	for(Moneyline pe : mailMoneylineEvents) {
    		if(pe.getEventID() == eventID) {
    			return true;
    		}
    	}
    	return false;
    }
    
	// getters
	public long getEventID() {return this.eventID;}
	public int getLeagueID() {return this.leagueID;}
	public double getDraw() {return this.draw;}
	public double getHome() {return this.home;}
	public double getAway() {return this.away;}
	public String getHomeStr() {return this.homeStr;}
	public String getAwayStr() {return this.awayStr;}
	public String getCutoff() {return this.cutoff;}
    public String getEventInfo() { return this.eventInfo; }
    public int getSportID() { return this.sportID; }
    public LIVE_STATUS getLiveStatus() { return liveStatus; }
	
	// setters
	public void setEventID(long eventID) {this.eventID = eventID;}
	public void setLeagueID(int leagueID) {this.leagueID = leagueID;}
	public void setDraw(double draw) {this.draw = draw;}
	public void setHome(double home) {this.home = home;}
	public void setAway(double away) {this.away = away;}
	public void setHomeStr(String home) {this.homeStr = home;}
	public void setAwayStr(String away) {this.awayStr = away;}
	public void setCutoff(String cutoff) {this.cutoff = cutoff;}
    public void setEventInfoID(String eventInfo) { this.eventInfo = eventInfo; }
    public void setSportID(int sportID) {this.sportID = sportID;}
    public void setLiveStatus(LIVE_STATUS liveStatus) {  this.liveStatus = liveStatus; }
	
}
