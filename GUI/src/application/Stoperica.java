package application;



import static org.unibl.etf.simulacija.MyLogger.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Stoperica extends Thread {
	
	private SimpleDateFormat sdf = new SimpleDateFormat("mm:ss:S");
	private String[] split;
	private String min, sec, millis, sspTime;
	private long time;
	private boolean stopped = false;
	private boolean paused = false;
	Label labelToUpdate;
	
	public Stoperica(Label labelToUpdate) {
		min = new String("00");
	    sec = new String("00");
	    millis = new String("00");
	    sspTime = new String("00:00:00");
		this.labelToUpdate = labelToUpdate;
		
		setDaemon(true);
	}
	
	public void pauseStopwatch() {
		this.paused = true;
	}
	
	public synchronized void resumeStopwatch() {
		this.paused = false;
		notify();
	}
	
	public void setTime(long time) {
	    this.time = time;
	    split = sdf.format(new Date(time)).split(":");
	    min = split[0];
	    sec = split[1];
	
	    if (split[2].length() == 1) {
	        split[2] = "0" + split[2];
	    }
	    millis = split[2].substring(0, 2);
	
	    sspTime = min + ":" + sec + ":" + millis;
	}
	
	public void stopStopwatch() {
		this.stopped = true;
	}
	

	@Override
	public void run() {
		while(!stopped) {
			synchronized(this) {
				while(paused) {
					try { 
						wait(); 
					} catch(InterruptedException e) { 
						logger.log(Level.WARNING, "Exception: ", e);
					}
				}
			}
			
			setTime(time);
			try { 
				Thread.sleep(10); 
			} catch(InterruptedException e) { 
				logger.log(Level.WARNING, "Exception: ", e);
			}
			time = time + 10;
			
			Platform.runLater(() -> {
				labelToUpdate.setText(sspTime.toString());
			});
		}
	}

}
