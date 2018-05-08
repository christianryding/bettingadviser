package bettingadviser.compare;

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
	private double draw;
	private double home;
	private double away;
	private String homeStr;
	private String awayStr;
	private String cutoff;
    private String eventInfo;
    private int sportID;
    private LIVE_STATUS liveStatus;
    
	/**
	 * Constructor
	 */
	public Moneyline() {
		draw = Double.NaN;
		home = Double.NaN;
		away = Double.NaN;
		homeStr = null;
		awayStr = null;
		cutoff = null;
		eventInfo = null;
	}
	
    /**
     * Check if eventID exist in arraylist
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
