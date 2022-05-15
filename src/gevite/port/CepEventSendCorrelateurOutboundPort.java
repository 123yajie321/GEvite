package gevite.port;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import gevite.evenement.EventI;
import gevite.interfaces.EventReceptionCI;

/**
 * The class <code>CepEventSendCorrelateurOutboundPort</code> implements an outbound port for the
 * <code>EventReceptionCI</code> component interface which allows component Cepbus to Send Event to correlator
 * @author	Yajie LIU, Zimeng ZHANG
 */

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
