package gevite.evenement.complexe.pompier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import gevite.evenement.EventI;
import gevite.evenement.complexe.ComplexEvent;
import gevite.evenement.complexe.ComplexEventI;

public class PremiereAlarmFeu extends ComplexEvent implements ComplexEventI {
	
	private static final long serialVersionUID = 1L;

	public PremiereAlarmFeu(ArrayList<EventI> correlateEvents) {
		super(correlateEvents);
	}

	public PremiereAlarmFeu() {
		super();
	}

	

}
