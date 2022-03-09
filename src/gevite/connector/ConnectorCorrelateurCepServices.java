package gevite.connector;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import gevite.cep.CEPBusManagementCI;

public class ConnectorCorrelateurCepServices extends AbstractConnector implements CEPBusManagementCI {

	@Override
	public String registerEmitter(String uri) throws Exception {
		return null;
	}

	@Override
	public void unregisterEmitter(String uri) throws Exception{
		// TODO Auto-generated method stub

	}

	@Override
	public String registerCorrelator(String uri, String inboundPortURI) throws Exception {
		return ((CEPBusManagementCI)this.offering).registerCorrelator(uri, inboundPortURI);
	}

	@Override
	public void unregisterCorrelator(String uri)throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerExecutor(String uri, String inboundPortURI)throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String getExecutorInboundPortURI(String uri) throws Exception {
		
		return ((CEPBusManagementCI)this.offering).getExecutorInboundPortURI(uri);
	}

	@Override
	public void unregisterExecutor(String uri)throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void subscribe(String subscriberURI, String emitterURI) throws Exception {
		((CEPBusManagementCI)this.offering).subscribe(subscriberURI, emitterURI);
	}

	@Override
	public void unsubscribe(String subscriberURI, String emitterURI)throws Exception {
		// TODO Auto-generated method stub

	}

}
