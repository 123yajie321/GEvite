package gevite.emitteur;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import gevite.cep.CEPBusManagementCI;
import gevite.cep.EventReceptionCI;
import gevite.evenement.EventI;

public class EmitterSendOutboundPort extends AbstractOutboundPort implements EventReceptionCI {

	private static final long serialVersionUID=1L;
	
	public EmitterSendOutboundPort( ComponentI owner) throws Exception {
		super(CEPBusManagementCI.class, owner);
		
	}
	
	public EmitterSendOutboundPort(String uri,ComponentI owner) throws Exception{
		super(uri, CEPBusManagementCI.class, owner);
	
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
