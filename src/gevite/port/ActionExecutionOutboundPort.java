package gevite.port;

import java.io.Serializable;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.cps.smartcity.interfaces.SAMUActionCI;
import gevite.actions.ActionI;
import gevite.interfaces.ActionExecutionCI;
import gevite.interfaces.EventEmissionCI;
import gevite.interfaces.ResponseI;

/**
 * The class <code>CorrelateurActionExecutionOutboundPort</code> implements an outbound port for the
 * <code>ActionExecutionCI</code> component interface
 * @author	Yajie LIU, Zimeng ZHANG
 */

public class ActionExecutionOutboundPort extends AbstractOutboundPort
implements ActionExecutionCI {

	private static final long serialVersionUID = 1L;

	public ActionExecutionOutboundPort( ComponentI owner)
			throws Exception {
		super(ActionExecutionCI.class, owner);
		
	}
	public ActionExecutionOutboundPort( String uri,ComponentI owner)
			throws Exception {
		super(uri,ActionExecutionCI.class, owner);
		
	}
	

	@Override
	public ResponseI executeAction(ActionI a, Serializable[] params) throws Exception {
		
		return ((ActionExecutionCI)this.getConnector()).executeAction(a, params);
	}

}
