package gevite.cep.gestionEvent;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import gevite.cep.CEPBusManagementCI;

@OfferedInterfaces(offered = {CEPBusManagementCI.class})
public class CEPBus extends AbstractComponent{
	
	public static final String CEPIP_URI = "cepip-uri";
	
	protected CepServiceInboundPort cepip;

	protected CEPBus()throws Exception {
		super(1, 0);
		this.cepip = new CepServiceInboundPort(CEPIP_URI,this); 
		this.cepip.publishPort();

	}
	
	public String registerEmitter(String uri) {
		System.out.println("Emetteur : "+ uri +"registed");
		return uri;
	}
	
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.cepip.unpublishPort();
			
		} catch (Exception e) {
			throw new ComponentShutdownException(e);		
		}
		super.shutdown();
	}

	
	


}
