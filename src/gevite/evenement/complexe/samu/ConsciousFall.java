package gevite.evenement.complexe.samu;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import gevite.evenement.EventI;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.complexe.ComplexEvent;
import gevite.evenement.complexe.ComplexEventI;

public class ConsciousFall  extends ComplexEvent implements ComplexEventI {

	private HashMap<String, Serializable> listPoperty;
	private ArrayList<EventI> correlateEvents;
	
	public ConsciousFall(ArrayList<EventI> correlateEvents) {
		this.correlateEvents = correlateEvents;
		
	}



	@Override
	public ArrayList<EventI> getCorrelatedEvents() {
		return this.correlateEvents;
	}
	
	
	
	

}
