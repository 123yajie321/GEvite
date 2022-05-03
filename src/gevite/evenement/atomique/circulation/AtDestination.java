package gevite.evenement.atomique.circulation;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;

import gevite.evenement.atomique.AtomicEvent;

public class AtDestination extends AtomicEvent{
	
	private static final long serialVersionUID = 1L;

	public AtDestination() {
		super();
	}

	public AtDestination(LocalTime occurrence) {
		super(occurrence);
	}
}
