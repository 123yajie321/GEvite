package gevite.correlateur;

import java.util.HashMap;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import gevite.cep.CEPBusManagementCI;
import gevite.cep.EventEmissionCI;
import gevite.emitteur.EmitterRegisterOutboundPort;
import gevite.evenement.EventBase;
import gevite.evenement.EventI;
import gevite.rule.RuleBase;

@OfferedInterfaces(offered = {EventEmissionCI.class})
@RequiredInterfaces(required = {CEPBusManagementCI.class})

public class Correlateur extends AbstractComponent {
	
	public static final String CERCIP_URI = "cercip-uri";
	public static final String CCROP_URI = "ccrop-uri";
	//public static final String CESCOP_URI = "cescop-uri";
	
	protected CepEventRecieveCorrelateurInboundPort cercip;
	protected CepCorrelateurRegisterOutboundPort ccrop;
	
	protected EventBase baseEvent;
	protected HashMap<EventI, String>eventEmitter;
	protected RuleBase baseRule;
	
	protected Correlateur() throws Exception{
		super(1,0);
		baseEvent =new EventBase();
		this.cercip= new CepEventRecieveCorrelateurInboundPort(CERCIP_URI,this);
		this.ccrop=new CepCorrelateurRegisterOutboundPort(CCROP_URI,this);
		this.ccrop.publishPort();
		this.cercip.publishPort();
		
	}
	

	
	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		this.ccrop.registerCorrelator(CCROP_URI, CERCIP_URI);
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
	
	public void addEvent(String emitterURI, EventI event) {
			
			this.baseEvent.addEvent(event);
			this.eventEmitter.put(event, emitterURI);
	}
	
	
	
	

}
