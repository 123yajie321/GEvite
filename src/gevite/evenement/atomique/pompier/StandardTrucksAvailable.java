package gevite.evenement.atomique.pompier;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;

import gevite.evenement.atomique.AtomicEvent;

public class StandardTrucksAvailable extends AtomicEvent {

	private static final long serialVersionUID = 1L;
	
	public StandardTrucksAvailable() {
		super();
	}
	
	public StandardTrucksAvailable(LocalTime occurrence) {
		listPoperty = new HashMap<String, Serializable>();
		createtime = occurrence;	
	}
	

}
