package gevite.evenement.atomique.pompier;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;

import gevite.evenement.atomique.AtomicEvent;

public class TouslesFireStation extends AtomicEvent{
	private static final long serialVersionUID = 1L;

	public TouslesFireStation() {
		super();
	}

	public TouslesFireStation(LocalTime occurrence) {
		super(occurrence);
	}

}