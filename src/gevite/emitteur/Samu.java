package gevite.emitteur;

import java.time.LocalTime;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.cps.smartcity.connections.SAMUActionOutboundPort;
import fr.sorbonne_u.cps.smartcity.connections.SAMUNotificationInboundPort;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;
import gevite.evenement.atomique.samu.HealthEvent;

public class Samu extends AbstractComponent implements SAMUNotificationImplI{
	
	protected String sendEventOutboundPort_URI;
	protected String registeOutboundPort_URI;
	protected String emitterRecieveNotifyInboundPort_URI;
	protected String samuId;
	protected String actionInboundPort_URI;
	protected String actionOutboundPort_URI;

	protected EmitterRegisterOutboundPort erop;
	protected EmitterSendOutboundPort esop;
	protected SAMUNotificationInboundPort snip;
	protected SAMUActionOutboundPort saop;
	

	protected Samu(String registeOutport,String sendOutport,String samuInport,
			String samuId,String actionInboundPort) throws Exception {
		super(1,0);
		this.sendEventOutboundPort_URI = sendOutport;
		this.registeOutboundPort_URI = registeOutport;
		this.emitterRecieveNotifyInboundPort_URI = samuInport;
		this.samuId = samuId;
		this.actionInboundPort_URI = actionInboundPort;
		
		
		this.erop = new EmitterRegisterOutboundPort(registeOutboundPort_URI,this);
		this.erop.publishPort();
		this.esop = new EmitterSendOutboundPort(sendEventOutboundPort_URI,this);
		this.esop.publishPort();
		this.snip = new SAMUNotificationInboundPort(emitterRecieveNotifyInboundPort_URI, this);
		this.snip.publishPort();
		this.saop = new SAMUActionOutboundPort(this);
		this.saop.publishPort();
	}
	
	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		String uri=this.erop.registerEmitter(registeOutboundPort_URI);
		
		HealthEvent he=new HealthEvent();
	}
	
	@Override
	public synchronized void finalise() throws Exception {		
		this.doPortDisconnection(registeOutboundPort_URI);
		super.finalise();
	}
	
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		super.shutdown();
	}
	@Override
	public void healthAlarm(AbsolutePosition position, TypeOfHealthAlarm type, LocalTime occurrence) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void trackingAlarm(AbsolutePosition position, String personId, LocalTime occurrence) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void manualSignal(String personId, LocalTime occurrence) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void requestPriority(IntersectionPosition intersection, TypeOfTrafficLightPriority priority,
			String vehicleId, AbsolutePosition destination, LocalTime occurrence) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void atDestination(String vehicleId, LocalTime occurrence) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void atStation(String vehicleId, LocalTime occurrence) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void notifyMedicsAvailable(LocalTime occurrence) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void notifyNoMedicAvailable(LocalTime occurrence) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void notifyAmbulancesAvailable(LocalTime occurrence) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void notifyNoAmbulanceAvailable(LocalTime occurrence) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
