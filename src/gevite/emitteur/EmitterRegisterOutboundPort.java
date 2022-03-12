package gevite.emitteur;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import gevite.cep.CEPBusManagementCI;

public class EmitterRegisterOutboundPort extends AbstractOutboundPort implements CEPBusManagementCI {

	private static final long serialVersionUID=1L;
	

	
	public EmitterRegisterOutboundPort( ComponentI owner) throws Exception {
		super(CEPBusManagementCI.class, owner);
		
	}
	
	public EmitterRegisterOutboundPort(String uri,ComponentI owner) throws Exception{
		super(uri, CEPBusManagementCI.class, owner);
	
	}

	@Override
	public String registerEmitter(String uri)throws Exception {
		return ((CEPBusManagementCI)this.getConnector()).registerEmitter(uri);
	}

	@Override
	public void unregisterEmitter(String uri) throws Exception {
		((CEPBusManagementCI)this.getConnector()).unregisterEmitter(uri);

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
