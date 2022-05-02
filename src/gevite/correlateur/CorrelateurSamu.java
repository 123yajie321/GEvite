package gevite.correlateur;

import java.io.Serializable;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.smartcity.SmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.descriptions.AbstractSmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import fr.sorbonne_u.utils.Pair;
import gevite.actions.SamuActions;
import gevite.cep.ActionExecutionCI;
import gevite.cep.CEPBusManagementCI;
import gevite.cep.EventEmissionCI;
import gevite.cep.EventReceptionCI;
import gevite.cepbus.CEPBus;
import gevite.cepbus.CepEventSendCorrelateurOutboundPort;
import gevite.connector.ConnectorCorrelateurCepServices;
import gevite.connector.ConnectorCorrelateurExecutor;
import gevite.connector.ConnectorCorrelateurSAMU;
import gevite.connector.ConnectorCorrelateurSendCep;
import gevite.evenement.EventBase;
import gevite.evenement.EventI;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.atomique.samu.SamuDejaSollicite;
import gevite.evenement.atomique.samu.SignaleManuel;
import gevite.evenement.complexe.samu.ConsciousFall;
import gevite.evenement.complexe.samu.DemandeInterventionSamu;
import gevite.plugin.PluginActionExecuteOut;
import gevite.plugin.PluginEmissionOut;
import gevite.rule.RuleBase;

@OfferedInterfaces(offered = {EventReceptionCI.class})
@RequiredInterfaces(required = {CEPBusManagementCI.class,EventEmissionCI.class,ActionExecutionCI.class})

public class CorrelateurSamu extends AbstractComponent implements SamuCorrelatorStateI{
	
	//public static final String CERCIP_URI = "cercip-uri";
	//public static final String CCROP_URI = "ccrop-uri";
	//public static final String CESCOP_URI = "cescop-uri";
	
	//EmitteurOutboundPort
	protected EventEmissionCI cscop;
	//protected ActionExecutionCI caeop;
	
	//protected ArrayList<ActionExecutionCI> list_caeop;
	
	protected ActionExecutionCI caeop;
	protected CorrelateurRecieveEventInboundPort cercip;
	protected CorrelateurCepServicesOutboundPort ccrop;
	//protected CorrelateurActionExecutionOutboundPort caeop;
	//protected CorrelateurSendCepOutboundPort cscop;
	
	protected EventBase baseEvent;
	//protected HashMap<EventI, String>eventEmitter;
	protected RuleBase baseRule;
	protected String correlateurId;
	//protected ArrayList<String>executors;
	protected String  executor;
	protected String sendEventInboundPort;
	protected ArrayList<String>emitters;
	protected LinkedBlockingQueue<EventI>bufferEvents;
	
	//protected Vector<AtomicBoolean>ambulancesAvailable;
	//protected Vector<AtomicBoolean> medicsAvailable;
	protected AtomicBoolean ambulancesAvailable;
	protected AtomicBoolean medicsAvailable;
	
	protected ThreadPoolExecutor recieveExecutor;
	protected ThreadPoolExecutor actionExecutor;

	
	
