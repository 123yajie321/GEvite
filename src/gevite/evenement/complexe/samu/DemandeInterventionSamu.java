package gevite.evenement.complexe.samu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import gevite.evenement.EventI;
import gevite.evenement.complexe.ComplexEvent;
import gevite.evenement.complexe.ComplexEventI;

public class DemandeInterventionSamu extends ComplexEvent implements ComplexEventI {

	public DemandeInterventionSamu(ArrayList<EventI> relatedEvents) {
		super(relatedEvents);
	
	}
	
	

}
