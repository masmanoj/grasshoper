package in.grasshoper;

import in.grasshoper.core.boot.AbstractApplicationConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class GHServerApplication {
	public static class Configuration extends AbstractApplicationConfiguration { }
	
    public static void main(String[] args) throws Exception{
    	ConfigurableApplicationContext ctx = SpringApplication.run(Configuration.class, args);
    	waitForKeyPressToCleanlyExit(ctx);
    }
    
    
    //safe exit.
    public static void waitForKeyPressToCleanlyExit(ConfigurableApplicationContext ctx) throws IOException {
        System.out.println("\nHit Enter to quit...");
        BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
        d.readLine();

        ctx.stop();
        ctx.close();
    }
}