	protected CorrelateurSamu(String correlateurId,String executor,ArrayList<String>emitters,RuleBase ruleBase) throws Exception{
		super(2,0);
		baseEvent =new EventBase();
		this.cercip= new CorrelateurRecieveEventInboundPort(this);
		this.ccrop=new CorrelateurCepServicesOutboundPort(this);
		//this.caeop=new CorrelateurActionExecutionOutboundPort(this);
		//this.cscop=new CorrelateurSendCepOutboundPort(this);
		//this.caeop.publishPort();
		this.ccrop.publishPort();
		this.cercip.publishPort();
		//this.cscop.publishPort();
		this.correlateurId= correlateurId;
		this.executor=executor;
		this.emitters=emitters;
		this.baseRule=ruleBase;
		//list_caeop = new ArrayList<ActionExecutionCI>();
		this.ambulancesAvailable=new AtomicBoolean(true);
		this.medicsAvailable=new AtomicBoolean(true);
		//this.ambulancesAvailable=new Vector<AtomicBoolean>();
		//this.medicsAvailable=new Vector<AtomicBoolean>();
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
		//this.doPortConnection(this.cscop.getPortURI(), sendEventInboundPort, ConnectorCorrelateurSendCep.class.getCanonicalName());
		//String ActionExecutionInboundPort=this.ccrop.getExecutorInboundPortURI(executors.get(0));
		//this.doPortConnection(this.caeop.getPortURI(), ActionExecutionInboundPort, ConnectorCorrelateurSAMU.class.getCanonicalName());
	
		PluginEmissionOut pluginSendOut = new PluginEmissionOut();
		pluginSendOut.setInboundPortUri(sendEventInboundPort);
		pluginSendOut.setPluginURI("CorrelateurSamuEmissionPluginOut_"+correlateurId);
		this.installPlugin(pluginSendOut);
		
		cscop = pluginSendOut.getEmissionService();
		
		PluginActionExecuteOut pluginExecuteOut = new PluginActionExecuteOut();
		String ActionExecutionInboundPort=this.ccrop.getExecutorInboundPortURI(executor);
		pluginExecuteOut.setInboundPortUri(ActionExecutionInboundPort);
		pluginExecuteOut.setPluginURI("CorrelateurSamuActionExecutePluginOut_"+correlateurId);
		this.installPlugin(pluginExecuteOut);
		this.caeop=pluginExecuteOut.getActionEecutionService();
		
		
		for(String emitter: emitters) {
			this.ccrop.subscribe(correlateurId, emitter);
		}
		
		
		/*for(int i = 0; i < executors.size(); i++) {
			PluginActionExecuteOut pluginExecuteOut = new PluginActionExecuteOut();
			String ActionExecutionInboundPort=this.ccrop.getExecutorInboundPortURI(executors.get(i));
			pluginExecuteOut.setInboundPortUri(ActionExecutionInboundPort);
			pluginExecuteOut.setPluginuURI("CorrelateurSamuActionExecutePluginOut_"+correlateurId);
			this.installPlugin(pluginExecuteOt);
			
			list_caeop.add(pluginExecuteOut.getActionEecutionService());
		}*/
	}
	
	@Override
	public synchronized void finalise() throws Exception {		
		this.doPortDisconnection(this.ccrop.getPortURI());
		//this.doPortDisconnection(this.caeop.getPortURI());
		//this.doPortDisconnection(this.cscop.getPortURI());
		super.finalise();
	}
	
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
			
