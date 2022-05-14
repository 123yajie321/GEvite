package gevite.plugin;

import java.io.Serializable;

import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import gevite.actions.ActionI;
import gevite.cep.ActionExecutionCI;
import gevite.cep.ActionExecutionImplementationCI;
import gevite.cep.EventEmissionCI;
import gevite.cep.ResponseI;
import gevite.cepbus.CEPBus;
import gevite.cepbus.CepEventRecieveInboundPort;
import gevite.cepbus.PluginCepEventRecieveInboundPort;
import gevite.evenement.EventI;

public class PluginActionExecuteIn extends AbstractPlugin implements ActionExecutionCI{
	private static final long serialVersionUID=1L;
	
	protected PluginActionExecutionInboundPort actionExecutionPluginInboundPort;

	
	public PluginActionExecuteIn() {
		super();
	
	}
	
	@Override
	public void	installOn(ComponentI owner) throws Exception{
		super.installOn(owner);
	}
	
	@Override
	public void initialise() throws Exception{
		super.initialise();
		this.addOfferedInterface(ActionExecutionCI.class);
		this.actionExecutionPluginInboundPort = new PluginActionExecutionInboundPort(this.getPluginURI(),this.getOwner());
		this.actionExecutionPluginInboundPort.publishPort();
}
	@Override
	public void uninstall() throws Exception {
		this.actionExecutionPluginInboundPort.unpublishPort();
		this.actionExecutionPluginInboundPort.destroyPort();
		this.removeOfferedInterface(ActionExecutionCI.class);
	}

	@Override
	public ResponseI executeAction(ActionI a, Serializable[] params) throws Exception {
	
		return ((ActionExecutionImplementationCI)this.getOwner()).executeAction(a, params);
		
	
	}
	
	public String getActionEecutionService() throws Exception {
		return this.actionExecutionPluginInboundPort.getPortURI();
	}

	
	
}