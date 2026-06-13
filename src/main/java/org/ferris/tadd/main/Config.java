package org.ferris.tadd.main;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 *
 * @author Michael
 */
public class Config {
   
    protected String resendApiKey;
    protected boolean sendEmail;
    protected boolean showsForToday;
    protected int showsForDays;

    public String getResendApiKey() {
        return resendApiKey;
    }

    public boolean isSendEmail() {
        return sendEmail;
    }
    
    public int getShowsForDays() {
        return showsForDays;
    }
    
    public boolean isShowsForToday() {
        return showsForToday;
    }
    
    public Config() {
        Properties props = new Properties();
        
        // local development
        if (props.isEmpty()) {
            try {
                InputStream is 
                    = getClass().getResourceAsStream("/tadd.properties");
                props.load(is);  
            } catch (Exception ignore) {
                ignore.printStackTrace(System.out);
            }
        }
        
        // production server "ferris.project.home"
        if (props.isEmpty()) {
            try {           
                Path confPath = Path.of(
                      System.getProperty("ferris.project.home")
                    , "conf"
                    , "tadd.properties"
                );

                InputStream is 
                    = Files.newInputStream(confPath);
                props.load(is);
            } catch (Exception ignore) {
                ignore.printStackTrace(System.out);
            }
        }
        
        if (props.isEmpty()) {
            throw new RuntimeException(
                "Configuration failed to load"
            );
        }
        
        resendApiKey = props.getProperty("resendApiKey", "").trim();
        if (resendApiKey.isEmpty()) {
            throw new RuntimeException(
                "Configuation failed to find resendApiKey"
            );
        }
        
        sendEmail = "true".equalsIgnoreCase(
            props.getProperty("sendEmail", "").trim()
        );
        
        showsForToday = "true".equalsIgnoreCase(
            props.getProperty("showsForToday", "").trim()
        );
        
        showsForDays = 3;
        {
            try {
                int i = Integer.parseInt(props.getProperty("showsForDays", "").trim());
                if (i > 0 && i <=14) {
                    showsForDays = i;
                }
            } catch (Exception ignore){}
        }
    }


}
