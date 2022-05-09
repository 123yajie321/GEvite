package gevite.evenement;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import fr.sorbonne_u.cps.smartcity.utils.TimeManager;



public class EventBase implements EventBaseI {

	private ArrayList<EventI> listEvent;
	
	
	 public EventBase() {
		listEvent=new ArrayList<EventI>();
	}

	

	@Override
	public void addEvent(EventI e) {
		
		boolean insert=false;
		for(int i=0;i<listEvent.size();i++) {
			if(e.getTimeStamp().isBefore(listEvent.get(i).getTimeStamp())) {
				listEvent.add(i, e);
				insert=true;
				break;
			}
		}
		if(!insert) {
			listEvent.add(e);
			
		}
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
		
		if(d==null) {
			
			listEvent.clear();
		}else {
			
			LocalTime now=TimeManager.get().getCurrentLocalTime();
			listEvent.removeIf(e -> 
						d.getNano()< Duration.between(now, e.getTimeStamp()).getNano());
		}
		
	
	}

	
	

}
