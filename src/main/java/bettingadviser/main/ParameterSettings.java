package bettingadviser.main;

import java.util.List;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;

public class ParameterSettings{
		
    @Parameter
    public List<String> parameters = Lists.newArrayList();
 
    @Parameter(names = {"-u", "--user"}, 
    		required = true,
    		description = "Username for pinnacle")
    public String username = "pinnacleuser";
    
    @Parameter(names = {"-p" , "--pswd"},
    		required = true,
    		description = "Password for pinnacle")
    public String password = "pinnaclepassword";    
    
    @Parameter(names = {"-mf", "--mailfrom"}, 
    		required = true,
    		description = "Mailaddress to send from")
    public String mailfrom = "";
    
    @Parameter(names = {"-mfp", "--mailfrompswd"}, 
    		required = true,
    		description = "Password for mailaddress to send from")
    public String mailfrompswd = "";
 
    @Parameter(names = {"-mt", "--mailto"},
    		required=true,
    		description = "Comma-separated list of emailaddresses")
    public String mailto = "";
    
    @Parameter(names = {"-ti", "--interval"},
    		required=false,
    		description = "Time interval")
    public int interval = 5;    
    
    @Parameter(names = {"-perc", "--percentage"},
    		required=false,
    		description = "Set percentage")
    public double percentage = 0.97;
    
    @Parameter(names = {"-um", "--upper"},
    		required=false,
    		description = "Set upper margins")
    public double upperMargin = 1.3;
    
    @Parameter(names = {"-lm", "--lower"},
    		required=false,
    		description = "Set lower margins")
    public double lowerMargins = 3.4;
    
    @Parameter(names = {"-l", "--checklive"},
    		required=false,
    		description = "Compare live events to")
    public boolean checkLive = true;
    
    @Parameter(names = {"-tr", "--timerange"},
    		required=false,
    		description = "Set time range")
    public int rangeMinutes = 10;
    
    @Parameter(names = {"-s", "--sport"},
    		required=false,
    		description = "Set sport")
    public String sport = "";
}
