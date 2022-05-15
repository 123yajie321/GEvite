package gevite.port;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import gevite.evenement.EventI;
import gevite.interfaces.EventEmissionCI;
/**
 * The class <code>SendEventOutboundPort</code> implements an outbound port for the
 * <code>EventEmissionCI</code> component interface.
 * @author	Yajie LIU, Zimeng ZHANG
 */

public class SendEventOutboundPort extends AbstractOutboundPort implements EventEmissionCI {

	private static final long serialVersionUID=1L;
	
	public SendEventOutboundPort( ComponentI owner) throws Exception {
		super(EventEmissionCI.class, owner);
		
	}
	
	public SendEventOutboundPort(String uri,ComponentI owner) throws Exception{
		super(uri, EventEmissionCI.class, owner);
	
	}

	@Override
	public void sendEvent(String emitterURI, EventI event) throws Exception {
		((EventEmissionCI)this.getConnector()).sendEvent(emitterURI, event);
		
	}

	@Override
	public void sendEvents(String emitterURI, EventI[] events) throws Exception {
		// TODO Auto-generated method stub
		
	}

	
}
