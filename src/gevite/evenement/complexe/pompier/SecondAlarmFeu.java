package gevite.evenement.complexe.pompier;

import java.io.Serializable;
import java.util.ArrayList;
import gevite.evenement.EventI;
import gevite.evenement.complexe.ComplexEvent;

public class SecondAlarmFeu extends ComplexEvent {
	
	private static final long serialVersionUID = 1L;

	public SecondAlarmFeu(ArrayList<EventI> correlateEvents) {
		super(correlateEvents);
	}

	public SecondAlarmFeu() {
		super();
	}
}
