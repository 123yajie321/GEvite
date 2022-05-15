package gevite.correlateur;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import gevite.CEPBus;
import gevite.connector.ConnectorCepManagement;
import gevite.evenement.EventBase;
import gevite.evenement.EventI;
import gevite.interfaces.ActionExecutionCI;
import gevite.interfaces.CorrelatorStateI;
import gevite.interfaces.EventEmissionCI;
import gevite.plugin.PluginActionExecuteOut;
import gevite.plugin.PluginEmissionOut;
import gevite.port.CepManagementOutboundPort;
import gevite.port.CorrelateurRecieveEventInboundPort;
import gevite.rule.RuleBase;

/**
 * The class <code>AbstractCorrelateur</code> extends 
 * AbstractComponent,represents the basic information and methods for correlators    
 * @author Yajie LIU, Zimeng ZHANG
 */

public abstract class AbstractCorrelateur extends AbstractComponent {
	
	/** Outbound port pour envoyer event au bus */
	protected EventEmissionCI cscop;
	/** Outbound port pour delancer l'action de l'executor */
	protected ActionExecutionCI caeop;
	/** Inbound port pour recevoir Evnet depuis CepBus  */
	protected CorrelateurRecieveEventInboundPort cercip;
	/** Outbound port demande managenement de CepBus */
	protected CepManagementOutboundPort ccrop;
	/**Base de event de correlateur*/
	protected EventBase baseEvent;
	/**Base de regle de correlateur*/
	protected RuleBase baseRule;
	/**Identifiant de correlateur*/
	protected String correlateurId;
	/**l'executeur que le correlateur connecte*/
	protected String  executor;
	/**Le URI de Inbound Port de destination,ici c'est le bus */
	protected String sendEventInboundPort;
	/**l'ensemble des émetteurs que le correlateur  abonne */
	protected ArrayList<String>emitters;
	/**le tampon qui stocke Event que le correlateur recu et n'a pas encore ajoute au event base */
	protected LinkedBlockingQueue<EventI>bufferEvents;
	/**pool de thread pour recevoir des evenement depuis le bus  */
	protected ThreadPoolExecutor recieveExecutor;
	/**pool de thread pour ajouter les evenements recu au base et declencher les regles */
	protected ThreadPoolExecutor actionExecutor;
	/**uri of inbound port de CEPBus pour le  management*/
	protected String busManagementInboundPortUri;
	
	
	protected AbstractCorrelateur(String correlateurId,String executor,ArrayList<String>emitters,RuleBase ruleBase,String busManagementInboundPortUri) throws Exception {
		super(2, 0);
		this.busManagementInboundPortUri=busManagementInboundPortUri;
		baseEvent =new EventBase();
		this.cercip= new CorrelateurRecieveEventInboundPort(this);
		this.ccrop=new CepManagementOutboundPort(this);
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
			this.doPortConnection(this.ccrop.getPortURI(), busManagementInboundPortUri,ConnectorCepManagement.class.getCanonicalName() );
			
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
	
	/**
	 * recevoir les evenements ,les mettre dans le tampon,
	 * submit le tache au pool thread pour declencher les actions
	 */
	
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
	
	
	/**
	 * premettre de recuprer l'excuteur que le correlateur connete avec
	 * @return String
	 */
	
	public String getExecutorId() throws Exception {
		return this.executor;
	}
	
	
	
	
	
	
	
	
	

}
