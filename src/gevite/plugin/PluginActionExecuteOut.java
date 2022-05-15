package gevite.plugin;

import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import gevite.connector.ConnectorCorrelateurExecuteur;
import gevite.connector.ConnectorEventEmission;
import gevite.interfaces.ActionExecutionCI;
import gevite.interfaces.EventEmissionCI;
import gevite.port.ActionExecutionOutboundPort;
import gevite.port.SendEventOutboundPort;

/**
 * The class <code>PluginActionExecuteOut</code> implements the the client side  plug-in
 * for the <code>ActionExecutionCI</code> component interface and associated ports and
 * connectors.
 *    
 * @author Yajie LIU, Zimeng ZHANG
 */

public class PluginActionExecuteOut extends AbstractPlugin{
	
	private static final long serialVersionUID=1L;
	protected String inboundPortUri;

	protected ActionExecutionOutboundPort caeop;
	
	@Override
	public void	installOn(ComponentI owner) throws Exception{
		super.installOn(owner);
		
		this.addRequiredInterface(ActionExecutionCI.class);
		this.caeop = new ActionExecutionOutboundPort(this.getOwner());
		this.caeop.publishPort();
		
	}
	
	public void setInboundPortUri(String uri) throws Exception {
		this.inboundPortUri = uri;
	}
	
	@Override
	public void initialise() throws Exception{
		this.getOwner().doPortConnection(
				this.caeop.getPortURI(),
				this.inboundPortUri, 
				ConnectorCorrelateurExecuteur.class.getCanonicalName());
		
		super.initialise();
	}
	
	@Override
	public void finalise() throws Exception {		
		this.getOwner().doPortDisconnection(caeop.getPortURI());
	}
	
	@Override
	public void uninstall() throws Exception {
		this.caeop.unpublishPort();
		this.caeop.destroyPort();
		this.removeRequiredInterface(ActionExecutionCI.class);
	}
	
	public ActionExecutionCI getActionEecutionService() {
		return this.caeop;
	}

	

}
