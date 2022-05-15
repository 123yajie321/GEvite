package gevite.port;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gevite.CEPBus;
import gevite.interfaces.CEPBusManagementCI;

/**
 * The class <code>CepManagementInboundPort</code> implements an inbound port for the
 * <code>CEPBusManagementCI</code> component interface.
 * @author	Yajie LIU, Zimeng ZHANG
 */


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
		this.getOwner().runTask(
				cep-> {try {
					((CEPBus)cep).unregisterEmitter(uri);
				} catch (Exception e) {
					e.printStackTrace();
				}}
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
		this.getOwner().runTask(
				cep-> {try {
					((CEPBus)cep).unregisterCorrelator(uri);
				} catch (Exception e) {
					e.printStackTrace();
				}
				});
	}

	@Override
	public void registerExecutor(String uri, String inboundPortURI)throws Exception {
		
		this.getOwner().handleRequest(
					cep-> {((CEPBus)cep).registerExecuteur(uri,inboundPortURI);
					return null;
				});
	}

	@Override
	public String getExecutorInboundPortURI(String uri)throws Exception {
		return this.getOwner().handleRequest(
				cep-> ((CEPBus)cep).getExecutorInboundPortURI(uri)
				);
		
	}

	@Override
	public void unregisterExecutor(String uri)throws Exception {
		this.getOwner().runTask(
				cep-> {try {
					((CEPBus)cep).unregisterExecutor(uri);
				} catch (Exception e) {
				
					e.printStackTrace();
				}
				
				});

	}

	@Override
	public void subscribe(String subscriberURI, String emitterURI)throws Exception {
		this.getOwner().handleRequest(
				cep-> {((CEPBus)cep).subscribe(subscriberURI, emitterURI);
				return null;}
						);

	}

	@Override
	public void unsubscribe(String subscriberURI, String emitterURI)throws Exception {
		this.getOwner().runTask(
				cep-> {try {
					((CEPBus)cep).unsubscribe(subscriberURI, emitterURI);
				} catch (Exception e) {
					e.printStackTrace();
				}}
						);
	}

}
