package gevite.samu;

import java.io.Serializable;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;

import fr.sorbonne_u.components.ports.AbstractInboundPort;

import gevite.actions.ActionI;
import gevite.cep.ActionExecutionCI;
import gevite.cep.ResponseI;
import gevite.plugin.PluginActionExecuteIn;

public class PluginSAMUActionExecutionInboundPort extends AbstractInboundPort implements ActionExecutionCI {

	private static final long serialVersionUID = 1L;

	public PluginSAMUActionExecutionInboundPort(String pluginURI, ComponentI owner) throws Exception {
		super(ActionExecutionCI.class, owner,pluginURI,null);
		
	}
	
	public PluginSAMUActionExecutionInboundPort(String uri,String pluginURI, ComponentI owner) throws Exception {
		super(uri,ActionExecutionCI.class, owner,pluginURI,null);
		
	}

	@Override
	public ResponseI execute(ActionI a, Serializable[] params) throws  Exception {
		
		System.out.println("begin Execute port");
		return this.getOwner().handleRequest(
				new AbstractComponent.AbstractService<ResponseI>(this.getPluginURI()) {
					@Override
					public ResponseI call() throws Exception{
						return ((PluginActionExecuteIn)this.getServiceProviderReference()).
						execute(a, params);
					}
				});
		//System.out.println("fin Execute port");

		//return	this.getOwner().handleRequest(samu -> ((Samu)samu).execute(a, params));

	}

}
