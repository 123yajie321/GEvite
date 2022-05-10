package gevite.cepbus;


import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.smartcity.connections.SAMUActionConnector;
import fr.sorbonne_u.utils.Pair;
import gevite.cep.CEPBusManagementCI;
import gevite.cep.EventEmissionCI;
import gevite.cep.EventEmissionImplementationCI;
import gevite.cep.EventReceptionCI;
import gevite.connector.ConnectorCepSendCorrelateur;
import gevite.connector.ConnectorEmitterRegister;
import gevite.connector.ConnectorEmitterSend;
import gevite.connector.ConnectorExcuteurRegister;
import gevite.evenement.EventI;
import gevite.plugin.PluginEmissionIn;
import javassist.expr.NewArray;

@OfferedInterfaces(offered = {CEPBusManagementCI.class,EventEmissionCI.class})
@RequiredInterfaces(required = {EventReceptionCI.class})

public class CEPBus extends AbstractComponent implements EventEmissionImplementationCI{
	
	//public static final String CSIP_URI = "csip-uri";		//register
	//public static final String CERIP_URI = "cerip-uri";	//event recieve from emitter or correlateur
	
	//public static final String CESCOP_URI = "cescop-uri";	//event send
	private static int pluginId=0;

	protected String BusId;
	protected String nextBusId;
   
	
	protected String managementInboundPortUri;
    protected String receiveEventInboundPortUri;
    protected String receiveEventOtherBusInboundPortUri;
   
    //stoker les event recu et leur emitter
    protected LinkedBlockingQueue<Pair<EventI,String>> eventsRecu;

	//protected HashSet<String> uriEmitters;
	protected LinkedBlockingQueue<String > uriEmitters;
    protected ConcurrentHashMap<String, CepEventSendCorrelateurOutboundPort > uriCorrelateurs;
	protected ConcurrentHashMap<String,String> uriExecuteurs;
                               
	//cle:uriEmitters  value:liste des correlateurs qui abonne cette Emitter
	protected ConcurrentHashMap<String,Vector<String>> uriSubscription;
	protected ThreadPoolExecutor sendExecutor;
	protected ThreadPoolExecutor registerCorrelateurExecutor;
    


	
	protected CepManagementInboundPort csip;
	//protected CepEventRecieveInboundPort cerip;
	//protected CepEventSendCorrelateurOutboundPort cescop;


	protected CEPBus(String  busId,String nextBusID,String managementInboudPortUri, String receiveEventInboundPortUri ,String receiveEventOtherBusInboundPortUri)throws Exception {
		super(2, 0);
		this.BusId=busId;
		this.managementInboundPortUri=managementInboudPortUri;
		this.receiveEventInboundPortUri=receiveEventInboundPortUri;
		this.nextBusId=nextBusID;
		this.receiveEventOtherBusInboundPortUri=receiveEventOtherBusInboundPortUri;
		
		//uriEmitters = new HashSet<String>();
		uriEmitters=new LinkedBlockingQueue<String>();
		uriCorrelateurs = new ConcurrentHashMap<String, CepEventSendCorrelateurOutboundPort >();
		uriSubscription = new ConcurrentHashMap<String,Vector<String>>();
		eventsRecu=new LinkedBlockingQueue<Pair<EventI,String>>();
		int  N=3;
		sendExecutor=new ThreadPoolExecutor(N, N, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(20));
		sendExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		
		registerCorrelateurExecutor=new ThreadPoolExecutor(N, N, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		registerCorrelateurExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		
		this.uriExecuteurs=new ConcurrentHashMap<String,String>();
		this.csip = new CepManagementInboundPort(managementInboudPortUri,this); 
		//this.cerip = new CepEventRecieveInboundPort(CERIP_URI,this);
		//this.cescop = new CepEventSendCorrelateurOutboundPort(CESCOP_URI, this);
		this.csip.publishPort();
		//this.cerip.publishPort();
		//this.cescop.publishPort();
		PluginEmissionIn pluginEmissionIn=new PluginEmissionIn(receiveEventInboundPortUri);
		pluginEmissionIn.setPluginURI("pluginEmission_in"+pluginId);
		pluginId++;
		this.installPlugin(pluginEmissionIn);
		

	}
	
	@Override
	public synchronized void	start() throws ComponentStartException
	{
		super.start();
	}
	
	
	
	
	public String registerEmitter(String uri)throws Exception {
		System.out.println("Emetteur : "+ uri +" registed");
		uriEmitters.add(uri);
		return this.receiveEventInboundPortUri;
	
	}
	
	public String registerCorrelator(String uri, String inboundPortURI) throws Exception{
			CepEventSendCorrelateurOutboundPort cescop;
			cescop = new CepEventSendCorrelateurOutboundPort(this);
			cescop.publishPort();
			this.doPortConnection(cescop.getPortURI(), inboundPortURI, ConnectorCepSendCorrelateur.class.getCanonicalName());
			uriCorrelateurs.put(uri,cescop);
			System.out.println(" Correlateur: " + uri+ "  register");
		return this.receiveEventInboundPortUri;// le port pour recevoir le event depuis correlateur
	}
	
	public void registerExecuteur(String uri, String inboundPortURI)throws Exception {
		System.out.println("Executeur : "+ uri +" registed");
		uriExecuteurs.put(uri,inboundPortURI);
		
	}

	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		//envoyer les evenemet recu aux correlateurs
		/*while(true) {
			Runnable SendTask=()->{
				Pair<EventI, String> pair;
				try {
						pair = eventsRecu.poll();
						Iterator<Entry<String, ArrayList<String>>> iterator=uriSubscription.entrySet().iterator();
						while (iterator.hasNext()) {
							Map.Entry<String,ArrayList<String>> entry = (Map.Entry<String,ArrayList<String>> )iterator.next();
							ArrayList<String> emitters= entry.getValue();
								if(emitters.contains(pair.getSecond())) {
									
									String uri_correlateur= entry.getKey();
									CepEventSendCorrelateurOutboundPort cepscop=uriCorrelateurs.get(uri_correlateur);
									//this.doPortConnection(CESCOP_URI, receiveEventInboundPort, ConnectorCepSendCorrelateur.class.getCanonicalName());
									cepscop.receiveEvent(pair.getSecond(), pair.getFirst());
									System.out.println("BUS send  "+ uri_correlateur+" a event");
								}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			};
			sendExecutor.submit(SendTask);	
		}*/
		
	}
	
	
	
	
/*	public void recieveEvent(String emitterURI, EventI event) throws Exception {
		
		
		Pair<EventI, String> newpair=new Pair<EventI, String>(event, emitterURI);
		
		this.eventsRecu.add(newpair);
		System.out.println("after add" );
		Runnable SendTask=()->{
			Pair<EventI, String> pair;
			try {  System.out.println("Pool thread strat");
					pair = eventsRecu.take();
					Iterator<Entry<String, Vector<String>>> iterator=uriSubscription.entrySet().iterator();
					while (iterator.hasNext()) {
						Map.Entry<String,Vector<String>> entry = (Map.Entry<String,Vector<String>> )iterator.next();
						Vector<String> emitters= entry.getValue();
							if(emitters.contains(pair.getSecond())) {
								
								String uri_correlateur= entry.getKey();
								CepEventSendCorrelateurOutboundPort cepscop=uriCorrelateurs.get(uri_correlateur);
								//this.doPortConnection(CESCOP_URI, receiveEventInboundPort, ConnectorCepSendCorrelateur.class.getCanonicalName());
								cepscop.receiveEvent(pair.getSecond(), pair.getFirst());
								System.out.println("BUS send  "+ uri_correlateur+" a event");
							}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		};
		sendExecutor.submit(SendTask);	
}*/


