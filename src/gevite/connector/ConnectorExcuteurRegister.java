package gevite.connector;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import gevite.cep.CEPBusManagementCI;

public class ConnectorExcuteurRegister extends AbstractConnector implements CEPBusManagementCI {

	@Override
	public String registerEmitter(String uri) throws Exception {
		return null;
	}

	@Override
	public void unregisterEmitter(String uri) {
		// TODO Auto-generated method stub

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
	public void registerExecutor(String uri, String inboundPortURI) throws Exception {
		((CEPBusManagementCI)this.offering).registerExecutor(uri,inboundPortURI);
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
