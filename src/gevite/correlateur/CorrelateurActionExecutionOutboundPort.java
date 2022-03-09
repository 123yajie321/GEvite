package gevite.correlateur;

import java.io.Serializable;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.cps.smartcity.interfaces.SAMUActionCI;
import gevite.actions.ActionI;
import gevite.cep.ActionExecutionCI;
import gevite.cep.EventEmissionCI;
import gevite.cep.ResponseI;

public class CorrelateurActionExecutionOutboundPort extends AbstractOutboundPort
implements ActionExecutionCI {

	private static final long serialVersionUID = 1L;

	public CorrelateurActionExecutionOutboundPort( ComponentI owner)
			throws Exception {
		super(ActionExecutionCI.class, owner);
		
	}
	public CorrelateurActionExecutionOutboundPort( String uri,ComponentI owner)
			throws Exception {
		super(uri,ActionExecutionCI.class, owner);
		
	}
	

	@Override
	public ResponseI execute(ActionI a, Serializable[] params) throws Exception {
		
		return ((ActionExecutionCI)this.getConnector()).execute(a, params);
	}

}
