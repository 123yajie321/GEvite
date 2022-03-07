package gevite.cepbus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import gevite.cep.CEPBusManagementCI;
import gevite.cep.EventReceptionCI;
import gevite.connector.ConnectorCepSendCorrelateur;
import gevite.connector.ConnectorEmitterSend;
import gevite.evenement.EventI;

@OfferedInterfaces(offered = {CEPBusManagementCI.class})
@RequiredInterfaces(required = {EventReceptionCI.class})

public class CEPBus extends AbstractComponent{
	
	public static final String CRIP_URI = "crip-uri";		//register
	public static final String CERIP_URI = "cerip-uri";	//event recieve 
	
	public static final String CESCOP_URI = "cescop-uri";	//event send

	
    protected HashMap<EventI, String>eventRecu;

	protected HashSet<String> uriEmitters;
	protected HashMap<String,String> uriCorrelateurs;
	protected HashMap<String,String> uriExecuteurs;

	protected HashMap<String,ArrayList<String>> uriSubscription;
	//protected ArrayList<Pair<String,String>> uriSubscription;

	
	protected CepRegisterInboundPort crip;
	protected CepEventRecieveInboundPort cerip;
	protected CepEventSendCorrelateurOutboundPort cescop;


	protected CEPBus()throws Exception {
		super(1, 0);
		
		uriEmitters = new HashSet<String>();
		uriCorrelateurs = new HashMap<String,String>();
		//uriSubscription = new ArrayList<Pair<String, String>>();
		uriSubscription = new HashMap<String,ArrayList<String>>();
		eventRecu=new HashMap<EventI,String>();
		this.uriExecuteurs=new HashMap<String,String>();
		this.crip = new CepRegisterInboundPort(CRIP_URI,this); 
		this.cerip = new CepEventRecieveInboundPort(CERIP_URI,this);
		this.cescop = new CepEventSendCorrelateurOutboundPort(CESCOP_URI, this);
		this.crip.publishPort();
		this.cerip.publishPort();
		this.cescop.publishPort();

	}
	
	
	
	
	
	
	public String registerEmitter(String uri)throws Exception {
		System.out.println("Emetteur : "+ uri +" registed");
		uriEmitters.add(uri);
		return this.CERIP_URI;
	}
	
	public String registerCorrelator(String uri, String inboundPortURI) throws Exception{
		uriCorrelateurs.put(uri,inboundPortURI);
		System.out.println(" RegisterCorrelateur: " + uri);
		return " port recevoir";// le port pour recevoir le event depuis correlateur
	}
	
	public String registerExecuteur(String uri, String inboundPortURI)throws Exception {
		System.out.println("Executeur : "+ uri +" registed");
		uriExecuteurs.put(uri,inboundPortURI);
		return null;
	}

	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		
		
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

	public void recieveEvent(String emitterURI, EventI event) throws Exception {
		
		this.eventRecu.put(event, emitterURI);
		Iterator<Entry<String, ArrayList<String>>> iterator=uriSubscription.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String,ArrayList<String>> entry = (Map.Entry<String,ArrayList<String>> )iterator.next();
			ArrayList<String> emitters= entry.getValue();
				if(emitters.contains(emitterURI)) {
					
					String uri_correlateur= entry.getKey();
					String receiveEventInboundPort=uriCorrelateurs.get(uri_correlateur);
					this.doPortConnection(CESCOP_URI, receiveEventInboundPort, ConnectorCepSendCorrelateur.class.getCanonicalName());
					this.cescop.receiveEvent(emitterURI, event);
					
					
					
				}
		}
	
		
		
		
		
}


	public boolean subscribe(String subscriberURI, String emitterURI) throws Exception {
		
		ArrayList<String> emitters=uriSubscription.get(subscriberURI);
		if(emitters==null) {
			emitters=new ArrayList<String>();
			uriSubscription.put(subscriberURI, emitters);
		}
		uriSubscription.get(subscriberURI).add(emitterURI);
		return true;
	}


}
