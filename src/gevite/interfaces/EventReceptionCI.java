package gevite.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import gevite.evenement.EventI;

public interface EventReceptionCI extends OfferedCI, RequiredCI {
	
	public void receiveEvent(String emitterURI, EventI e)throws Exception;
	
	public void receiveEvents(String emitterURI, EventI[] events)throws Exception;
	

}
