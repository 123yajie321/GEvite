package gevite.plugin;

import java.io.Serializable;

import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import gevite.actions.ActionI;
import gevite.cep.ActionExecutionCI;
import gevite.cep.EventEmissionCI;
import gevite.cep.ResponseI;
import gevite.cepbus.CEPBus;
import gevite.cepbus.CepEventRecieveInboundPort;
import gevite.cepbus.PluginCepEventRecieveInboundPort;
import gevite.evenement.EventI;
import gevite.samu.PluginSAMUActionExecutionInboundPort;

public class PluginActionExecuteIn extends AbstractPlugin implements ActionExecutionCI{
	private static final long serialVersionUID=1L;
	
	protected PluginSAMUActionExecutionInboundPort actionExecutionPluginInboundPort;
	//protected String PortUri; 
	
	public PluginActionExecuteIn() {
		super();
		//this.PortUri=uri;
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
		this.addOfferedInterface(ActionExecutionCI.class);
		this.actionExecutionPluginInboundPort = new PluginSAMUActionExecutionInboundPort(this.getOwner());
		this.actionExecutionPluginInboundPort.publishPort();
}
	@Override
	public void uninstall() throws Exception {
		this.actionExecutionPluginInboundPort.unpublishPort();
		this.actionExecutionPluginInboundPort.destroyPort();
		this.removeRequiredInterface(ActionExecutionCI.class);
	}

	@Override
	public ResponseI execute(ActionI a, Serializable[] params) throws Exception {
		
		System.out.println("begin Execute PLugin in");
		
		return ((ActionExecutionCI)this.getOwner()).execute(a, params);
		//System.out.println("fin Execute PLugin in");
	
	}
	
	public String getActionEecutionService() throws Exception {
		return this.actionExecutionPluginInboundPort.getPortURI();
	}

	
	
}