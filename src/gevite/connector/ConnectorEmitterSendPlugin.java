package gevite.connector;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import gevite.cep.CEPBusManagementCI;
import gevite.cep.EventEmissionCI;
import gevite.cep.EventReceptionCI;
import gevite.evenement.EventI;

public class ConnectorEmitterSendPlugin extends AbstractConnector implements EventEmissionCI {


	@Override
	public void sendEvent(String emitterURI, EventI event) throws Exception {
		((EventEmissionCI)this.offering).sendEvent(emitterURI, event);
		
		
	}

	@Override
	public void sendEvents(String emitterURI, EventI[] events) throws Exception {
		// TODO Auto-generated method stub
		
	}

	
}
