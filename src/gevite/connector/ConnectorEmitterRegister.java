package gevite.connector;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import gevite.cep.CEPBusManagementCI;

public class ConnectorEmitterRegister extends AbstractConnector implements CEPBusManagementCI {

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
