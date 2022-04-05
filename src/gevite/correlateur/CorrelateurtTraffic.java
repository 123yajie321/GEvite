
package gevite.correlateur;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.smartcity.SmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.descriptions.AbstractSmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;
import gevite.actions.TrafficLightActions;
import gevite.cep.ActionExecutionCI;
import gevite.cep.CEPBusManagementCI;
import gevite.cep.EventEmissionCI;
import gevite.cep.EventReceptionCI;
import gevite.cepbus.CEPBus;
import gevite.connector.ConnectorCorrelateurCepServices;
import gevite.evenement.EventBase;
import gevite.evenement.EventI;
import gevite.plugin.PluginActionExecuteOut;
import gevite.plugin.PluginEmissionOut;
import gevite.rule.RuleBase;

@OfferedInterfaces(offered = {EventReceptionCI.class})
@RequiredInterfaces(required = {CEPBusManagementCI.class,EventEmissionCI.class,ActionExecutionCI.class})

public class CorrelateurtTraffic extends AbstractComponent implements CirculationCorrelatorStateI{
	
	//public static final String CERCIP_URI = "cercip-uri";
	//public static final String CCROP_URI = "ccrop-uri";
	//public static final String CESCOP_URI = "cescop-uri";
	
	
	//EmitteurOutboundPort
	protected EventEmissionCI cscop;
	
	protected ArrayList<ActionExecutionCI> list_caeop;

		
	protected CorrelateurRecieveEventInboundPort cercip;
	protected CorrelateurCepServicesOutboundPort ccrop;
	//protected CorrelateurActionExecutionOutboundPort caeop;
	//protected CorrelateurSendCepOutboundPort cscop;
	
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
		//this.caeop=new CorrelateurActionExecutionOutboundPort(this);
		//this.cscop=new CorrelateurSendCepOutboundPort(this);
		//this.caeop.publishPort();
		this.ccrop.publishPort();
		this.cercip.publishPort();
		//this.cscop.publishPort();
		this.correlateurId= correlateurId;
		this.executors=executors;
		this.emitters=emitters;
		this.baseRule=ruleBase;
		list_caeop = new ArrayList<ActionExecutionCI>();

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
		//String ActionExecutionInboundPort=this.ccrop.getExecutorInboundPortURI(executors.get(0));// à modifier
		//this.doPortConnection(this.caeop.getPortURI(), ActionExecutionInboundPort, ConnectorCorrelateurTrafficLight.class.getCanonicalName());
		
		for(String emitter: emitters) {
			this.ccrop.subscribe(correlateurId, emitter);
		}
		
		PluginEmissionOut pluginOut = new PluginEmissionOut();
		pluginOut.setInboundPortUri(sendEventInboundPort);
		pluginOut.setPluginURI("CorrelateurTrafficLightEmissionPluginOut_"+correlateurId);
		this.installPlugin(pluginOut);
		
		cscop = pluginOut.getEmissionService();
		
		for(int i = 0; i < executors.size(); i++) {
			PluginActionExecuteOut pluginExecuteOut = new PluginActionExecuteOut();
			String ActionExecutionInboundPort=this.ccrop.getExecutorInboundPortURI(executors.get(i));
			pluginExecuteOut.setInboundPortUri(ActionExecutionInboundPort);
			pluginExecuteOut.setPluginURI("CorrelateurTrafficLightActionExecutePluginOut_"+correlateurId);
			this.installPlugin(pluginExecuteOut);
			list_caeop.add(pluginExecuteOut.getActionEecutionService());
		}
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
		//if(event instanceof DemandePriorite) {System.out.println("receive Event DemandPriorite");}
		this.baseEvent.addEvent(event);
		baseRule.fireFirstOn(baseEvent, this);
	
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
				SmartCityDescriptor.createTrafficLightPositionIterator();
		
	
		/*
		while (trafficLightsIterator.hasNext()) {
			if(trafficLightsIterator.next().equals(position)) {
				break;
			}
			index++;
		}
		*/
		TrafficLightActions traffic=TrafficLightActions.changePriority;
		this.list_caeop.get(0).execute(traffic, new Serializable[] {p}); 
		
	}




	
	
	
	

}