			try {
				this.ccrop.unpublishPort();
				//this.caeop.unpublishPort();
				//this.cscop.unpublishPort();
				this.cercip.unpublishPort();
				
			} catch (Exception e) {
				throw new ComponentShutdownException(e) ;
			}
			
			
			super.shutdown();
		}
	
	public void addEvent(String emitterURI, EventI event) throws Exception {

		/*if(event instanceof AlarmeSante ) {System.out.println(" CorrelateurSamu receive alarme sante :"+(event.getPropertyValue("personId") != null ?
  				" form person " + event.getPropertyValue("personId") :	""));}
		
		if(event instanceof SignaleManuel  ) {System.out.println("CorrelateurSamu receive Signal Manuel from "+ event.getPropertyValue("personId"));}*/
		Runnable RecieveTask=()->{
			bufferEvents.add(event);
			System.out.println("correlateur"+correlateurId+" receive event from "+emitterURI);
		};	
		
		recieveExecutor.submit(RecieveTask);
		
		Runnable ActionTask=()->{
			try {
				EventI e = bufferEvents.take();
				this.baseEvent.addEvent(e);
				baseRule.fireFirstOn(baseEvent, this);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};	
		
		actionExecutor.submit(ActionTask);
	}


	
	
	/*S16*/
    public void setAmbulancesNoAvailable() throws Exception {
    	
    	
    	this.ambulancesAvailable.compareAndExchange(true, false);
    }
	
	
	/*S17*/
    public void setMedcinNoAvailable() throws Exception{
    	this.medicsAvailable.compareAndExchange(true, false);
    }
	
	
	/*S18*/
    public void setAmbulancesAvailable() throws Exception{
    	this.ambulancesAvailable.compareAndExchange(false, true);
    }
	
	
	/*S19*/
    public void setMedcinAvailable() throws Exception{
    	this.medicsAvailable.compareAndExchange(false, true);
    	
    }	

	@Override
	public boolean inZone(AbsolutePosition p) {
		/*for(String e:executors) {
			if(	SmartCityDescriptor.dependsUpon(p,this.executor))
						
				return true;
		}
		return false;*/
		return SmartCityDescriptor.dependsUpon(p,this.executor);
		
		
	}


	@Override
	public boolean isAmbulanceAvailable() {
	
		return this.ambulancesAvailable.get();
	}



	@Override
	public void intervanetionAmbulance(AbsolutePosition position,String personId,TypeOfSAMURessources type) throws Exception {
		
		SamuActions	intervention=  SamuActions.InterventionAmbulance;
		String uri=((CorrelateurActionExecutionOutboundPort) caeop).getPortURI();
		System.out.println("the action Port : "+uri);
		this.caeop.execute(intervention, new Serializable[] {position,personId,type}); 
		System.out.println("intervanetionAmbulance finished");
	
	}
	
	@Override
	public void intervanetionMedecin(AbsolutePosition position,String personId,TypeOfSAMURessources type) throws Exception {
		
		SamuActions	intervention=  SamuActions.IntervetionMedcin;
		String uri=((CorrelateurActionExecutionOutboundPort) caeop).getPortURI();
		System.out.println("the action Port : "+uri);
		this.caeop.execute(intervention, new Serializable[] {position,personId,type}); 
		System.out.println("intervanetionMedecin finished");
	
	}


	@Override
	public void triggerMedicCall(AbsolutePosition position, String personId, TypeOfSAMURessources type)
			throws Exception {
		SamuActions intervention=SamuActions.AppelMedcin;
		this.caeop.execute(intervention, new Serializable[] {position,personId,type}); 
		
	}
	
	@Override
	public boolean samuNonSolliciteExiste(ArrayList<EventI>matchedEvents)throws Exception {
		int nbsamu=0;
        ArrayList<String>samuList=new ArrayList<String>();
		Iterator<String> samuStationsIditerator =
				SmartCityDescriptor.createSAMUStationIdIterator();
		while (samuStationsIditerator.hasNext()) {
			String samuStationId = samuStationsIditerator.next();
			nbsamu++;
			samuList.add(samuStationId);
		}
		SamuDejaSollicite samusEvent=null;
		for(EventI e:matchedEvents) {
			if(e instanceof SamuDejaSollicite) {
				samusEvent=(SamuDejaSollicite) e;
				break;
			}
		}
		if(samusEvent == null) {
			return nbsamu > 1;
		}else {
			for(String s:samuList) {
				if( !(((String) samusEvent.getPropertyValue("samuIdList")).contains(s)))
					return true;
			}
		}
		return false;		
			
		/*
		int nbsamu=0;	
		DemandeInterventionSamu demandeInterventionSamu=(DemandeInterventionSamu) matchedEvents.get(0);
		ArrayList<EventI> correlateEvents=demandeInterventionSamu.getCorrelatedEvents();
		
		Iterator<String> samuStationsIditerator =
				SmartCityDescriptor.createSAMUStationIdIterator();
		while (samuStationsIditerator.hasNext()) {
			String samuStationId = samuStationsIditerator.next();
			nbsamu++;
			
		}
		
			return nbsamu > correlateEvents.size()-1;*/
		
	}

	@Override
	public boolean isMedicAvailable() {
		
		return this.medicsAvailable.get();
	}

	@Override
	public void propagerEvent(EventI event) throws Exception {
		this.cscop.sendEvent(this.correlateurId, event);
	}

	@Override
	public String getExecutorId() throws Exception {
		
		return this.executor;
	}


	




	
	
	
	

}
