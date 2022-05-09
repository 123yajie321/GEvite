package gevite.cep;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import gevite.evenement.EventI;

public interface BusEventEmissionCI extends OfferedCI, RequiredCI {
	
	public void sendOtherBusEvent(String emitterURI, EventI event,String busId)throws Exception;
	

}
