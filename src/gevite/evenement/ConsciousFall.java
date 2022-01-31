package gevite.evenement;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class ConsciousFall  extends ComplexEvent implements ComplexEventI {

	private HashMap<String, Serializable> listPoperty;



	@Override
	public ArrayList<EventI> getCorrelatedEvents() {
		// TODO Auto-generated method stub
		return null;
	}

}
