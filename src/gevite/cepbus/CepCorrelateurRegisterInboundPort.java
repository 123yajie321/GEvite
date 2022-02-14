package gevite.cepbus;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gevite.cep.CEPBusManagementCI;

public class CepCorrelateurRegisterInboundPort extends AbstractInboundPort implements CEPBusManagementCI{

	private static final long serialVersionUID=1L;

	public CepCorrelateurRegisterInboundPort(ComponentI owner)
			throws Exception {
		super(CEPBusManagementCI.class, owner);
	}
	
	public CepCorrelateurRegisterInboundPort(String uri,ComponentI owner)
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
		return this.getOwner().handleRequest(
				cep-> ((CEPBus)cep).registerCorrelator(uri, inboundPortURI)
						);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unregisterExecutor(String uri) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void subscribe(String subscriberURI, String emitterURI) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unsubscribe(String subscriberURI, String emitterURI) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
