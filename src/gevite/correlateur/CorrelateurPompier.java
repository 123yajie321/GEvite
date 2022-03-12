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
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFirefightingResource;
import gevite.actions.FireStationActions;
import gevite.actions.SamuActions;
import gevite.cep.CEPBusManagementCI;
import gevite.cep.EventEmissionCI;
import gevite.cep.EventReceptionCI;
import gevite.cepbus.CEPBus;
import gevite.connector.ConnectorCorrelateurCepServices;
import gevite.connector.ConnectorCorrelateurExecutor;
import gevite.connector.ConnectorCorrelateurPompier;
import gevite.connector.ConnectorCorrelateurSendCep;
import gevite.evenement.EventBase;
import gevite.evenement.EventI;
import gevite.rule.RuleBase;

@OfferedInterfaces(offered = {EventReceptionCI.class})
@RequiredInterfaces(required = {CEPBusManagementCI.class,EventEmissionCI.class})

public class CorrelateurPompier extends AbstractComponent implements PompierCorrelatorStateI  {
	
	//public static final String CERCIP_URI = "cercip-uri";
	//public static final String CCROP_URI = "ccrop-uri";
	//public static final String CESCOP_URI = "cescop-uri";
	
	protected CorrelateurRecieveEventInboundPort cercip;
	protected CorrelateurCepServicesOutboundPort ccrop;
	protected CorrelateurActionExecutionOutboundPort caeop;
	protected CorrelateurSendCepOutboundPort cscop;
	
	protected EventBase baseEvent;
	//protected HashMap<EventI, String>eventEmitter;
	protected RuleBase baseRule;
	protected String correlateurId;
	protected ArrayList<String>executors;
	protected String sendEventInboundPort;
	protected ArrayList<String>emitters;
	
	protected boolean echelleAvailable=true;
	protected boolean camionAvailable=true;
	
	protected CorrelateurPompier(String correlateurId,ArrayList<String> executors,ArrayList<String>emitters,RuleBase ruleBase) throws Exception{
		super(2,0);
		baseEvent =new EventBase();
		this.cercip= new CorrelateurRecieveEventInboundPort(this);
		this.ccrop=new CorrelateurCepServicesOutboundPort(this);
		this.caeop=new CorrelateurActionExecutionOutboundPort(this);
		this.cscop=new CorrelateurSendCepOutboundPort(this);
		this.caeop.publishPort();
		this.ccrop.publishPort();
		this.cercip.publishPort();
		this.cscop.publishPort();
		this.correlateurId= correlateurId;
		this.executors=executors;
		this.emitters=emitters;
		this.baseRule=ruleBase;
	}
	

	@Override
	public synchronized void start()throws ComponentStartException{
		try {
			this.doPortConnection(this.ccrop.getPortURI(), CEPBus.CSIP_URI,ConnectorCorrelateurCepServices.class.getCanonicalName() );
			
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
		this.doPortDisconnection(this.caeop.getPortURI());
		this.doPortDisconnection(this.cscop.getPortURI());
		super.finalise();
	}
	
	
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
			
			try {
				this.ccrop.unpublishPort();
				this.caeop.unpublishPort();
				this.cscop.unpublishPort();
				this.cercip.unpublishPort();
				
			} catch (Exception e) {
				throw new ComponentShutdownException(e) ;
			}
			
			
			super.shutdown();
		}
	
	public void addEvent(String emitterURI, EventI event) throws Exception {
			this.baseEvent.addEvent(event);
			//this.eventEmitter.put(event, emitterURI);
			baseRule.fireAllOn(baseEvent, this);
	}



	
	@Override
	public boolean inZone(AbsolutePosition p) throws Exception {
		for(String e:executors) {
			if(SmartCityDescriptor.dependsUpon(p,e))
				return true;
		}
		return false;
	}



	@Override
	public void declancheFirstAlarme(AbsolutePosition position, TypeOfFirefightingResource type) throws Exception {
		FireStationActions firstAlarmActions = FireStationActions.FirstAlarme;
		String ActionExecutionInboundPort=this.ccrop.getExecutorInboundPortURI(executors.get(0));
		this.doPortConnection(this.caeop.getPortURI(), ActionExecutionInboundPort, ConnectorCorrelateurPompier.class.getCanonicalName());
		this.caeop.execute(firstAlarmActions, new Serializable[] {position,type}); 			
	}



	@Override
	public void declancheSecondAlarme(AbsolutePosition position) throws Exception {
		FireStationActions secondAlarmActions = FireStationActions.FirstAlarme;
		String ActionExecutionInboundPort=this.ccrop.getExecutorInboundPortURI(executors.get(0));
		this.doPortConnection(this.caeop.getPortURI(), ActionExecutionInboundPort, ConnectorCorrelateurPompier.class.getCanonicalName());
		this.caeop.execute(secondAlarmActions, new Serializable[] {position}); 			
	}		



	@Override
	public boolean procheCaserneExiste() throws Exception {
		return false;
	}



	@Override
	public void propagerEvent(EventI event) throws Exception {
		this.cscop.sendEvent(this.correlateurId, event);		
	}



	@Override
	public boolean isEchelleDisponible() throws Exception {
		return echelleAvailable;
	}



	@Override
	public boolean isCamionDisponible() throws Exception {
		return camionAvailable;
	}


	@Override
	public void setHighLadderTrucksBusy() throws Exception {
		this.echelleAvailable = false;
	}


	@Override
	public void setStandardTrucksBusy() throws Exception {
		this.camionAvailable = false;
		
	}


	@Override
	public void setHighLadderTrucksAvailable() throws Exception {
		
		this.echelleAvailable = true;
		
	}


	@Override
	public void setStandardTRucksAvailable() throws Exception {
		
		this.camionAvailable = true;
		
	}




	
	
	
	

}
