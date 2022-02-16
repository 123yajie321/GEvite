package gevite.correlateur;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gevite.cep.EventEmissionCI;
import gevite.cep.EventReceptionCI;
import gevite.cepbus.CEPBus;
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
	public void sendEvent(String emitterURI, EventI event) throws Exception{
		/*
		  this.getOwner().handleRequest(
				c-> ((Correlateur)c).addEvent(emitterURI, event)
						);*/
		this.getOwner().runTask(c-> ((Correlateur)c).addEvent(emitterURI, event));
		  
	}

	@Override
	public void sendEvents(String emitterURI, EventI[] events) {
		// TODO Auto-generated method stub
		
	}

}
