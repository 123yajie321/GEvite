package gevite.cep.gestionEvent.correlateur;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gevite.cep.EventReceptionCI;
import gevite.evenement.EventI;

public class CorrelateServiceInboundPort extends AbstractInboundPort implements EventReceptionCI {

	public CorrelateServiceInboundPort(Class<? extends OfferedCI> implementedInterface, ComponentI owner)
			throws Exception {
		super(implementedInterface, owner);
		// TODO Auto-generated constructor stub
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
