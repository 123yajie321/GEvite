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
		System.out.println("begin Send port");
		this.getOwner().handleRequest(
				new AbstractComponent.AbstractService<Void>(this.getPluginURI()) {
					@Override
					public Void call() throws Exception{
						((PluginEmissionIn)this.getServiceProviderReference()).
						sendEvent(emitterURI, event);
						
						return null;
					}
				});
		System.out.println("fin Send port");
	}

	@Override
	public void sendEvents(String emitterURI, EventI[] events) throws Exception {
		
	}

}
