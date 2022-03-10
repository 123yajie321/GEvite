package gevite.evenement.atomique.pompier;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;

import gevite.evenement.atomique.AtomicEvent;

public class HighLadderTrucksBusy extends AtomicEvent {

	private static final long serialVersionUID = 1L;
	
	public HighLadderTrucksBusy() {
		super();
	}
	
	public HighLadderTrucksBusy(LocalTime occurrence) {
		listPoperty = new HashMap<String, Serializable>();
		createtime = occurrence;	
	}
	

}
