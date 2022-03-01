package gevite.connector;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import gevite.cep.EventReceptionCI;
import gevite.evenement.EventI;

public class ConnectorCepSendCorrelateur extends AbstractConnector implements EventReceptionCI {

	@Override
	public void receiveEvent(String emitterURI, EventI e) throws Exception {
		((EventReceptionCI)this.offering).receiveEvent(emitterURI, e);
	}

	@Override
	public void receiveEvents(String emitterURI, EventI[] events) throws Exception {
		// TODO Auto-generated method stub
		
	}

	

}
