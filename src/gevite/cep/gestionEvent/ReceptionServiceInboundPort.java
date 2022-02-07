package gevite.cep.gestionEvent;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gevite.cep.EventReceptionCI;
import gevite.evenement.EventI;

public class ReceptionServiceInboundPort extends AbstractInboundPort implements EventReceptionCI {

	public ReceptionServiceInboundPort(Class<? extends OfferedCI> implementedInterface, ComponentI owner)
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
