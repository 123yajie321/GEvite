package gevite.correlateur;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import gevite.cep.CEPBusManagementCI;
import gevite.cep.EventReceptionCI;
import gevite.evenement.EventI;

public class CorrelateurSendCepOutboundPort extends AbstractOutboundPort implements  EventReceptionCI {

	public CorrelateurSendCepOutboundPort( ComponentI owner)
			throws Exception {
		super(EventReceptionCI.class, owner);
		
	}

	
	public CorrelateurSendCepOutboundPort(String uri,ComponentI owner) throws Exception{
		super(uri, EventReceptionCI.class, owner);
	
	}

	
	@Override
	public void receiveEvent(String emitterURI, EventI e) throws Exception {
		((EventReceptionCI)this.getConnector()).receiveEvent(emitterURI, e);
		
	}

	@Override
	public void receiveEvents(String emitterURI, EventI[] events) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
	


}
