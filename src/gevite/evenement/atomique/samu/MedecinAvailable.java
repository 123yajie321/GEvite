package gevite.evenement.atomique.samu;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;

import gevite.evenement.atomique.AtomicEvent;

public class MedecinAvailable extends AtomicEvent {

	private static final long serialVersionUID = 1L;
	
	public MedecinAvailable() {
		super();
	}
	
	public MedecinAvailable(LocalTime occurrence) {
		super(occurrence);
	}	

}
