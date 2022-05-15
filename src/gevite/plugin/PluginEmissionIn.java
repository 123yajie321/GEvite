package gevite.plugin;

import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import gevite.evenement.EventI;
import gevite.interfaces.EventEmissionCI;
import gevite.interfaces.EventEmissionImplementationI;
import gevite.port.CepEventRecieveInboundPortForPlugin;

/**
 * The class <code>PluginEmissionIn</code> implements the the CepBus component side  plug-in
 * for the <code>EventEmissionCI</code> component interface and associated ports and
 * connectors.
 *    
 * @author Yajie LIU, Zimeng ZHANG
 */



public class PluginEmissionIn extends AbstractPlugin implements EventEmissionCI{
	private static final long serialVersionUID=1L;
	
	protected CepEventRecieveInboundPortForPlugin receivePluginInboundPort;
	protected String PortUri; 
	
	public PluginEmissionIn(String uri) {
		super();
		this.PortUri=uri;
	}
	
	@Override
	public void	installOn(ComponentI owner) throws Exception{
		super.installOn(owner);
	}
	
	@Override
	public void initialise() throws Exception{
		super.initialise();
		this.addOfferedInterface(EventEmissionCI.class);
		this.receivePluginInboundPort = new CepEventRecieveInboundPortForPlugin(PortUri,this.getPluginURI(),this.getOwner());
		this.receivePluginInboundPort.publishPort();
}
	@Override
	public void uninstall() throws Exception {
		this.receivePluginInboundPort.unpublishPort();
		this.receivePluginInboundPort.destroyPort();
		this.removeOfferedInterface(EventEmissionCI.class);
	}
	
	@Override
	public void sendEvent(String emitterURI, EventI event) throws Exception {
	
		((EventEmissionImplementationI)this.getOwner()).sendEvent(emitterURI, event);;
	
	}

	@Override
	public void sendEvents(String emitterURI, EventI[] events) {
		

	}
	
}