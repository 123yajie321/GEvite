package gevite.plugin;

import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import gevite.cep.ActionExecutionCI;
import gevite.cep.EventEmissionCI;
import gevite.connector.ConnectorCorrelateurExecuteurPlugin;
import gevite.connector.ConnectorEmitterSend;
import gevite.connector.ConnectorEmitterSendPlugin;
import gevite.correlateur.CorrelateurActionExecutionOutboundPort;
import gevite.emitteur.EmitterSendOutboundPort;

public class PluginActionExecuteOut extends AbstractPlugin{
	
	private static final long serialVersionUID=1L;
	protected String inboundPortUri;

	protected CorrelateurActionExecutionOutboundPort caeop;
	
	@Override
	public void	installOn(ComponentI owner) throws Exception{
		super.installOn(owner);
		
		this.addRequiredInterface(ActionExecutionCI.class);
		this.caeop = new CorrelateurActionExecutionOutboundPort(this.getOwner());
		this.caeop.publishPort();
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
				this.caeop.getPortURI(),
				this.inboundPortUri, 
				ConnectorCorrelateurExecuteurPlugin.class.getCanonicalName());
		
		System.out.println(" COnnected");
		super.initialise();
	}
	
	@Override
	public void finalise() throws Exception {		
		this.getOwner().doPortDisconnection(caeop.getPortURI());
		//super.finalise();
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
