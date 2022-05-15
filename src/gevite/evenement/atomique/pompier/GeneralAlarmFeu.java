package gevite.evenement.atomique.pompier;

import java.util.ArrayList;

import gevite.evenement.EventI;
import gevite.evenement.complexe.ComplexEvent;

public class GeneralAlarmFeu extends ComplexEvent {
	
	private static final long serialVersionUID = 1L;

	public GeneralAlarmFeu(ArrayList<EventI> correlateEvents) {
		super(correlateEvents);
	}

	public GeneralAlarmFeu() {
		super();
	}
}
