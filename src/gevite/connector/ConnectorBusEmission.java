package gevite.connector;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import gevite.evenement.EventI;
import gevite.interfaces.BusEventEmissionCI;

/**
 * The class <code>ConnectorBusEmission</code> implements a connector for the
 * <code>BusEventEmissionCI</code> component interface.
 * @author	Yajie LIU, Zimeng ZHANG
 */

public class ConnectorBusEmission extends AbstractConnector implements BusEventEmissionCI {

	@Override
	public void sendOtherBusEvent(String emitterURI, EventI event, String busId) throws Exception {
		((BusEventEmissionCI)this.offering).sendOtherBusEvent(emitterURI, event, busId);	
	}

	
}
