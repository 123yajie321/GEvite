package gevite.evenement.complexe.circulation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import gevite.evenement.EventI;
import gevite.evenement.complexe.ComplexEvent;
import gevite.evenement.complexe.ComplexEventI;

public class AttentePassageComplexe extends ComplexEvent  {
	
	private static final long serialVersionUID = 1L;

	public AttentePassageComplexe(ArrayList<EventI> correlateEvents) {
		super(correlateEvents);
	}

	public AttentePassageComplexe() {
		super();
	}

	

}
