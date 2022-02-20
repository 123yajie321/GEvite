package gevite.emitteur;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import gevite.cep.CEPBusManagementCI;
import gevite.evenement.atomique.samu.HealthEvent;


@RequiredInterfaces(required = {CEPBusManagementCI.class})
public class Emetteur extends AbstractComponent {
	
	public static final String EROP_URI = "erop-uri";
	public static final String ESOP_URI = "esop-uri";
	protected EmitterRegisterOutboundPort erop;
	protected EmitterSendOutboundPort esop;

	protected Emetteur() throws Exception {
		super(1,0);
		this.erop = new EmitterRegisterOutboundPort(EROP_URI,this);
		this.erop.publishPort();
		this.esop = new EmitterSendOutboundPort(ESOP_URI,this);
		this.esop.publishPort();
	}
	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		String uri=this.erop.registerEmitter(EROP_URI);
		
		HealthEvent he=new HealthEvent();
	}
	
	@Override
	public synchronized void finalise() throws Exception {		
		this.doPortDisconnection(EROP_URI);
		super.finalise();
	}
	
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		super.shutdown();
	}
	
	
	
	
	

}
