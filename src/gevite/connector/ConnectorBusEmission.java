package gevite.connector;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import gevite.cep.BusEventEmissionCI;
import gevite.evenement.EventI;

public class ConnectorBusEmission extends AbstractConnector implements BusEventEmissionCI {



	@Override
	public void sendOtherBusEvent(String emitterURI, EventI event, String busId) throws Exception {
		((BusEventEmissionCI)this.offering).sendOtherBusEvent(emitterURI, event, busId);	
	}

	
}
