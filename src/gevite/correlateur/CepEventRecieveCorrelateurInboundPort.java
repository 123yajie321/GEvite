package gevite.correlateur;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gevite.cep.EventEmissionCI;
import gevite.cep.EventReceptionCI;
import gevite.evenement.EventI;

public class CepEventRecieveCorrelateurInboundPort extends AbstractInboundPort implements EventEmissionCI {

	public CepEventRecieveCorrelateurInboundPort(ComponentI owner)
			throws Exception {
		super(EventEmissionCI.class,owner);
	}
	
	public CepEventRecieveCorrelateurInboundPort(String uri,ComponentI owner)
			throws Exception {
		super(uri,EventEmissionCI.class, owner);
	}


	@Override
	public void sendEvent(String emitterURI, EventI event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendEvents(String emitterURI, EventI[] events) {
		// TODO Auto-generated method stub
		
	}

}
