package gevite.connector;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import gevite.evenement.EventI;
import gevite.interfaces.EventReceptionCI;

/**
 * The class <code>ConnectorEventReception</code> implements a connector for the
 * <code>EventReceptionCI</code> component interface.
 * @author	Yajie LIU, Zimeng ZHANG
 */

public class ConnectorEventReception extends AbstractConnector implements EventReceptionCI {

	@Override
	public void receiveEvent(String emitterURI, EventI e) throws Exception {
		((EventReceptionCI)this.offering).receiveEvent(emitterURI, e);
	}

	@Override
	public void receiveEvents(String emitterURI, EventI[] events) throws Exception {
		// TODO Auto-generated method stub
		
	}

	

}
