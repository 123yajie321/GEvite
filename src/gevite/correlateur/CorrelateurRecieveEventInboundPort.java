package gevite.correlateur;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gevite.cep.EventEmissionCI;
import gevite.cep.EventReceptionCI;
import gevite.evenement.EventI;

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
		this.getOwner().handleRequest(c-> {
			try { 
				if(c instanceof CorrelateurSamu) {((CorrelateurSamu)c).addEvent(emitterURI, e);}
				else if(c instanceof CorrelateurPompier) {((CorrelateurPompier)c).addEvent(emitterURI, e);}
				else {((CorrelateurtTraffic)c).addEvent(emitterURI, e);}
				
			} catch (Exception e1) {
			
				e1.printStackTrace();
			}
			return null;
		});		
	}

	@Override
	public void receiveEvents(String emitterURI, EventI[] events) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
