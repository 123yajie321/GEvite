package gevite.connector;

import java.io.Serializable;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import gevite.actions.ActionI;
import gevite.cep.ActionExecutionCI;
import gevite.cep.EventEmissionCI;
import gevite.cep.ResponseI;

public class ConnectorCorrelateurTrafficLight extends AbstractConnector implements ActionExecutionCI {

	@Override
	public ResponseI executeAction(ActionI a, Serializable[] params) throws Exception {
		return ((ActionExecutionCI)this.offering).executeAction(a, params);
		
	}
	
	

}
