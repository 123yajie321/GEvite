package gevite.evenement;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class EventBase implements EventBaseI {

	private ArrayList<EventI> listEvent;
	
	
	 public EventBase() {
		listEvent=new ArrayList<EventI>();
	}

	

	@Override
	public void addEvent(EventI e) {
		listEvent.add(e);
	}

	@Override
	public void removeEvent(EventI e) {
		listEvent.remove(e);

	}

	@Override
	public EventI getEvent(int i) {
		return listEvent.get(i);
	}

	@Override
	public int numberOfEvents() {
		return listEvent.size();
	}

	@Override
	public boolean appearsIn(EventI e) {
		return listEvent.contains(e);
	}

	
	@Override
	public void clearEvents(Duration d) {
		/*
		for(int i = 0 ; i < listEvent.size() ; i++) {
			LocalTime time=LocalTime.now();
			if( Duration.between(time, listEvent.get(i).getTimeStamp())>d) {
				listEvent.remove(i);
				i--;
				
			}
		}
*/
	}
	

}
