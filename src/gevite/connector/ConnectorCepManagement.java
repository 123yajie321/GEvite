package gevite.connector;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import gevite.interfaces.CEPBusManagementCI;

/**
 * The class <code>ConnectorCepManagement</code> implements a connector for the
 * <code>CEPBusManagementCI</code> component interface.
 * @author	Yajie LIU, Zimeng ZHANG
 */
public class ConnectorCepManagement extends AbstractConnector implements CEPBusManagementCI {

	@Override
	public String registerEmitter(String uri) throws Exception {
		return ((CEPBusManagementCI)this.offering).registerEmitter(uri);
	}

	@Override
	public void unregisterEmitter(String uri) throws Exception {
		((CEPBusManagementCI)this.offering).unregisterEmitter(uri);

	}

	@Override
	public String registerCorrelator(String uri, String inboundPortURI) throws Exception {
		return ((CEPBusManagementCI)this.offering).registerCorrelator(uri, inboundPortURI);
	}

	@Override
	public void unregisterCorrelator(String uri) throws Exception {
		((CEPBusManagementCI)this.offering).unregisterCorrelator(uri);

	}

	@Override
	public void registerExecutor(String uri, String inboundPortURI) throws Exception {
		((CEPBusManagementCI)this.offering).registerExecutor(uri, inboundPortURI);

	}

	@Override
	public String getExecutorInboundPortURI(String uri) throws Exception {
		return ((CEPBusManagementCI)this.offering).getExecutorInboundPortURI(uri);
	}

	@Override
	public void unregisterExecutor(String uri) throws Exception {
		((CEPBusManagementCI)this.offering).unregisterExecutor(uri);

	}

	@Override
	public void subscribe(String subscriberURI, String emitterURI) throws Exception {
		((CEPBusManagementCI)this.offering).subscribe(subscriberURI, emitterURI);

	}

	@Override
	public void unsubscribe(String subscriberURI, String emitterURI) throws Exception {
		((CEPBusManagementCI)this.offering).unsubscribe(subscriberURI, emitterURI);

	}

}
