package gevite.correlateur;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gevite.cep.EventEmissionCI;
import gevite.cep.EventReceptionCI;
import gevite.evenement.EventI;

public class CepEventRecieveCorrelateurInboundPort extends AbstractInboundPort implements EventReceptionCI {

	private static final long serialVersionUID = 1L;

	public CepEventRecieveCorrelateurInboundPort(ComponentI owner)
			throws Exception {
		super(EventEmissionCI.class,owner);
	}
	
	public CepEventRecieveCorrelateurInboundPort(String uri,ComponentI owner)
			throws Exception {
		super(uri,EventEmissionCI.class, owner);
	}

	@Override
	public void receiveEvent(String emitterURI, EventI e) throws Exception {
		this.getOwner().runTask(c-> ((CorrelateurSamu)c).addEvent(emitterURI, e));		
	}

	@Override
	public void receiveEvents(String emitterURI, EventI[] events) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
