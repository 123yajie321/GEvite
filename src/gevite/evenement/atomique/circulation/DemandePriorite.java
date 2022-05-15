package gevite.evenement.atomique.circulation;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;

import gevite.evenement.atomique.AtomicEvent;

public class DemandePriorite extends AtomicEvent{
	

	private static final long serialVersionUID = 1L;

	public DemandePriorite(){
		super();
	}

	public DemandePriorite(LocalTime occurrence) {
		super(occurrence);	
	}
	
	

}
