package bettingadviser.main;

import java.util.List;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;
import bettingadviser.enums.DefaultSettings;

public class ParameterSettings{
		
    @Parameter
    public List<String> parameters = Lists.newArrayList();
 
    @Parameter(names = {"-u", "--user"}, 
    		required = true,
    		description = "Username for pinnacle")
    public String username;
    
    @Parameter(names = {"-p" , "--pswd"},
    		required = true,
    		description = "Password for pinnacle")
    public String password;    
    
    @Parameter(names = {"-mf", "--mailfrom"}, 
    		required = true,
    		description = "Mailaddress to send from")
    public String mailfrom;
    
    @Parameter(names = {"-mfp", "--mailfrompswd"}, 
    		required = true,
    		description = "Password for mailaddress to send from")
    public String mailfrompswd;
 
    @Parameter(names = {"-mt", "--mailto"},
    		required=true,
    		description = "Comma-separated list of emailaddresses")
    public String mailto;
    
    @Parameter(names = {"-ti", "--interval"},
    		required=false,
    		description = "Time interval")
    public int interval;    
    
    @Parameter(names = {"-perc", "--percentage"},
    		required=false,
    		description = "Set percentage")
    public double percentage;
    
    @Parameter(names = {"-um", "--upper"},
    		required=false,
    		description = "Set upper margins")
    public double upperMargin;
    
    @Parameter(names = {"-lm", "--lower"},
    		required=false,
    		description = "Set lower margins")
    public double lowerMargins;
    
    @Parameter(names = {"-l", "--checklive"},
    		required=false,
    		description = "Compare live events to")
    public boolean checkLive;
    
    @Parameter(names = {"-tr", "--timerange"},
    		required=false,
    		description = "Set time range")
    public int rangeMinutes;
    
    @Parameter(names = {"-s", "--sport"},
    		required=false,
    		description = "Set sport")
    public int sport;
}
