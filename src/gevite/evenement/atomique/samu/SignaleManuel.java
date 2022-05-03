package gevite.evenement.atomique.samu;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;

import gevite.evenement.atomique.AtomicEvent;

public class SignaleManuel extends AtomicEvent {
	
	
	private static final long serialVersionUID = 1L;

	public SignaleManuel() {
		super();
	}

	public SignaleManuel(LocalTime occurrence) {
		super(occurrence);
	}
	

}
