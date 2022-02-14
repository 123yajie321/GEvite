package gevite.cepbus;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gevite.cep.EventReceptionCI;
import gevite.evenement.EventI;

public class CepEventRecieveCorrelateurInboundPort extends AbstractInboundPort implements EventReceptionCI {

	public CepEventRecieveCorrelateurInboundPort(ComponentI owner)
			throws Exception {
		super(EventReceptionCI.class, owner);
	}
	
	public CepEventRecieveCorrelateurInboundPort(String uri,ComponentI owner)
			throws Exception {
		super(uri,EventReceptionCI.class, owner);
	}

	@Override
	public void receiveEvent(String emitterURI, EventI e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveEvents(String emitterURI, EventI[] events) {
		// TODO Auto-generated method stub

	}

}
