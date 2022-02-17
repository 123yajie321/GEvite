package gevite.evenement.complexe;

import java.util.ArrayList;

import gevite.evenement.EventI;

public interface ComplexEventI extends EventI {
	
	public ArrayList<EventI> getCorrelatedEvents();

}
