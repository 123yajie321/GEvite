package gevite.port;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gevite.CEPBus;
import gevite.correlateur.CorrelateurPompier;
import gevite.correlateur.CorrelateurSamu;
import gevite.correlateur.CorrelateurtTraffic;
import gevite.evenement.EventI;
import gevite.interfaces.BusEventEmissionCI;
import gevite.interfaces.EventEmissionCI;
import gevite.interfaces.EventReceptionCI;

/**
 * The class <code>BusEventEmissionInboundPort</code> implements an inbound port for the
 * <code>BusEventEmissionCI</code> component interface which allows component Bus to receive Event from other CepBus
 * @author	Yajie LIU, Zimeng ZHANG
 */

public class BusEventEmissionInboundPort extends AbstractInboundPort implements BusEventEmissionCI {

	private static final long serialVersionUID = 1L;

	public BusEventEmissionInboundPort(ComponentI owner)
			throws Exception {
		super(BusEventEmissionCI.class,owner);
	}
	
	public BusEventEmissionInboundPort(String uri,ComponentI owner)
			throws Exception {
		super(uri,BusEventEmissionCI.class, owner);
	}

	
	@Override
	public void sendOtherBusEvent(String emitterURI, EventI event, String busId) throws Exception {
		this.getOwner().runTask( c-> {
			try { 
				((CEPBus)c).receiveEventOtherBus(emitterURI, event, busId);
				
			} catch (Exception e1) {
			
				e1.printStackTrace();
			}
			
		});		
	}

}
