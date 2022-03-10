package gevite.correlateur;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import gevite.cep.CEPBusManagementCI;
import gevite.cep.EventEmissionCI;
import gevite.cep.EventReceptionCI;
import gevite.evenement.EventBase;
import gevite.evenement.EventI;
import gevite.rule.RuleBase;

@OfferedInterfaces(offered = {EventReceptionCI.class})
@RequiredInterfaces(required = {CEPBusManagementCI.class,EventEmissionCI.class})

public class CorrelateurtTraffic extends AbstractComponent implements CirculationCorrelatorStateI{
	
	public static final String CERCIP_URI = "cercip-uri";
	public static final String CCROP_URI = "ccrop-uri";
	//public static final String CESCOP_URI = "cescop-uri";
	
	protected CepEventRecieveCorrelateurInboundPort cercip;
	protected CorrelateurCepServicesOutboundPort ccrop;
	
	protected EventBase baseEvent;
	//protected HashMap<EventI, String>eventEmitter;
	protected RuleBase baseRule;
	protected String correlateurId;
	protected CorrelatorStateI correlatorStat;
	protected ArrayList<String>executors;
	protected String sendEventInboundPort;
	protected ArrayList<String>emitters;
	
	protected CorrelateurtTraffic(String correlateurId,ArrayList<String> executors,ArrayList<String>emitters,CorrelatorStateI correlatorStat,RuleBase ruleBase) throws Exception{
		super(1,0);
		baseEvent =new EventBase();
		this.cercip= new CepEventRecieveCorrelateurInboundPort(CERCIP_URI,this);
		this.ccrop=new CorrelateurCepServicesOutboundPort(CCROP_URI,this);
		this.ccrop.publishPort();
		this.cercip.publishPort();
		this.correlateurId= correlateurId;
		this.executors=executors;
		this.emitters=emitters;
		this.correlatorStat=correlatorStat;
		this.baseRule=ruleBase;
	}
	

	
	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		sendEventInboundPort= this.ccrop.registerCorrelator(correlateurId, CERCIP_URI);
		for(String emitter: emitters) {
			this.ccrop.subscribe(correlateurId, emitter);
		}
		
		
	}
	
	@Override
	public synchronized void finalise() throws Exception {		
		this.doPortDisconnection(CCROP_URI);
		super.finalise();
	}
	
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		super.shutdown();
	}
	
	public void addEvent(String emitterURI, EventI event) throws Exception {
			
			this.baseEvent.addEvent(event);
			//this.eventEmitter.put(event, emitterURI);
			baseRule.fireAllOn(baseEvent, this);
			
	}



	@Override
	public void passerIntersectionP(Serializable p) {
		// TODO Auto-generated method stub
		
	}



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




	
	
	
	

}
