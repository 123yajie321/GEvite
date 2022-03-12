package gevite.cepbus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.utils.Pair;
import gevite.cep.CEPBusManagementCI;
import gevite.cep.EventEmissionCI;
import gevite.cep.EventReceptionCI;
import gevite.connector.ConnectorCepSendCorrelateur;
import gevite.connector.ConnectorEmitterSend;
import gevite.evenement.EventI;

@OfferedInterfaces(offered = {CEPBusManagementCI.class,EventEmissionCI.class})
@RequiredInterfaces(required = {EventReceptionCI.class})

public class CEPBus extends AbstractComponent{
	
	public static final String CSIP_URI = "csip-uri";		//register
	public static final String CERIP_URI = "cerip-uri";	//event recieve form emitter or correlateur
	
	public static final String CESCOP_URI = "cescop-uri";	//event send

	
  
    //stoker les event recu et leur emitter
    protected LinkedBlockingQueue<Pair<EventI,String>> eventsRecu;

	protected HashSet<String> uriEmitters;
	protected HashMap<String,String> uriCorrelateurs;
	protected HashMap<String,String> uriExecuteurs;

	protected HashMap<String,ArrayList<String>> uriSubscription;


	
	protected CepServicesInboundPort csip;
	protected CepEventRecieveInboundPort cerip;
	protected CepEventSendCorrelateurOutboundPort cescop;


	protected CEPBus()throws Exception {
		super(2, 0);
		
		uriEmitters = new HashSet<String>();
		uriCorrelateurs = new HashMap<String,String>();
		uriSubscription = new HashMap<String,ArrayList<String>>();
		eventsRecu=new LinkedBlockingQueue<Pair<EventI,String>>();
		this.uriExecuteurs=new HashMap<String,String>();
		this.csip = new CepServicesInboundPort(CSIP_URI,this); 
		this.cerip = new CepEventRecieveInboundPort(CERIP_URI,this);
		this.cescop = new CepEventSendCorrelateurOutboundPort(CESCOP_URI, this);
		this.csip.publishPort();
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
		System.out.println(" Correlateur: " + uri+ "  register");
		return this.CERIP_URI;// le port pour recevoir le event depuis correlateur
	}
	
	public String registerExecuteur(String uri, String inboundPortURI)throws Exception {
		System.out.println("Executeur : "+ uri +" registed");
		uriExecuteurs.put(uri,inboundPortURI);
		return null;
	}

	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		//envoyer les evenemet recu aux correlateurs
		while(true) {
			
			Pair<EventI, String> pair=eventsRecu.take();
			
			Iterator<Entry<String, ArrayList<String>>> iterator=uriSubscription.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String,ArrayList<String>> entry = (Map.Entry<String,ArrayList<String>> )iterator.next();
				ArrayList<String> emitters= entry.getValue();
					if(emitters.contains(pair.getSecond())) {
						
						String uri_correlateur= entry.getKey();
						String receiveEventInboundPort=uriCorrelateurs.get(uri_correlateur);
						this.doPortConnection(CESCOP_URI, receiveEventInboundPort, ConnectorCepSendCorrelateur.class.getCanonicalName());
						this.cescop.receiveEvent(pair.getSecond(), pair.getFirst());
						System.out.println("BUS send  "+ uri_correlateur+" a event");
						
						
						
					}
			}
		
		}
		
	}
	
	
	
	
	public void recieveEvent(String emitterURI, EventI event) throws Exception {
		System.out.println("Bus reveive Event from : " + emitterURI);
		this.eventsRecu.put(new Pair<EventI, String>(event, emitterURI));
		
		
}


	public boolean subscribe(String subscriberURI, String emitterURI) throws Exception {
		
		ArrayList<String> emitters=uriSubscription.get(subscriberURI);
		if(emitters==null) {
			emitters=new ArrayList<String>();
			uriSubscription.put(subscriberURI, emitters);
		}
		uriSubscription.get(subscriberURI).add(emitterURI);
		System.out.println(subscriberURI+ " subscribe : "+emitterURI);
		return true;
	}
	
	public String getExecutorInboundPortURI(String uri)throws Exception{
	
		String uriExecutoInboudPort =uriExecuteurs.get(uri);	
		System.out.println(uri+" geturiExecutoInboudPort: " +uriExecutoInboudPort);
		 return uriExecutoInboudPort;
		
	}
	
	public String unregisterEmitter(String uri) throws Exception{
		 uriEmitters.remove(uri);
		
		
		return null;
	}
	
	public String unregisterCorrelator(String uri)throws Exception {
		
		 uriCorrelateurs.remove(uri);
		 uriSubscription.remove(uri);
		
		return null;
	}
	
	public String unregisterExecutor(String uri)throws Exception {
		
		 uriExecuteurs.remove(uri);
		
		return null;
	}
	
	@Override
	public synchronized void finalise() throws Exception {		
		
		this.doPortDisconnection(this.cescop.getPortURI());
		
		super.finalise();
	}
	
	
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
			
			try {
				this.cerip.unpublishPort();
				this.cescop.unpublishPort();
				this.csip.unpublishPort();
				
			} catch (Exception e) {
				throw new ComponentShutdownException(e) ;
			}
			
			
			super.shutdown();
		}


}
