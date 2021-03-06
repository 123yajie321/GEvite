package gevite.port;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import gevite.interfaces.CEPBusManagementCI;
/**
 * The class <code>CepManagementOutboundPort</code> implements an outbound port for the
 * <code>CEPBusManagementCI</code> component interface.
 * @author	Yajie LIU, Zimeng ZHANG
 */

public class CepManagementOutboundPort extends AbstractOutboundPort implements CEPBusManagementCI {

	private static final long serialVersionUID = 1L;

	public CepManagementOutboundPort(ComponentI owner)
			throws Exception {
		super(CEPBusManagementCI.class, owner);
		
	}
	
	public CepManagementOutboundPort(String uri,ComponentI owner)
			throws Exception {
		super(uri,CEPBusManagementCI.class, owner);
	}

	@Override
	public String registerEmitter(String uri) throws Exception {
		return ((CEPBusManagementCI)this.getConnector()).registerEmitter(uri);
	}

	@Override
	public void unregisterEmitter(String uri) throws Exception {
		((CEPBusManagementCI)this.getConnector()).unregisterEmitter(uri);
		
	}

	@Override
	public String registerCorrelator(String uri, String inboundPortURI) throws Exception {
		return ((CEPBusManagementCI)this.getConnector()).registerCorrelator(uri, inboundPortURI);
	}

	@Override
	public void unregisterCorrelator(String uri) throws Exception {
		((CEPBusManagementCI)this.getConnector()).unregisterCorrelator(uri);
		
	}

	@Override
	public void registerExecutor(String uri, String inboundPortURI) throws Exception {
		((CEPBusManagementCI)this.getConnector()).registerExecutor(uri,inboundPortURI);
		
	}

	@Override
	public String getExecutorInboundPortURI(String uri) throws Exception {
		return  ((CEPBusManagementCI)this.getConnector()).getExecutorInboundPortURI(uri);
	}

	@Override
	public void unregisterExecutor(String uri) throws Exception {
		((CEPBusManagementCI)this.getConnector()).unregisterExecutor(uri);
		
	}

	@Override
	public void subscribe(String subscriberURI, String emitterURI) throws Exception {
		 ((CEPBusManagementCI)this.getConnector()).subscribe(subscriberURI, emitterURI);
		
	}

	@Override
	public void unsubscribe(String subscriberURI, String emitterURI) throws Exception {
		((CEPBusManagementCI)this.getConnector()).unsubscribe(subscriberURI, emitterURI);
		
	}

}
