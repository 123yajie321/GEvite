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


	private static final long serialVersionUID = 1L;

	public ConsciousFall(ArrayList<EventI> correlateEvents) {
		super(correlateEvents);
		
	}

	public ConsciousFall() {
		super();
		
	}

	
	
	

}
