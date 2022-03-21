package gevite.plugin;

import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import gevite.cep.EventEmissionCI;
import gevite.cepbus.CEPBus;
import gevite.cepbus.CepEventRecieveInboundPort;
import gevite.cepbus.PluginCepEventRecieveInboundPort;
import gevite.evenement.EventI;

public class PluginEmissionIn extends AbstractPlugin implements EventEmissionCI{
	private static final long serialVersionUID=1L;
	
	protected PluginCepEventRecieveInboundPort receivePluginInboundPort;
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
		//this.addRequiredInterface(ReflectionCI.class);
		//ReflectionOutboundPort  rop= new ReflectionOutboundPort(this.getOwner());
		super.initialise();
		this.addOfferedInterface(EventEmissionCI.class);
		this.receivePluginInboundPort = new PluginCepEventRecieveInboundPort(PortUri,this.getPluginURI(),this.getOwner());
		this.receivePluginInboundPort.publishPort();
}
	@Override
	public void uninstall() throws Exception {
		this.receivePluginInboundPort.unpublishPort();
		this.receivePluginInboundPort.destroyPort();
		this.removeRequiredInterface(CepEventRecieveInboundPort.class);
	}
	
	@Override
	public void sendEvent(String emitterURI, EventI event) throws Exception {
		System.out.println("begin Send PLugin in");
		/*
		this.getOwner().handleRequest(
				cep -> {	((CEPBus)cep).
									recieveEvent(emitterURI, event);;
						return null;
					 });
					 */
		((EventEmissionCI)this.getOwner()).sendEvent(emitterURI, event);;
		System.out.println("fin Send PLugin in");
	}

	@Override
	public void sendEvents(String emitterURI, EventI[] events) {
		

	}
	
}