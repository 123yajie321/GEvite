package gevite.correlateur;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.smartcity.BasicSimSmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightActionCI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;
import gevite.actions.SamuActions;
import gevite.actions.TrafficLightActions;
import gevite.cep.CEPBusManagementCI;
import gevite.cep.EventEmissionCI;
import gevite.cep.EventReceptionCI;
import gevite.cepbus.CEPBus;
import gevite.connector.ConnectorCorrelateurCepServices;
import gevite.connector.ConnectorCorrelateurExecutor;
import gevite.connector.ConnectorCorrelateurSendCep;
import gevite.connector.ConnectorCorrelateurTrafficLight;
import gevite.evenement.EventBase;
import gevite.evenement.EventI;
import gevite.evenement.atomique.circulation.DemandePriorite;
import gevite.rule.RuleBase;

@OfferedInterfaces(offered = {EventReceptionCI.class})
@RequiredInterfaces(required = {CEPBusManagementCI.class,EventEmissionCI.class})

public class CorrelateurtTraffic extends AbstractComponent implements CirculationCorrelatorStateI{
	
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
	
	//l'ensemble des emitters (ou correlateur ) abonné
	protected ArrayList<String>emitters;
	
	protected CorrelateurtTraffic(String correlateurId,ArrayList<String> executors,ArrayList<String>emitters,RuleBase ruleBase) throws Exception{
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
		super.shutdown();
	}
	
	public void addEvent(String emitterURI, EventI event) throws Exception {
		if(event instanceof DemandePriorite) {System.out.println("receive Event DemandPriorite");}
		this.baseEvent.addEvent(event);
		baseRule.fireAllOn(baseEvent, this);
		//this.eventEmitter.put(event, emitterURI);
	}
	/*

	@Override
	public boolean estAvantDestination(Serializable destination) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void passerIntersectionN(Serializable priorite) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public boolean estApresDestination(Serializable destination) {
		// TODO Auto-generated method stub
		return false;
	}
	*/

	@Override
	public void changePriority(TypeOfTrafficLightPriority p,IntersectionPosition position) throws Exception {
		int index = 0;
		Iterator<IntersectionPosition> trafficLightsIterator =
				BasicSimSmartCityDescriptor.createTrafficLightPositionIterator();
		while (trafficLightsIterator.hasNext()) {
			if(trafficLightsIterator.next().equals(position)) {
				break;
			}
			index++;
		}
		TrafficLightActions traffic=TrafficLightActions.changePriority;
		String ActionExecutionInboundPort=this.ccrop.getExecutorInboundPortURI(executors.get(index));
		this.doPortConnection(this.caeop.getPortURI(), ActionExecutionInboundPort, ConnectorCorrelateurTrafficLight.class.getCanonicalName());
		this.caeop.execute(traffic, new Serializable[] {p}); 
		
	}




	
	
	
	

}
