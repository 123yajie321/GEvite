package gevite.emitteur;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import gevite.cep.EventEmissionCI;
import gevite.evenement.EventI;

public class EmitterSendOutboundPort extends AbstractOutboundPort implements EventEmissionCI {

	private static final long serialVersionUID=1L;
	
	public EmitterSendOutboundPort( ComponentI owner) throws Exception {
		super(EventEmissionCI.class, owner);
		
	}
	
	public EmitterSendOutboundPort(String uri,ComponentI owner) throws Exception{
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
