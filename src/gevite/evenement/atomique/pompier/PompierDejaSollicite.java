package gevite.evenement.atomique.pompier;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;

import gevite.evenement.atomique.AtomicEvent;

public class PompierDejaSollicite extends AtomicEvent{
	private static final long serialVersionUID = 1L;

	public PompierDejaSollicite() {
		super();
	}

	public PompierDejaSollicite(LocalTime occurrence) {
		super(occurrence);
	}

}
