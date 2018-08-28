package bettingadviser.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.beust.jcommander.DynamicParameter;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;

public class JCommanderDefinitions{
    @Parameter
    public List<String> parameters = Lists.newArrayList();
 
    @Parameter(names = "-username", 
    		required = true,
    		description = "Username for pinnacle")
    public String username = "";
    
    @Parameter(names = "-password", 
    		required = true,
    		description = "Password for pinnacle")
    public String password = "";    
    
    @Parameter(names = "-mailfrom", 
    		required = true,
    		description = "Mailaddress to send from")
    public String mailfrom = "";
    
    @Parameter(names = "-mailfrompswd", 
    		required = true,
    		description = "Password for mailaddress to send from")
    public String mailfrompswd = "";
    
    
    @Parameter(names = { "-log", "-verbose" }, 
    		description = "Level of verbosity")
    public Integer verbose = 1;
 
    @Parameter(names = "-mailto",
    		required=true,
    		description = "Comma-separated list of emailaddresses")
    public String mailto;
 

    

    @DynamicParameter(names = "-D", 
    		description = "Dynamic parameters go here")
    public Map<String, String> dynamicParams = new HashMap<String, String>();
}
