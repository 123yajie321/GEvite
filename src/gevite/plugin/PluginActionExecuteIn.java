package gevite.plugin;

import java.io.Serializable;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import gevite.actions.ActionI;
import gevite.interfaces.ActionExecutionCI;
import gevite.interfaces.ActionExecutionImplementationI;
import gevite.interfaces.ResponseI;
import gevite.port.ActionExecutionInboundPortForPlugin;

/**
 * The class <code>PluginActionExecuteIn</code> implements the the executor component side  plug-in
 * for the <code>ActionExecutionCI</code> component interface and associated ports and
 * connectors.
 *    
 * @author Yajie LIU, Zimeng ZHANG
 */

public class PluginActionExecuteIn extends AbstractPlugin implements ActionExecutionCI{
	private static final long serialVersionUID=1L;
	
	protected ActionExecutionInboundPortForPlugin actionExecutionPluginInboundPort;

	
	public PluginActionExecuteIn() {
		super();
	
	}
	
	@Override
	public void	installOn(ComponentI owner) throws Exception{
		super.installOn(owner);
	}
	
	@Override
	public void initialise() throws Exception{
		super.initialise();
		this.addOfferedInterface(ActionExecutionCI.class);
		this.actionExecutionPluginInboundPort = new ActionExecutionInboundPortForPlugin(this.getPluginURI(),this.getOwner());
		this.actionExecutionPluginInboundPort.publishPort();
}
	@Override
	public void uninstall() throws Exception {
		this.actionExecutionPluginInboundPort.unpublishPort();
		this.actionExecutionPluginInboundPort.destroyPort();
		this.removeOfferedInterface(ActionExecutionCI.class);
	}

	@Override
	public ResponseI executeAction(ActionI a, Serializable[] params) throws Exception {
	
		return ((ActionExecutionImplementationI)this.getOwner()).executeAction(a, params);
		
	
	}
	
	public String getActionEecutionService() throws Exception {
		return this.actionExecutionPluginInboundPort.getPortURI();
	}

	
	
}