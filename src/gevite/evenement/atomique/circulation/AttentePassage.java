package gevite.evenement.atomique.circulation;

import java.io.Serializable;
import java.time.LocalTime;

import gevite.evenement.atomique.AtomicEvent;

public class AttentePassage extends AtomicEvent {

	private static final long serialVersionUID = 1L;
	public AttentePassage(){
		super();
	}

	public AttentePassage(LocalTime occurrence) {
		super(occurrence);	
	}

}
