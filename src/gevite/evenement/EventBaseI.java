package gevite.evenement;

import java.time.Duration;


public interface EventBaseI {
	
	
	 public void addEvent(EventI e);
	 public void removeEvent(EventI e);
	 public EventI getEvent(int i);
	 public int numberofEvents();
	 public boolean appearsIn(EventI e);
	 public void clearEvents(Duration d);
	 
	 

}
