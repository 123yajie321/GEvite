package gevite.cepbus;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gevite.cep.EventEmissionCI;
import gevite.evenement.EventI;

public class CepEventRecieveEmitterInboundPort extends AbstractInboundPort implements EventEmissionCI {

	private static final long serialVersionUID=1L;

	public CepEventRecieveEmitterInboundPort(ComponentI owner)
			throws Exception {
		super(EventEmissionCI.class, owner);
		// TODO Auto-generated constructor stub
	}
	
	public CepEventRecieveEmitterInboundPort(String uri,ComponentI owner)
			throws Exception {
		super(uri,EventEmissionCI.class, owner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void sendEvent(String emitterURI, EventI event) {
		

	}

	@Override
	public void sendEvents(String emitterURI, EventI[] events) {
		// TODO Auto-generated method stub

	}

}
