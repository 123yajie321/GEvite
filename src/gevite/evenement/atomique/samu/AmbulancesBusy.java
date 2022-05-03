package gevite.evenement.atomique.samu;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;

import gevite.evenement.atomique.AtomicEvent;

public class AmbulancesBusy extends AtomicEvent {


	private static final long serialVersionUID = 1L;
	
	public AmbulancesBusy() {
		super();
	}
	
	public AmbulancesBusy(LocalTime occurrence) {
		super(occurrence);
	}
}
