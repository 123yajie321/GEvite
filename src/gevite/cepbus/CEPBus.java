package gevite.cepbus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

public class CEPBus extends AbstractComponent implements EventEmissionCI{
	
	public static final String CSIP_URI = "csip-uri";		//register
	public static final String CERIP_URI = "cerip-uri";	//event recieve form emitter or correlateur
	
	public static final String CESCOP_URI = "cescop-uri";	//event send
	private static int pluginId=0;

	
  
    //stoker les event recu et leur emitter
    protected LinkedBlockingQueue<Pair<EventI,String>> eventsRecu;

	//protected HashSet<String> uriEmitters;
	protected LinkedBlockingQueue<String > uriEmitters;
    protected ConcurrentHashMap<String, CepEventSendCorrelateurOutboundPort > uriCorrelateurs;
	protected ConcurrentHashMap<String,String> uriExecuteurs;

	protected ConcurrentHashMap<String,Vector<String>> uriSubscription;
	protected ThreadPoolExecutor sendExecutor;
	protected ThreadPoolExecutor registerCorrelateurExecutor;



	
	protected CepServicesInboundPort csip;
	//protected CepEventRecieveInboundPort cerip;
	//protected CepEventSendCorrelateurOutboundPort cescop;


	protected CEPBus()throws Exception {
		super(2, 0);
		
		//uriEmitters = new HashSet<String>();
		uriEmitters=new LinkedBlockingQueue<String>();
		uriCorrelateurs = new ConcurrentHashMap<String, CepEventSendCorrelateurOutboundPort >();
		uriSubscription = new ConcurrentHashMap<String,Vector<String>>();
		eventsRecu=new LinkedBlockingQueue<Pair<EventI,String>>();
		int  N=3;
		sendExecutor=new ThreadPoolExecutor(N, N, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(20));
		sendExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		
		registerCorrelateurExecutor=new ThreadPoolExecutor(N, N, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(20));
		registerCorrelateurExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		
		this.uriExecuteurs=new ConcurrentHashMap<String,String>();
		this.csip = new CepServicesInboundPort(CSIP_URI,this); 
		//this.cerip = new CepEventRecieveInboundPort(CERIP_URI,this);
		//this.cescop = new CepEventSendCorrelateurOutboundPort(CESCOP_URI, this);
		this.csip.publishPort();
		//this.cerip.publishPort();
		//this.cescop.publishPort();
		PluginEmissionIn pluginEmissionIn=new PluginEmissionIn(CERIP_URI);
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
		return this.CERIP_URI;
	
	}
	
	public String registerCorrelator(String uri, String inboundPortURI) throws Exception{
		Runnable RegisterTask=()->{
			CepEventSendCorrelateurOutboundPort cescop;
			try {
				cescop = new CepEventSendCorrelateurOutboundPort(this);
				cescop.publishPort();
				this.doPortConnection(cescop.getPortURI(), inboundPortURI, ConnectorCepSendCorrelateur.class.getCanonicalName());
				uriCorrelateurs.put(uri,cescop);
				System.out.println(" Correlateur: " + uri+ "  register");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		};
		
		registerCorrelateurExecutor.submit(RegisterTask);
		
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
			
			uriSubscription.putIfAbsent(subscriberURI,new Vector<String>());
			uriSubscription.get(subscriberURI).add(emitterURI);
			System.out.println(subscriberURI+ " subscribe : "+emitterURI);
			return true;
			
		
		
	}
	
	
	public boolean unsubscribe(String subscriberURI, String emitterURI)throws Exception{
		
		uriSubscription.get(subscriberURI).remove(emitterURI);
		
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
					Iterator<Entry<String, Vector<String>>> iterator=uriSubscription.entrySet().iterator();
					while (iterator.hasNext()) {
						Map.Entry<String,Vector<String>> entry = (Map.Entry<String,Vector<String>> )iterator.next();
						Vector<String> emitters= entry.getValue();
							if(emitters.contains(pair.getSecond())) {
								
								String uri_correlateur= entry.getKey();
								CepEventSendCorrelateurOutboundPort cepscop=uriCorrelateurs.get(uri_correlateur);
								//this.doPortConnection(CESCOP_URI, receiveEventInboundPort, ConnectorCepSendCorrelateur.class.getCanonicalName());
								cepscop.receiveEvent(pair.getSecond(), pair.getFirst());
								//System.out.println("BUS send  "+ uri_correlateur+" a event");
							}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		};
		sendExecutor.submit(SendTask);
		
	}

	@Override
	public void sendEvents(String emitterURI, EventI[] events) throws Exception {
		// TODO Auto-generated method stub
		
	}


}
