package gevite.evenement.complexe;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import fr.sorbonne_u.cps.smartcity.utils.TimeManager;
import gevite.evenement.EventI;

public abstract class ComplexEvent implements ComplexEventI {


	private HashMap<String, Serializable> listPoperty;
	private ArrayList<EventI> relatedEvents;
	protected LocalTime createtime;
	
	public ComplexEvent(ArrayList<EventI> relatedEvents) {
		this.relatedEvents = relatedEvents;
		createtime=TimeManager.get().getCurrentLocalTime();
	}
	
	@Override
	public LocalTime getTimeStamp() {
		
		return createtime;
	}

	@Override
	public boolean hasProperty(String name) {
		
		return listPoperty.containsKey(name);
	}

	@Override
	public Serializable getPropertyValue(String name) {
		
		return listPoperty.get(name);
	}

	@Override
	public ArrayList<EventI> getCorrelatedEvents() {
		
		return relatedEvents;
	}

}
