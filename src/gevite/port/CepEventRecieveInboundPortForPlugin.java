package gevite.port;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gevite.evenement.EventI;
import gevite.interfaces.EventEmissionCI;
import gevite.plugin.PluginEmissionIn;

/**
 * The class <code>CepEventRecieveInboundPortForPlugin</code> implements an inbound port
 * for the <code>EventEmissionCI</code> component interface that directs its calls to
 * the plug-in rather than directly to the component implementation.
 * 
 * @author Yajie LIU, Zimeng ZHANG
 */


public class CepEventRecieveInboundPortForPlugin extends AbstractInboundPort implements EventEmissionCI {

	private static final long serialVersionUID = 1L;

	public CepEventRecieveInboundPortForPlugin(String uri,String pluginURI,ComponentI owner)
			throws Exception {
		super(uri,EventEmissionCI.class, owner,pluginURI,null);
	}
	
	public CepEventRecieveInboundPortForPlugin(String pluginURI,ComponentI owner)
			throws Exception {
		super(EventEmissionCI.class, owner,pluginURI,null);
	}

	@Override
	public void sendEvent(String emitterURI, EventI event) throws Exception {
	
		this.getOwner().runTask(
				new AbstractComponent.AbstractTask(this.getPluginURI()) {
					@Override
					public void run() {
						try {
							((PluginEmissionIn)this.getTaskProviderReference()).sendEvent(emitterURI, event);
						} catch (Exception e) {
						
							e.printStackTrace();
						}
						
					}
				});
		
	}

	@Override
	public void sendEvents(String emitterURI, EventI[] events) throws Exception {
		
	}

}
