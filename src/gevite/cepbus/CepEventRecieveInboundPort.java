package gevite.cepbus;

import java.util.concurrent.RejectedExecutionException;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI;
import gevite.cep.EventEmissionCI;
import gevite.correlateur.CorrelateurSamu;
import gevite.evenement.EventI;

public class CepEventRecieveInboundPort extends AbstractInboundPort implements EventEmissionCI {

	private static final long serialVersionUID=1L;

	public CepEventRecieveInboundPort(ComponentI owner)
			throws Exception {
		super(EventEmissionCI.class, owner);
		// TODO Auto-generated constructor stub
	}
	
	public CepEventRecieveInboundPort(String uri,ComponentI owner)
			throws Exception {
		super(uri,EventEmissionCI.class, owner);
		// TODO Auto-generated constructor stub
	}

	
	
	
	@Override
	public void sendEvent(String emitterURI, EventI event) throws Exception {
		
		this.getOwner().handleRequest(
				cep -> {	((CEPBus)cep).
									sendEvent(emitterURI, event);
						return null;
					 });
	}

	@Override
	public void sendEvents(String emitterURI, EventI[] events) {
		

	}

}
