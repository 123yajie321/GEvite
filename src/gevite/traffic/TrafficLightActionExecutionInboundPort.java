package gevite.traffic;

import java.io.Serializable;


import fr.sorbonne_u.components.ComponentI;

import fr.sorbonne_u.components.ports.AbstractInboundPort;

import gevite.actions.ActionI;
import gevite.cep.ActionExecutionCI;
import gevite.cep.ResponseI;
import gevite.fireStation.FireStation;
import gevite.samu.Samu;
import gevite.traffic.TrafficLight;

public class TrafficLightActionExecutionInboundPort extends AbstractInboundPort implements ActionExecutionCI {

	private static final long serialVersionUID = 1L;

	public TrafficLightActionExecutionInboundPort( ComponentI owner) throws Exception {
		super(ActionExecutionCI.class, owner);
		
	}
	
	public TrafficLightActionExecutionInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri,ActionExecutionCI.class, owner);
		
	}

	@Override
	public ResponseI executeAction(ActionI a, Serializable[] params) throws  Exception {
		
		
		return	this.getOwner().handleRequest(tf -> ((TrafficLight)tf).executeAction(a, params));
		
		
	
	
	}

}
