package gevite.correlateur;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import gevite.cep.CEPBusManagementCI;
import gevite.cep.EventReceptionCI;

public class CorrelateurCepServicesOutboundPort extends AbstractOutboundPort implements CEPBusManagementCI {
	
	
	

	private static final long serialVersionUID = 1L;

	public CorrelateurCepServicesOutboundPort(ComponentI owner)
			throws Exception {
		super(CEPBusManagementCI.class, owner);
	}
	
	public CorrelateurCepServicesOutboundPort(String uri,ComponentI owner)
			throws Exception {
		super(uri,CEPBusManagementCI.class, owner);
	}
	@Override
	public String registerEmitter(String uri) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unregisterEmitter(String uri) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String registerCorrelator(String uri, String inboundPortURI) throws Exception {
		return ((CEPBusManagementCI)this.getConnector()).registerCorrelator(uri, inboundPortURI);

	}

	@Override
	public void unregisterCorrelator(String uri) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerExecutor(String uri, String inboundPortURI) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String getExecutorInboundPortURI(String uri) throws Exception {
		
		return  ((CEPBusManagementCI)this.getConnector()).getExecutorInboundPortURI(uri);
	}

	@Override
	public void unregisterExecutor(String uri) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void subscribe(String subscriberURI, String emitterURI) throws Exception {
		
		 ((CEPBusManagementCI)this.getConnector()).subscribe(subscriberURI, emitterURI);
	}

	@Override
	public void unsubscribe(String subscriberURI, String emitterURI) throws Exception {
		// TODO Auto-generated method stub

	}

}