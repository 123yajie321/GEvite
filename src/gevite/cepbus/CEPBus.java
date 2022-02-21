package gevite.cepbus;

import java.util.HashMap;
import java.util.HashSet;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import gevite.cep.CEPBusManagementCI;
import gevite.cep.EventReceptionCI;
import gevite.evenement.EventI;

@OfferedInterfaces(offered = {CEPBusManagementCI.class})
@RequiredInterfaces(required = {EventReceptionCI.class})

public class CEPBus extends AbstractComponent{
	
	public static final String CRIP_URI = "crip-uri";
	public static final String CEREIP_URI = "cereip-uri";
	public static final String CERCIP_URI = "cercip-uri";
	public static final String CESCOP_URI = "cescop-uri";

	
    protected HashMap<EventI, String>eventEmitter;
	protected HashSet<String> uriEmitters;
	protected HashSet<String> uriCorrelateur;
	protected HashMap<String,String> uriSubscription;

	
	protected CepRegisterInboundPort crip;
	protected CepEventRecieveEmitterInboundPort cereip;
	protected CepEventRecieveCorrelateurInboundPort cercip;
	protected CepEventSendCorrelateurOutboundPort cescop;


	protected CEPBus()throws Exception {
		super(1, 0);
		
		uriEmitters = new HashSet<String>();
		uriCorrelateur = new HashSet<String>();
		uriSubscription = new HashMap<String,String>();
		eventEmitter=new HashMap<EventI,String>();
		
		this.crip = new CepRegisterInboundPort(CRIP_URI,this); 
		this.cereip = new CepEventRecieveEmitterInboundPort(CEREIP_URI,this);
		this.cercip = new CepEventRecieveCorrelateurInboundPort(CERCIP_URI, this);
		this.cescop = new CepEventSendCorrelateurOutboundPort(CESCOP_URI, this);
		this.crip.publishPort();
		this.cereip.publishPort();
		this.cercip.publishPort();
		this.cescop.publishPort();

	}
	
	public String registerEmitter(String uri)throws Exception {
		System.out.println("Emetteur : "+ uri +" registed");
		uriEmitters.add(uri);
		return this.CEREIP_URI;
	}
	
	public String registerCorrelator(String uri, String inboundPortURI) throws Exception{
		uriCorrelateur.add(uri);
		System.out.println(" RegisterCorrelateur: " + uri);
		return " port recevoir";// le port pour recevoir le event depuis correlateur
	}

	
	
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.crip.unpublishPort();
			
		} catch (Exception e) {
			throw new ComponentShutdownException(e);		
		}
		super.shutdown();
	}

	public void addEvent(String emitterURI, EventI event) {
		
		this.eventEmitter.put(event, emitterURI);
}


	


}