	public boolean subscribe(String subscriberURI, String emitterURI) throws Exception {
			
			/*Vector<String> emitters=uriSubscription.get(subscriberURI);
			if(emitters==null) {
				emitters=new Vector<String>();
				uriSubscription.put(subscriberURI, emitters);
			}*/
			
			uriSubscription.putIfAbsent(emitterURI,new Vector<String>());
			uriSubscription.get(emitterURI).add(subscriberURI);
			System.out.println(emitterURI+ " subscribe par : "+subscriberURI);
			return true;
			
		
		
	}
	
	
	public boolean unsubscribe(String subscriberURI, String emitterURI)throws Exception{
		
		uriSubscription.get(emitterURI).remove(subscriberURI);
		
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
		
		uriSubscription.remove(uri);
		Iterator<Entry<String, Vector<String>>> iterator=uriSubscription.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String,Vector<String>> entry = (Map.Entry<String,Vector<String>> )iterator.next();
				Vector<String> subscribers= entry.getValue();
				subscribers.remove(uri);	
		}
		
		uriCorrelateurs.remove(uri);
		 
		
		return null;
	}
	
	public String unregisterExecutor(String uri)throws Exception {
		
		 uriExecuteurs.remove(uri);
		
		return null;
	}
	
	@Override
	public synchronized void finalise() throws Exception {		
		//this.doPortDisconnection(this.cescop.getPortURI());
		Iterator<Entry<String,CepEventSendCorrelateurOutboundPort >> iterator=uriCorrelateurs.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String,CepEventSendCorrelateurOutboundPort> entry = (Map.Entry<String,CepEventSendCorrelateurOutboundPort> )iterator.next();
			CepEventSendCorrelateurOutboundPort outPort= entry.getValue();
			outPort.doDisconnection();
		}
		super.finalise();
	}
	
	
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
			
			try {
				//this.cerip.unpublishPort();
				//this.cescop.unpublishPort();
				this.csip.unpublishPort();
				
				Iterator<Entry<String,CepEventSendCorrelateurOutboundPort >> iterator=uriCorrelateurs.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry<String,CepEventSendCorrelateurOutboundPort> entry = (Map.Entry<String,CepEventSendCorrelateurOutboundPort> )iterator.next();
					CepEventSendCorrelateurOutboundPort outPort= entry.getValue();
					outPort.unpublishPort();
				}
				sendExecutor.shutdown();
				registerCorrelateurExecutor.shutdown();
				
			} catch (Exception e) {
				throw new ComponentShutdownException(e) ;
			}
			
			
			super.shutdown();
		}

	@Override
	public void sendEvent(String emitterURI, EventI event) throws Exception {
		System.out.println("Bus reveive Event from : " + emitterURI);
		this.eventsRecu.add(new Pair<EventI, String>(event, emitterURI));
	
		Runnable SendTask=()->{
			Pair<EventI, String> pair;
			try { 
					pair = eventsRecu.take();
					Vector<String> subscribers= uriSubscription.get(pair.getSecond());
					for(String subscriber:subscribers) {
						CepEventSendCorrelateurOutboundPort cepscop=uriCorrelateurs.get(subscriber);
						cepscop.receiveEvent(pair.getSecond(), pair.getFirst());
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			
		};
		sendExecutor.submit(SendTask);
		
	}

	@Override
	public void sendEvents(String emitterURI, EventI[] events) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void receiveEventOtherBus(String emitterURI, EventI event,String busId) throws Exception {
		
		if(busId==this.BusId) {
			return; 
		}
		else {
			sendEvent(emitterURI, event);
		}
	}


}
