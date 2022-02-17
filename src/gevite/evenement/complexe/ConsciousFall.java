package gevite.evenement.complexe;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import gevite.evenement.EventI;
import gevite.evenement.atomique.AlarmeSante;

public class ConsciousFall  extends ComplexEvent implements ComplexEventI {

	private HashMap<String, Serializable> listPoperty;
	private ArrayList<EventI> correlateEvents;
	
	public ConsciousFall() {
		AlarmeSante aSante = new AlarmeSante();
		
	}



	@Override
	public ArrayList<EventI> getCorrelatedEvents() {
		return this.correlateEvents;
	}
	
	
	
	

}
