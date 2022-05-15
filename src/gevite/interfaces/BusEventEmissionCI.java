package gevite.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import gevite.evenement.EventI;

/**
 * The component interface <code>BusEventEmissionCI</code> declares the signatures of
 * services to be offered by a CepBus component.It is used for the transmission of events between two Buses in Multi-JVM
 * @author Yajie LIU, Zimeng ZHANG
 */

public interface BusEventEmissionCI extends OfferedCI, RequiredCI {
	
	public void sendOtherBusEvent(String emitterURI, EventI event,String busId)throws Exception;
	

}
