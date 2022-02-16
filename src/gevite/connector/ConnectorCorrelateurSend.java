package gevite.connector;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import gevite.cep.CEPBusManagementCI;
import gevite.cep.EventEmissionCI;
import gevite.cep.EventReceptionCI;
import gevite.evenement.EventI;

public class ConnectorCorrelateurSend extends AbstractConnector implements EventReceptionCI {

	@Override
	public void receiveEvent(String emitterURI, EventI e) throws Exception {
		((EventEmissionCI)this.offering).sendEvent(emitterURI, e);
	}

	@Override
	public void receiveEvents(String emitterURI, EventI[] events) throws Exception {
		// TODO Auto-generated method stub
		
	}

	

}
