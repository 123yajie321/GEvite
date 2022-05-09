package gevite.cepbus;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import gevite.cep.BusEventEmissionCI;
import gevite.cep.EventReceptionCI;
import gevite.evenement.EventI;

public class BusSendOutbounndPort extends AbstractOutboundPort implements BusEventEmissionCI {

	private static final long serialVersionUID = 1L;

	public BusSendOutbounndPort(ComponentI owner)
			throws Exception {
		super(BusEventEmissionCI.class, owner);
	}
	
	public BusSendOutbounndPort(String uri,ComponentI owner)
			throws Exception {
		super(uri,BusEventEmissionCI.class, owner);
	}

	

	@Override
	public void sendOtherBusEvent(String emitterURI, EventI event, String busId) throws Exception {
		((BusEventEmissionCI)this.getConnector()).sendOtherBusEvent(emitterURI, event, busId);
		
		
	}

}
