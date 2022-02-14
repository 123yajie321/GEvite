package gevite.emitteur;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import gevite.cep.CEPBusManagementCI;


@RequiredInterfaces(required = {CEPBusManagementCI.class})
public class Emetteur extends AbstractComponent {
	
	public static final String ESOP_URI = "esop-uri";
	protected EmitterRegisterOutboundPort esop;

	protected Emetteur() throws Exception {
		super(1,0);
		this.esop = new EmitterRegisterOutboundPort(ESOP_URI,this);
		this.esop.publishPort();
	}
	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		this.esop.registerEmitter(ESOP_URI);
	}
	
	@Override
	public synchronized void finalise() throws Exception {		
		this.doPortDisconnection(ESOP_URI);
		super.finalise();
	}
	
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		super.shutdown();
	}
	
	
	
	
	

}
