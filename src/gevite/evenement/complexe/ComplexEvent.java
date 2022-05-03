package gevite.evenement.complexe;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import fr.sorbonne_u.cps.smartcity.utils.TimeManager;
import gevite.evenement.EventI;

public abstract class ComplexEvent implements ComplexEventI {



	protected ArrayList<EventI> relatedEvents;
	protected LocalTime createtime;
	
	public ComplexEvent(ArrayList<EventI> relatedEvents) {
		this.relatedEvents = relatedEvents;
		createtime=TimeManager.get().getCurrentLocalTime();
	}
	
	public ComplexEvent() {
		this.relatedEvents = new ArrayList<EventI>();
		createtime=TimeManager.get().getCurrentLocalTime();
	}
	
	@Override
	public LocalTime getTimeStamp() {
		
		return createtime;
	}

	@Override
	public ArrayList<EventI> getCorrelatedEvents() {
		
		return relatedEvents;
	}
	
	public void addCorrelatedEvent(EventI event) {
		relatedEvents.add(event);
	}
	
	public void remouveCorrelatedEvent(EventI event) {
		relatedEvents.remove(event);
	}
	
	@Override
	public boolean hasProperty(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Serializable getPropertyValue(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	
	

}
