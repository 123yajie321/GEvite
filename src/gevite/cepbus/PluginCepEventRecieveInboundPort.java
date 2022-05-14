package gevite.cepbus;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gevite.cep.EventEmissionCI;
import gevite.evenement.EventI;
import gevite.plugin.PluginEmissionIn;

public class PluginCepEventRecieveInboundPort extends AbstractInboundPort implements EventEmissionCI {

	public PluginCepEventRecieveInboundPort(String uri,String pluginURI,ComponentI owner)
			throws Exception {
		super(uri,EventEmissionCI.class, owner,pluginURI,null);
	}
	
	public PluginCepEventRecieveInboundPort(String pluginURI,ComponentI owner)
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
