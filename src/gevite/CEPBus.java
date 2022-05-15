package gevite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.utils.Pair;
import gevite.connector.ConnectorBusEmission;
import gevite.connector.ConnectorEventReception;
import gevite.evenement.EventI;
import gevite.interfaces.BusEventEmissionCI;
import gevite.interfaces.CEPBusManagementCI;
import gevite.interfaces.EventEmissionImplementationI;
import gevite.interfaces.EventReceptionCI;
import gevite.plugin.PluginEmissionIn;
import gevite.port.BusEventEmissionInboundPort;
import gevite.port.BusSendEventOutboundPort;
import gevite.port.CepEventSendCorrelateurOutboundPort;
import gevite.port.CepManagementInboundPort;

/**
 * The class <code>CEPBus</code> implements a component that can
 * receive events from emitters or correlators ,and send them to the correspending correlator  
 * <p>
 *  Data transfer between BUSs of different JVMs has been added in the current version,
 *  but because there are still some problems with DistributedCVM, 
 *  the code for these functions has been put in comments
 * </p>
 * 
 *  <p>
 * The component implements l'interface  {@code EventEmissionImplementationI} to receive Event from correlators and emitters
 * </p>
 *    
 *    
 * @author Yajie LIU, Zimeng ZHANG
 */

@OfferedInterfaces(offered = {CEPBusManagementCI.class})
@RequiredInterfaces(required = {EventReceptionCI.class})

public class CEPBus extends AbstractComponent implements EventEmissionImplementationI{
	
	
	/**identifier of Bus */
	protected String BusId;
	/**uri of inbound port to receive events*/
    protected String receiveEventInboundPortUri;
    /**uri of inbound port of other bus for receiving evnts,
     *  which should be connected with the bsop of this bus*/
    protected String OtherBusReceiveEventInboundPortUri;
    /**stoker les event recu et leur emitter*/
    protected LinkedBlockingQueue<Pair<EventI,String>> eventsRecu;
    /**stoker le uri des emitters*/
	protected LinkedBlockingQueue<String > uriEmitters;
	 /**stoker le uri des correlateur et le outbound port qui premet de
	  * envoyer des evenement  */
    protected ConcurrentHashMap<String, CepEventSendCorrelateurOutboundPort > uriCorrelateurs;
    /**stoker les event recu et leur emitterUri*/
    protected ConcurrentHashMap<String,String> uriExecuteurs;
    /**stoker les donnee des ahonnement 
     * cle:uriEmitters  value:liste des correlateurs qui abonne ce Emitter
     */                        
	protected ConcurrentHashMap<String,Vector<String>> uriSubscription;
	 /**Pool de thread pour envoyer des events*/
	protected ThreadPoolExecutor sendExecutor;
	/**inbound port of CEPBus for management*/
	protected CepManagementInboundPort csip;
	/**outbound Port to send events to other bus*/
	protected BusSendEventOutboundPort bsop;
	/**Inboudn Port to receive events from other bus*/
	protected BusEventEmissionInboundPort beeip;


	protected CEPBus(String busId,String managementInboudPortUri, String receiveEventInboundPortUri /*,String receiveEventOtherBusInboundPortUri,String OtherBusReceiveEventInboundPortUri*/)throws Exception {
		super(2, 0);
		this.BusId=busId;
		this.receiveEventInboundPortUri=receiveEventInboundPortUri;
		uriEmitters=new LinkedBlockingQueue<String>();
		uriCorrelateurs = new ConcurrentHashMap<String, CepEventSendCorrelateurOutboundPort >();
		uriSubscription = new ConcurrentHashMap<String,Vector<String>>();
		eventsRecu=new LinkedBlockingQueue<Pair<EventI,String>>();
		int  N=3;
		sendExecutor=new ThreadPoolExecutor(N, N, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(20));
		sendExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		this.uriExecuteurs=new ConcurrentHashMap<String,String>();
		this.csip = new CepManagementInboundPort(managementInboudPortUri,this); 
		this.csip.publishPort();
		PluginEmissionIn pluginEmissionIn=new PluginEmissionIn(receiveEventInboundPortUri);
		pluginEmissionIn.setPluginURI("pluginEmission_in"+AbstractPort.generatePortURI());
		this.installPlugin(pluginEmissionIn);
		
		/* 
		 * this.OtherBusReceiveEventInboundPortUri=OtherBusReceiveEventInboundPortUri;
			this.bsop=new BusSendEventOutboundPort(this);
			this.bsop.publishPort();
			this.beeip=new BusEventEmissionInboundPort(this);
			this.beeip.publishPort();		
		 */
	}
	
