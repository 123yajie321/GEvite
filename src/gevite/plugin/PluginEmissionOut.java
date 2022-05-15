package gevite.plugin;

import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import gevite.connector.ConnectorEventEmission;
import gevite.evenement.EventI;
import gevite.interfaces.EventEmissionCI;
import gevite.port.SendEventOutboundPort;

/**
 * The class <code>PluginEmissionIn</code> implements the the client side plug-in
 * for the <code>EventEmissionCI</code> component interface and associated ports and
 * connectors.
 *    
 * @author Yajie LIU, Zimeng ZHANG
 */

public class PluginEmissionOut extends AbstractPlugin{
	
	private static final long serialVersionUID=1L;
	protected String inboundPortUri;

	protected SendEventOutboundPort emitterSendOutboundPort;
	
	@Override
	public void	installOn(ComponentI owner) throws Exception{
		super.installOn(owner);
		
		this.addRequiredInterface(EventEmissionCI.class);
		this.emitterSendOutboundPort = new SendEventOutboundPort(this.getOwner());
		this.emitterSendOutboundPort.publishPort();
	
	}
	
	public void setInboundPortUri(String uri) throws Exception {
		this.inboundPortUri = uri;
	}
	
	@Override
	public void initialise() throws Exception{
	
		this.getOwner().doPortConnection(
				this.emitterSendOutboundPort.getPortURI(),
				this.inboundPortUri, 
				ConnectorEventEmission.class.getCanonicalName());
		super.initialise();
	}
	
	@Override
	public void finalise() throws Exception {		
		this.getOwner().doPortDisconnection(emitterSendOutboundPort.getPortURI());
	
	}
	
	@Override
	public void uninstall() throws Exception {
		this.emitterSendOutboundPort.unpublishPort();
		this.emitterSendOutboundPort.destroyPort();
		this.removeRequiredInterface(EventEmissionCI.class);
	}
	
	public EventEmissionCI getEmissionService() {
		return this.emitterSendOutboundPort;
	}



	

}
