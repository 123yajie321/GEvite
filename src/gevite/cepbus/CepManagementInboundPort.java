package gevite.cepbus;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gevite.cep.CEPBusManagementCI;

public class CepManagementInboundPort extends AbstractInboundPort implements CEPBusManagementCI {
	
	private static final long serialVersionUID=1L;

	public CepManagementInboundPort(ComponentI owner) throws Exception {
		super(CEPBusManagementCI.class,owner);
		
	}
	
	public CepManagementInboundPort(String uri,ComponentI owner) throws Exception{
		super(uri, CEPBusManagementCI.class,owner);
		
	}

	@Override
	public String registerEmitter(String uri)throws Exception {
		
		
		return this.getOwner().handleRequest(
				cep-> ((CEPBus)cep).registerEmitter(uri)
						);
	}

	@Override
	public void unregisterEmitter(String uri) throws Exception{
		this.getOwner().handleRequest(
				cep-> ((CEPBus)cep).unregisterEmitter(uri)
						);
	}

	@Override
	public String registerCorrelator(String uri, String inboundPortURI)throws Exception {
		return this.getOwner().handleRequest(
				cep-> ((CEPBus)cep).registerCorrelator(uri, inboundPortURI)
						);
	}

	@Override
	public void unregisterCorrelator(String uri)throws Exception {
		this.getOwner().handleRequest(
				cep-> ((CEPBus)cep).unregisterCorrelator(uri)
						);
	}

	@Override
	public void registerExecutor(String uri, String inboundPortURI)throws Exception {
		
		this.getOwner().handleRequest(
				cep-> ((CEPBus)cep).registerExecuteur(uri,inboundPortURI)
						);
	}

	@Override
	public String getExecutorInboundPortURI(String uri)throws Exception {
		return this.getOwner().handleRequest(
				cep-> ((CEPBus)cep).getExecutorInboundPortURI(uri)
				);
		
	}

	@Override
	public void unregisterExecutor(String uri)throws Exception {
		this.getOwner().handleRequest(
				cep-> ((CEPBus)cep).unregisterExecutor(uri)
						);

	}

	@Override
	public void subscribe(String subscriberURI, String emitterURI)throws Exception {
		this.getOwner().handleRequest(
				cep-> ((CEPBus)cep).subscribe(subscriberURI, emitterURI)
						);

	}

	@Override
	public void unsubscribe(String subscriberURI, String emitterURI)throws Exception {
		this.getOwner().handleRequest(
				cep-> ((CEPBus)cep).unsubscribe(subscriberURI, emitterURI)
						);
	}

}
