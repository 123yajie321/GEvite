package gevite.port;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import gevite.evenement.EventI;
import gevite.interfaces.BusEventEmissionCI;
import gevite.interfaces.EventEmissionCI;
/**
 * The class <code>BusSendEventOutboundPort</code> implements an outbound port for the
 * <code>BusEventEmissionCI</code> component interface.
 * @author	Yajie LIU, Zimeng ZHANG
 */

public class BusSendEventOutboundPort extends AbstractOutboundPort implements BusEventEmissionCI {

	private static final long serialVersionUID=1L;
	
	public BusSendEventOutboundPort( ComponentI owner) throws Exception {
		super(BusEventEmissionCI.class, owner);
		
	}
	
	public BusSendEventOutboundPort(String uri,ComponentI owner) throws Exception{
		super(uri, BusEventEmissionCI.class, owner);
	
	}


	@Override
	public void sendOtherBusEvent(String emitterURI, EventI event, String busId) throws Exception {
		((BusEventEmissionCI)this.getConnector()).sendOtherBusEvent(emitterURI, event, busId);
		
	}

	
}
