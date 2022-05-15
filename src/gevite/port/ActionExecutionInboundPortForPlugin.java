package gevite.port;

import java.io.Serializable;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;

import fr.sorbonne_u.components.ports.AbstractInboundPort;

import gevite.actions.ActionI;
import gevite.interfaces.ActionExecutionCI;
import gevite.interfaces.ResponseI;
import gevite.plugin.PluginActionExecuteIn;

/**
 * The class <code>ActionExecutionInboundPortForPlugin</code> implements an inbound port
 * for the <code>ActionExecutionCI</code> component interface that directs its calls to
 * the plug-in rather than directly to the component implementation.
 * 
 * @author Yajie LIU, Zimeng ZHANG
 */

public class ActionExecutionInboundPortForPlugin extends AbstractInboundPort implements ActionExecutionCI {

	private static final long serialVersionUID = 1L;

	public ActionExecutionInboundPortForPlugin(String pluginURI, ComponentI owner) throws Exception {
		super(ActionExecutionCI.class, owner,pluginURI,null);
		
	}
	
	public ActionExecutionInboundPortForPlugin(String uri,String pluginURI, ComponentI owner) throws Exception {
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
