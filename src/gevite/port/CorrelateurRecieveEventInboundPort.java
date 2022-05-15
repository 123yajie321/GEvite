package gevite.port;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gevite.correlateur.CorrelateurPompier;
import gevite.correlateur.CorrelateurSamu;
import gevite.correlateur.CorrelateurtTraffic;
import gevite.evenement.EventI;
import gevite.interfaces.EventEmissionCI;
import gevite.interfaces.EventReceptionCI;

/**
 * The class <code>CorrelateurRecieveEventInboundPort</code> implements an inbound port for the
 * <code>EventReceptionCI</code> component interface which allows component correlateur to receive Event from CepBus
 * @author	Yajie LIU, Zimeng ZHANG
 */

public class CorrelateurRecieveEventInboundPort extends AbstractInboundPort implements EventReceptionCI {

	private static final long serialVersionUID = 1L;

	public CorrelateurRecieveEventInboundPort(ComponentI owner)
			throws Exception {
		super(EventReceptionCI.class,owner);
	}
	
	public CorrelateurRecieveEventInboundPort(String uri,ComponentI owner)
			throws Exception {
		super(uri,EventReceptionCI.class, owner);
	}

	@Override
	public void receiveEvent(String emitterURI, EventI e) throws Exception {
		this.getOwner().runTask( c-> {
			try { 
				if(c instanceof CorrelateurSamu) {((CorrelateurSamu)c).addEvent(emitterURI, e);}
				else if(c instanceof CorrelateurPompier) {((CorrelateurPompier)c).addEvent(emitterURI, e);}
				else {((CorrelateurtTraffic)c).addEvent(emitterURI, e);}
				
			} catch (Exception e1) {
			
				e1.printStackTrace();
			}
			
		});		
	}

	@Override
	public void receiveEvents(String emitterURI, EventI[] events) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
