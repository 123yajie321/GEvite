package gevite.cep.gestionEvent;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gevite.cep.CEPBusManagementCI;

public class CepServiceInboundPort extends AbstractInboundPort implements CEPBusManagementCI {
	
	private static final long serialVersionUID=1L;

	public CepServiceInboundPort(ComponentI owner) throws Exception {
		super(CEPBusManagementCI.class,owner);
		
	}
	
	public CepServiceInboundPort(String uri,ComponentI owner) throws Exception{
		super(uri, CEPBusManagementCI.class,owner);
		
	}

	@Override
	public String registerEmitter(String uri)throws Exception {
		
		
		return this.getOwner().handleRequest(
				cep-> ((CEPBus)cep).registerEmitter(uri)
						);
	}

	@Override
	public void unregisterEmitter(String uri) {
		// TODO Auto-generated method stub

	}

	@Override
	public String registerCorrelator(String uri, String inboundPortURI) {
		// TODO Auto-generated method stub
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
