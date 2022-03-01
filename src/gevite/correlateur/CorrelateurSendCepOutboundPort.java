package gevite.correlateur;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import gevite.cep.CEPBusManagementCI;
import gevite.cep.EventEmissionCI;
import gevite.cep.EventReceptionCI;
import gevite.evenement.EventI;

public class CorrelateurSendCepOutboundPort extends AbstractOutboundPort implements  EventEmissionCI {

	public CorrelateurSendCepOutboundPort( ComponentI owner)
			throws Exception {
		super(EventReceptionCI.class, owner);
		
	}

	
	public CorrelateurSendCepOutboundPort(String uri,ComponentI owner) throws Exception{
		super(uri, EventReceptionCI.class, owner);
	
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
