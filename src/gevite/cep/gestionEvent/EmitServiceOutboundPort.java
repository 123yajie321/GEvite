package gevite.cep.gestionEvent;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import gevite.cep.CEPBusManagementCI;

public class EmitServiceOutboundPort extends AbstractOutboundPort implements CEPBusManagementCI {

	private static final long serialVersionUID=1L;
	
	public EmitServiceOutboundPort( ComponentI owner) throws Exception {
		super(CEPBusManagementCI.class, owner);
		
	}
	
	public EmitServiceOutboundPort(String uri,ComponentI owner) throws Exception{
		super(uri, CEPBusManagementCI.class, owner);
	
	}

	@Override
	public String registerEmitter(String uri)throws Exception {
		
		return ((CEPBusManagementCI)this.getConnector()).registerEmitter(uri);
	}

	@Override
	public void unregisterEmitter(String uri) {
		// TODO Auto-generated method stub

	}

	@Override
	public String registerCorrelator(String uri, String inboundPortURI) {
		
		return null;
	}

	@Override
	public void unregisterCorrelator(String uri) {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerExecutor(String uri, String inboundPortURI) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getExecutorInboundPortURI(String uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unregisterExecutor(String uri) {
		// TODO Auto-generated method stub

	}

	@Override
	public void subscribe(String subscriberURI, String emitterURI) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unsubscribe(String subscriberURI, String emitterURI) {
		// TODO Auto-generated method stub

	}

}
