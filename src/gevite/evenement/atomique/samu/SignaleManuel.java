package gevite.evenement.atomique.samu;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;

import gevite.evenement.atomique.AtomicEvent;

public class SignaleManuel extends AtomicEvent {
	
	public SignaleManuel() {
		super();
	}

	public SignaleManuel(LocalTime occurrence) {
		listPoperty = new HashMap<String, Serializable>();
		createtime = occurrence;
	}
	

}
