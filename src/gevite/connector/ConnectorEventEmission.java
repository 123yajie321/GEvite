package gevite.connector;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import gevite.evenement.EventI;
import gevite.interfaces.CEPBusManagementCI;
import gevite.interfaces.EventEmissionCI;
import gevite.interfaces.EventReceptionCI;

/**
 * The class <code>ConnectorEventEmission</code> implements a connector for the
 * <code>EventEmissionCI</code> component interface.
 * @author	Yajie LIU, Zimeng ZHANG
 */

public class ConnectorEventEmission extends AbstractConnector implements EventEmissionCI {


	@Override
	public void sendEvent(String emitterURI, EventI event) throws Exception {
		((EventEmissionCI)this.offering).sendEvent(emitterURI, event);
		
		
	}

	@Override
	public void sendEvents(String emitterURI, EventI[] events) throws Exception {
		// TODO Auto-generated method stub
		
	}

	
}
