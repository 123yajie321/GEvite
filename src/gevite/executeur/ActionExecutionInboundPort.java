package gevite.executeur;

import java.io.Serializable;


import fr.sorbonne_u.components.ComponentI;

import fr.sorbonne_u.components.ports.AbstractInboundPort;

import gevite.actions.ActionI;
import gevite.cep.ActionExecutionCI;
import gevite.cep.ResponseI;

public class ActionExecutionInboundPort extends AbstractInboundPort implements ActionExecutionCI {

	private static final long serialVersionUID = 1L;

	public ActionExecutionInboundPort( ComponentI owner) throws Exception {
		super(ActionExecutionCI.class, owner);
		
	}
	
	public ActionExecutionInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri,ActionExecutionCI.class, owner);
		
	}

	@Override
	public ResponseI execute(ActionI a, Serializable[] params) throws  Exception {
		
		return	this.getOwner().handleRequest(ae -> ((ActionExecutionCI)ae).execute(a, params));
		
	
	
	}

}