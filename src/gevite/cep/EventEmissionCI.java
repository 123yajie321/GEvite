package gevite.cep;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import gevite.evenement.EventI;

public interface EventEmissionCI extends OfferedCI, RequiredCI {
	
	public void sendEvent(String emitterURI, EventI event);
	public void sendEvents(String emitterURI, EventI[] events);

}
