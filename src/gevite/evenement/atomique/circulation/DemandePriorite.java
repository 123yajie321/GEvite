package gevite.evenement.atomique.circulation;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;

import gevite.evenement.atomique.AtomicEvent;

public class DemandePriorite extends AtomicEvent{
	
	public DemandePriorite(){
		super();
	}

	public DemandePriorite(LocalTime occurrence) {
		listPoperty = new HashMap<String, Serializable>();
		createtime = occurrence;	
	}
	
	

}
