package gevite.plugin;

import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import gevite.cep.EventEmissionCI;
import gevite.connector.ConnectorEmitterSend;
import gevite.connector.ConnectorEmitterSendPlugin;
import gevite.emitteur.EmitterSendOutboundPort;
import gevite.evenement.EventI;

public class PluginEmissionOut extends AbstractPlugin{
	
	private static final long serialVersionUID=1L;
	protected String inboundPortUri;

	protected EmitterSendOutboundPort emitterSendOutboundPort;
	
	@Override
	public void	installOn(ComponentI owner) throws Exception{
		super.installOn(owner);
		
		this.addRequiredInterface(EventEmissionCI.class);
		this.emitterSendOutboundPort = new EmitterSendOutboundPort(this.getOwner());
		this.emitterSendOutboundPort.publishPort();
		System.out.println(" out install");
	}
	
	public void setInboundPortUri(String uri) throws Exception {
		this.inboundPortUri = uri;
	}
	
	@Override
	public void initialise() throws Exception{
		//this.addRequiredInterface(ReflectionCI.class);
		//ReflectionOutboundPort  rop= new ReflectionOutboundPort(this.getOwner());
		this.getOwner().doPortConnection(
				this.emitterSendOutboundPort.getPortURI(),
				this.inboundPortUri, 
				ConnectorEmitterSendPlugin.class.getCanonicalName());
		super.initialise();
	}
	
	@Override
	public void finalise() throws Exception {		
		this.getOwner().doPortDisconnection(emitterSendOutboundPort.getPortURI());
		//super.finalise();
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
