package gevite.evenement.complexe.samu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import gevite.evenement.EventI;
import gevite.evenement.complexe.ComplexEvent;
import gevite.evenement.complexe.ComplexEventI;

public class DemandeInterventionSamu extends ComplexEvent implements ComplexEventI {
	
	private HashMap<String, Serializable> listPoperty;
	private ArrayList<EventI> correlateEvents;
	
	public DemandeInterventionSamu(ArrayList<EventI> correlateEvents) {
		this.correlateEvents = correlateEvents;
	}

	@Override
	public ArrayList<EventI> getCorrelatedEvents() {
		return this.correlateEvents;
	}
	

}
