package gevite.samu;

import java.io.Serializable;


import fr.sorbonne_u.components.ComponentI;

import fr.sorbonne_u.components.ports.AbstractInboundPort;

import gevite.actions.ActionI;
import gevite.cep.ActionExecutionCI;
import gevite.cep.ResponseI;
import gevite.fireStation.FireStation;
import gevite.samu.Samu;
import gevite.traffic.TrafficLight;

public class SAMUActionExecutionInboundPort extends AbstractInboundPort implements ActionExecutionCI {

	private static final long serialVersionUID = 1L;

	public SAMUActionExecutionInboundPort( ComponentI owner) throws Exception {
		super(ActionExecutionCI.class, owner);
		
	}
	
	public SAMUActionExecutionInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri,ActionExecutionCI.class, owner);
		
	}

	@Override
	public ResponseI execute(ActionI a, Serializable[] params) throws  Exception {
		
		return	this.getOwner().handleRequest(samu -> ((Samu)samu).execute(a, params));
		
		
	
	
	}

}
