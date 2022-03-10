package gevite.correlateur;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.smartcity.descriptions.SmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import gevite.actions.SamuActions;
import gevite.cep.CEPBusManagementCI;
import gevite.cep.EventEmissionCI;
import gevite.cep.EventReceptionCI;
import gevite.cepbus.CEPBus;
import gevite.connector.ConnectorCorrelateurExecutor;
import gevite.connector.ConnectorCorrelateurSendCep;
import gevite.evenement.EventBase;
import gevite.evenement.EventI;
import gevite.rule.RuleBase;

@OfferedInterfaces(offered = {EventReceptionCI.class})
@RequiredInterfaces(required = {CEPBusManagementCI.class,EventEmissionCI.class})

public class CorrelateurSamu extends AbstractComponent implements SamuCorrelatorStateI{
	
	//public static final String CERCIP_URI = "cercip-uri";
	//public static final String CCROP_URI = "ccrop-uri";
	//public static final String CESCOP_URI = "cescop-uri";
	
	protected CepEventRecieveCorrelateurInboundPort cercip;
	protected CorrelateurCepServicesOutboundPort ccrop;
	protected CorrelateurActionExecutionOutboundPort caeop;
	protected CorrelateurSendCepOutboundPort cscop;
	
	protected EventBase baseEvent;
	//protected HashMap<EventI, String>eventEmitter;
	protected RuleBase baseRule;
	protected String correlateurId;
	protected CorrelatorStateI correlatorStat;
	protected ArrayList<String>executors;
	protected String sendEventInboundPort;
	protected ArrayList<String>emitters;
	
	protected boolean ambulancesAvailable=true;
	protected boolean medicsAvailable=true;
	
	
	
	protected CorrelateurSamu(String correlateurId,ArrayList<String> executors,ArrayList<String>emitters,CorrelatorStateI correlatorStat,RuleBase ruleBase) throws Exception{
		super(1,0);
		baseEvent =new EventBase();
		this.cercip= new CepEventRecieveCorrelateurInboundPort(this);
		this.ccrop=new CorrelateurCepServicesOutboundPort(this);
		this.caeop=new CorrelateurActionExecutionOutboundPort(this);
		this.cscop=new CorrelateurSendCepOutboundPort(this);
		this.caeop.publishPort();
		this.ccrop.publishPort();
		this.cercip.publishPort();
		this.correlateurId= correlateurId;
		this.executors=executors;
		this.emitters=emitters;
		this.correlatorStat=correlatorStat;
		this.baseRule=ruleBase;
	}
	

	@Override
	public synchronized void start()throws ComponentStartException{
		try {
			sendEventInboundPort= this.ccrop.registerCorrelator(correlateurId, this.cercip.getPortURI());
			this.doPortConnection(this.cscop.getPortURI(), sendEventInboundPort, ConnectorCorrelateurSendCep.class.getCanonicalName());
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	
	
	
	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		
		
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
		super.shutdown();
	}
	
	public void addEvent(String emitterURI, EventI event) throws Exception {
			
			this.baseEvent.addEvent(event);
			//this.eventEmitter.put(event, emitterURI);
			baseRule.fireFirstOn(baseEvent, this);
			
	}


	
	
	/*S16*/
    public void setAmbulancesNoAvailable() throws Exception {
    			this.ambulancesAvailable=false;
    }
	
	
	/*S17*/
    public void setMedcinNoAvailable() throws Exception{
    	this.medicsAvailable=false;
    }
	
	
	/*S18*/
    public void setAmbulancesAvailable() throws Exception{
    	this.ambulancesAvailable=true;
    }
	
	
	/*S19*/
    public void setMedcinAvailable() throws Exception{
    	this.medicsAvailable=true;
    	
    }	

	@Override
	public boolean inZone(AbsolutePosition p) {
		for(String e:executors) {
			if(SmartCityDescriptor.dependsUpon(p,e))
				return true;
		}
		return false;
		
		
	}


	@Override
	public boolean isAmbulanceAvailable() {
		
		return ambulancesAvailable;
	}



	@Override
	public void intervanetionAmbulance(AbsolutePosition position,String personId,TypeOfSAMURessources type) throws Exception {
		SamuActions	intervention=  SamuActions.InterventionAmbulance;
		String ActionExecutionInboundPort=this.ccrop.getExecutorInboundPortURI(executors.get(0));
		this.doPortConnection(this.caeop.getPortURI(), ActionExecutionInboundPort, ConnectorCorrelateurExecutor.class.getCanonicalName());
		this.caeop.execute(intervention, new Serializable[] {position,personId,type}); 
	
	}


	@Override
	public void triggerMedicCall(AbsolutePosition position, String personId, TypeOfSAMURessources type)
			throws Exception {
		SamuActions intervention=SamuActions.AppelMedcin;
		String ActionExecutionInboundPort=this.ccrop.getExecutorInboundPortURI(executors.get(0));
		this.doPortConnection(this.caeop.getPortURI(), ActionExecutionInboundPort, ConnectorCorrelateurExecutor.class.getCanonicalName());
		this.caeop.execute(intervention, new Serializable[] {position,personId,type}); 
		
	}
	
	
	

	@Override
	public boolean isNotAmbulanceAvailable() {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean procheSamuExiste()throws Exception {
		
		return false;
	}



	@Override
	public boolean isMedicAvailable() {
	
		return medicsAvailable;
	}


	@Override
	public boolean isNotMedicAvailable() {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void propagerEvent(EventI event) throws Exception {
		this.cscop.sendEvent(this.correlateurId, event);
		
	}



	




	
	
	
	

}
