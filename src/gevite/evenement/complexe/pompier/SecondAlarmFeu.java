package gevite.evenement.complexe.pompier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import gevite.evenement.EventI;
import gevite.evenement.complexe.ComplexEvent;
import gevite.evenement.complexe.ComplexEventI;

public class SecondAlarmFeu extends ComplexEvent implements ComplexEventI {
	
	private HashMap<String, Serializable> listPoperty;
	private ArrayList<EventI> correlateEvents;
	
	public SecondAlarmFeu(ArrayList<EventI> correlateEvents) {
		super(correlateEvents);
	}

	@Override
	public ArrayList<EventI> getCorrelatedEvents() {
		return this.correlateEvents;
	}
	

}
