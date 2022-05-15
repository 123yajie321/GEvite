package gevite.connector;

import java.io.Serializable;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import gevite.actions.ActionI;
import gevite.interfaces.ActionExecutionCI;
import gevite.interfaces.EventEmissionCI;
import gevite.interfaces.ResponseI;
/**
 * The class <code>ConnectorCorrelateurExecuteur</code> implements a connector for the
 * <code>ActionExecutionCI</code> component interface.
 * @author	Yajie LIU, Zimeng ZHANG
 */

public class ConnectorCorrelateurExecuteur extends AbstractConnector implements ActionExecutionCI {

	@Override
	public ResponseI executeAction(ActionI a, Serializable[] params) throws Exception {
		return ((ActionExecutionCI)this.offering).executeAction(a, params);
		
	}
	
	

}
