package gevite.evenement;

import java.util.ArrayList;

public interface ComplexEventI extends EventI {
	
	public ArrayList<EventI> getCorrelatedEvents();

}