	@Override
	public synchronized void	start() throws ComponentStartException
	{
		super.start();
		try {
			//doPortConnection(this.bsop.getPortURI(), OtherBusReceiveEventInboundPortUri, ConnectorBusEmission.class.getCanonicalName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		
		
	}
	
	
	@Override
	public synchronized void finalise() throws Exception {		
		
		Iterator<Entry<String,CepEventSendCorrelateurOutboundPort >> iterator=uriCorrelateurs.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String,CepEventSendCorrelateurOutboundPort> entry = (Map.Entry<String,CepEventSendCorrelateurOutboundPort> )iterator.next();
			CepEventSendCorrelateurOutboundPort outPort= entry.getValue();
			outPort.doDisconnection();
		}
		/*
		doPortDisconnection(this.bsop.getPortURI());
		this.bsop.unpublishPort();*/
		super.finalise();
	}
	
	
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
			
			try {
				this.csip.unpublishPort();
				
				Iterator<Entry<String,CepEventSendCorrelateurOutboundPort >> iterator=uriCorrelateurs.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry<String,CepEventSendCorrelateurOutboundPort> entry = (Map.Entry<String,CepEventSendCorrelateurOutboundPort> )iterator.next();
					CepEventSendCorrelateurOutboundPort outPort= entry.getValue();
					outPort.unpublishPort();
				}
				sendExecutor.shutdown();
				//	this.beeip.unpublishPort(); 
				
			} catch (Exception e) {
				throw new ComponentShutdownException(e) ;
			}
			
			
			super.shutdown();
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
			this.doPortConnection(cescop.getPortURI(), inboundPortURI, ConnectorEventReception.class.getCanonicalName());
			uriCorrelateurs.put(uri,cescop);
			System.out.println(" Correlateur: " + uri+ "  register");
		return this.receiveEventInboundPortUri;// le port pour recevoir le event depuis correlateur
	}
	
	public void registerExecuteur(String uri, String inboundPortURI)throws Exception {
		System.out.println("Executeur : "+ uri +" registed");
		uriExecuteurs.put(uri,inboundPortURI);
		
	}


	public void subscribe(String subscriberURI, String emitterURI) throws Exception {
				
			uriSubscription.putIfAbsent(emitterURI,new Vector<String>());
			uriSubscription.get(emitterURI).add(subscriberURI);
			System.out.println(emitterURI+ " subscribe par : "+subscriberURI);
			
	}
	
	
	public void unsubscribe(String subscriberURI, String emitterURI)throws Exception{
		
		uriSubscription.get(emitterURI).remove(subscriberURI);
		
	}
	
	
	public String getExecutorInboundPortURI(String uri)throws Exception{
	
		String uriExecutoInboudPort =uriExecuteurs.get(uri);	
		System.out.println(uri+" geturiExecutoInboudPort: " +uriExecutoInboudPort);
		 return uriExecutoInboudPort;
		
	}
	
	public void unregisterEmitter(String uri) throws Exception{
		 uriSubscription.remove(uri);
		 uriEmitters.remove(uri);
	}
	
	public void unregisterCorrelator(String uri)throws Exception {
		
		uriSubscription.remove(uri);
		Iterator<Entry<String, Vector<String>>> iterator=uriSubscription.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String,Vector<String>> entry = (Map.Entry<String,Vector<String>> )iterator.next();
				Vector<String> subscribers= entry.getValue();
				subscribers.remove(uri);	
		}
		
		uriCorrelateurs.remove(uri);
		 
	
	}
	
	public void unregisterExecutor(String uri)throws Exception {
		
		uriExecuteurs.remove(uri);
		
	}
	
	/**
	 * Send the received event to the correlator who subscribed to it
	 * */
	@Override
	public void sendEvent(String emitterURI, EventI event) throws Exception {
		System.out.println("Bus reveive Event from : " + emitterURI);
		this.eventsRecu.add(new Pair<EventI, String>(event, emitterURI));
		Pair<EventI, String> pair=this.eventsRecu.take();
		submitSendEventTask(pair,BusId);
		
	}

	@Override
	public void sendEvents(String emitterURI, EventI[] events) throws Exception {
		
		
	}
	/**
	 * Send the received event to the next bus, and to the correlator who subscribed to it
	 * @param emitterURI String
	 * @param event EventI
	 * @param busId String
	 * @throws Exception exception
	 */
	public void receiveEventOtherBus(String emitterURI, EventI event,String busId) throws Exception {
		
		if(busId==this.BusId) {
			return; 
		}	
		else {
			Pair<EventI, String> pair=new Pair<EventI, String>(event, emitterURI);
			submitSendEventTask(pair,BusId);
		}
	}
	

	/**
	 * Add a new send task to the thread pool
	 * @param pair Pair of EventI, String
	 * @param busId String
	 * */
	public void submitSendEventTask(Pair<EventI, String> pair,String busId) {
		
		Runnable SendTask=()->{
			
			try { 
					//this.bsop.sendOtherBusEvent(pair.getSecond(), pair.getFirst(), busId);
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
	
	
	


}
