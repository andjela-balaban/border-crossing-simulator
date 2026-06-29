package org.unibl.etf.simulacija;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.util.Util;

import static org.unibl.etf.util.SimulacioniParametri.*;

public class MyLogger {
	
	private MyLogger() { 
		super(); 
	}
	
	public static final Logger logger;
	
	static {
		logger = Logger.getGlobal();
		try {
			FileHandler fh = new FileHandler(Util.buildPath(LOGGER_PUTANJA));
			logger.addHandler(fh);
		} catch (SecurityException | IOException e) {
			logger.log(Level.WARNING, "Exception: ", e);
		}
	}
}
