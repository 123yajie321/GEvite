package gevite.cepbus;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gevite.cep.BusEventEmissionCI;
import gevite.cep.CEPBusManagementCI;
import gevite.evenement.EventI;

public class BusReceiveInboundPort extends AbstractInboundPort implements BusEventEmissionCI {
	
	private static final long serialVersionUID=1L;

	public BusReceiveInboundPort(ComponentI owner) throws Exception {
		super(BusEventEmissionCI.class,owner);
		
	}
	
	public BusReceiveInboundPort(String uri,ComponentI owner) throws Exception{
		super(uri, BusEventEmissionCI.class,owner);
		
	}

	
	@Override
	public void sendOtherBusEvent(String emitterURI, EventI event, String busId) throws Exception {
		
		
		this.getOwner().runTask(
				bus-> {
						try {
							((CEPBus)bus).receiveEventOtherBus(emitterURI, event, busId);
						} catch (Exception e) {
							
							e.printStackTrace();
						}
				});
		
	 }

}
