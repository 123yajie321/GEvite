package gevite.plugin;

import java.io.Serializable;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;

import fr.sorbonne_u.components.ports.AbstractInboundPort;

import gevite.actions.ActionI;
import gevite.cep.ActionExecutionCI;
import gevite.cep.ResponseI;

public class PluginActionExecutionInboundPort extends AbstractInboundPort implements ActionExecutionCI {

	private static final long serialVersionUID = 1L;

	public PluginActionExecutionInboundPort(String pluginURI, ComponentI owner) throws Exception {
		super(ActionExecutionCI.class, owner,pluginURI,null);
		
	}
	
	public PluginActionExecutionInboundPort(String uri,String pluginURI, ComponentI owner) throws Exception {
		super(uri,ActionExecutionCI.class, owner,pluginURI,null);
		
	}

	@Override
	public ResponseI executeAction(ActionI a, Serializable[] params) throws  Exception {
		
		return this.getOwner().handleRequest(
				new AbstractComponent.AbstractService<ResponseI>(this.getPluginURI()) {
					@Override
					public ResponseI call() throws Exception{
						return ((PluginActionExecuteIn)this.getServiceProviderReference()).
						executeAction(a, params);
					}
				});
		

	}

}
