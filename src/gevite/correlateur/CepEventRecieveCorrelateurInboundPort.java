package gevite.correlateur;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gevite.cep.EventEmissionCI;
import gevite.cep.EventReceptionCI;
import gevite.evenement.EventI;

public class CepEventRecieveCorrelateurInboundPort extends AbstractInboundPort implements EventReceptionCI {

	private static final long serialVersionUID = 1L;

	public CepEventRecieveCorrelateurInboundPort(ComponentI owner)
			throws Exception {
		super(EventEmissionCI.class,owner);
	}
	
	public CepEventRecieveCorrelateurInboundPort(String uri,ComponentI owner)
			throws Exception {
		super(uri,EventEmissionCI.class, owner);
	}

	@Override
	public void receiveEvent(String emitterURI, EventI e) throws Exception {
		this.getOwner().runTask(c-> {
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
