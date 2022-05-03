package gevite.correlateur;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import gevite.cep.ActionExecutionCI;
import gevite.cep.EventEmissionCI;
import gevite.cepbus.CEPBus;
import gevite.connector.ConnectorCorrelateurCepServices;
import gevite.evenement.EventBase;
import gevite.evenement.EventI;
import gevite.plugin.PluginActionExecuteOut;
import gevite.plugin.PluginEmissionOut;
import gevite.rule.RuleBase;

public abstract class AbstractCorrelateur extends AbstractComponent {
	
	//Outbound port pour envoyer event au bus 
	protected EventEmissionCI cscop;
	//Outbound port pour delancer l'action de l'executor
	protected ActionExecutionCI caeop;
	
	protected CorrelateurRecieveEventInboundPort cercip;
	
	protected CorrelateurCepServicesOutboundPort ccrop;
	protected EventBase baseEvent;
	protected RuleBase baseRule;
	protected String correlateurId;
	protected String  executor;
	protected String sendEventInboundPort;
	//l'ensemble des émetteurs auxquels le correlateur est abonné
	protected ArrayList<String>emitters;
	
	protected LinkedBlockingQueue<EventI>bufferEvents;
	
	//pool de thread pour recevoir des evenement depuis le bus  
	protected ThreadPoolExecutor recieveExecutor;
	//pool de thread pour ajouter les evenements recu au base et declencher les regles
	protected ThreadPoolExecutor actionExecutor;
	
	
	
	protected AbstractCorrelateur(String correlateurId,String executor,ArrayList<String>emitters,RuleBase ruleBase) throws Exception {
		super(2, 0);
		baseEvent =new EventBase();
		this.cercip= new CorrelateurRecieveEventInboundPort(this);
		this.ccrop=new CorrelateurCepServicesOutboundPort(this);
		this.correlateurId= correlateurId;
		this.executor=executor;
		this.emitters=emitters;
		this.baseRule=ruleBase;
		this.ccrop.publishPort();
		this.cercip.publishPort();
		recieveExecutor=new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		recieveExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		actionExecutor=new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		actionExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		bufferEvents=new LinkedBlockingQueue<EventI>();
	}
	
	@Override
	public synchronized void start()throws ComponentStartException{
		try {
			super.start();
			this.doPortConnection(this.ccrop.getPortURI(), CEPBus.CSIP_URI,ConnectorCorrelateurCepServices.class.getCanonicalName() );
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		sendEventInboundPort= this.ccrop.registerCorrelator(correlateurId, this.cercip.getPortURI());
		PluginEmissionOut pluginSendOut = new PluginEmissionOut();
		pluginSendOut.setInboundPortUri(sendEventInboundPort);
		pluginSendOut.setPluginURI("CorrelateurEmissionPluginOut_"+correlateurId);
		this.installPlugin(pluginSendOut);
		
		cscop = pluginSendOut.getEmissionService();
		
		PluginActionExecuteOut pluginExecuteOut = new PluginActionExecuteOut();
		String ActionExecutionInboundPort=this.ccrop.getExecutorInboundPortURI(executor);
		pluginExecuteOut.setInboundPortUri(ActionExecutionInboundPort);
		pluginExecuteOut.setPluginURI("CorrelateurActionExecutePluginOut_"+correlateurId);
		this.installPlugin(pluginExecuteOut);
		this.caeop=pluginExecuteOut.getActionEecutionService();
		
		for(String emitter: emitters) {
			this.ccrop.subscribe(correlateurId, emitter);
		}
	}
	

	@Override
	public synchronized void finalise() throws Exception {		
		this.doPortDisconnection(this.ccrop.getPortURI());
		super.finalise();
	}
	
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
			
			try {
				this.ccrop.unpublishPort();
				this.cercip.unpublishPort();
				
			} catch (Exception e) {
				throw new ComponentShutdownException(e) ;
			}
			
			
			super.shutdown();
	}
	
	public void addEvent(String emitterURI, EventI event) throws Exception {

		Runnable RecieveTask=()->{
			bufferEvents.add(event);
			System.out.println("correlateur"+correlateurId+" receive event from "+emitterURI);
		};	
		
		recieveExecutor.submit(RecieveTask);
		
		Runnable ActionTask=()->{
			try {
				EventI e = bufferEvents.take();
				this.baseEvent.addEvent(e);
				this.baseRule.fireFirstOn(baseEvent, (CorrelatorStateI) this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		};	
		
		actionExecutor.submit(ActionTask);
	}
	
	public String getExecutorId() throws Exception {
		return this.executor;
	}
	
	
	
	
	
	
	
	
	

}
