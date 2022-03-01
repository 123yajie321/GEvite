package gevite.cepbus;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import gevite.cep.EventReceptionCI;
import gevite.evenement.EventI;

public class CepEventSendCorrelateurOutboundPort extends AbstractOutboundPort implements EventReceptionCI {

	private static final long serialVersionUID = 1L;

	public CepEventSendCorrelateurOutboundPort(ComponentI owner)
			throws Exception {
		super(EventReceptionCI.class, owner);
	}
	
	public CepEventSendCorrelateurOutboundPort(String uri,ComponentI owner)
			throws Exception {
		super(uri,EventReceptionCI.class, owner);
	}

	@Override
	public void receiveEvent(String emitterURI, EventI e)throws Exception {
		((EventReceptionCI)this.getConnector()).receiveEvent(emitterURI, e);
		   
					
	}

	@Override
	public void receiveEvents(String emitterURI, EventI[] events) throws Exception{

	}

}
