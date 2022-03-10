package gevite.traffic;

import java.time.LocalTime;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.smartcity.grid.Direction;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightNotificationImplI;
import gevite.cep.ActionExecutionCI;
import gevite.cep.CEPBusManagementCI;
import gevite.cep.EventEmissionCI;
import gevite.cepbus.CEPBus;
import gevite.connector.ConnectorEmitterRegister;
import gevite.connector.ConnectorEmitterSend;
import gevite.connector.ConnectorExcuteurRegister;
import gevite.connector.ConnectorTrafficAction;
import gevite.emitteur.EmitterRegisterOutboundPort;
import gevite.emitteur.EmitterSendOutboundPort;
import gevite.evenement.atomique.circulation.DemandePriorite;
import gevite.evenement.atomique.circulation.PassageVehicule;
import gevite.executeur.ExecuteurRegisterOutboundPort;
@OfferedInterfaces(offered= {ActionExecutionCI.class})
@RequiredInterfaces(required = {CEPBusManagementCI.class,EventEmissionCI.class})
public class Traffic extends AbstractComponent implements TrafficLightNotificationImplI{
	
	protected IntersectionPosition					position;

	//protected String sendEventOutboundPort_URI;
	protected String registeEmInboundPort_URI;
	protected String registeExInboundPort_URI;

	protected String TrafficReceiveNotifyInboundPort_URI;
	protected String actionInboundPort_URI;
	//protected String actionOutboundPort_URI;

	protected EmitterRegisterOutboundPort erop;
	protected ExecuteurRegisterOutboundPort exrop;

	protected EmitterSendOutboundPort esop;
	
	protected TrafficNotifyInboundPort tnip;
	protected TrafficActionOutboundPort taop;
	

	protected Traffic(String registeEmitteurInboundPort,String registeExecuteurInboundPort,String sendInboundPort,String trafficInport,
			IntersectionPosition position,String actionInboundPort) throws Exception {
		super(1,0);
	//	this.sendEventOutboundPort_URI = sendOutport;
		this.registeEmInboundPort_URI = registeEmitteurInboundPort;
		this.registeExInboundPort_URI = registeExecuteurInboundPort;
		this.TrafficReceiveNotifyInboundPort_URI = trafficInport;
		this.position = position;
		this.actionInboundPort_URI = actionInboundPort;
		
		
		this.erop = new EmitterRegisterOutboundPort(this);
		this.erop.publishPort();
		this.exrop = new ExecuteurRegisterOutboundPort(this);
		this.exrop.publishPort();
		this.esop = new EmitterSendOutboundPort(this);
		this.esop.publishPort();
		this.tnip = new TrafficNotifyInboundPort(TrafficReceiveNotifyInboundPort_URI, this);
		this.tnip.publishPort();
		this.taop = new TrafficActionOutboundPort(this);
		this.taop.publishPort();
	}
	
	
	@Override
	public synchronized void	start() throws ComponentStartException
	{
		super.start();

		try {
			this.doPortConnection(
					this.erop.getPortURI(),
					CEPBus.CRIP_URI,
					ConnectorEmitterRegister.class.getCanonicalName());
			this.doPortConnection(
					this.exrop.getPortURI(),
					CEPBus.CRIP_URI,
					ConnectorExcuteurRegister.class.getCanonicalName());
			this.doPortConnection(
					this.esop.getPortURI(),
					CEPBus.CERIP_URI,
					ConnectorEmitterSend.class.getCanonicalName());
			this.doPortConnection(
					this.taop.getPortURI(),
					this.actionInboundPort_URI,
					ConnectorTrafficAction.class.getCanonicalName());
			
			
			
		} catch (Exception e) {
			throw new ComponentStartException(e) ;
		}
	}
	
	
	
	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		String uri=this.erop.registerEmitter(erop.getPortURI());
		
	}
	
	@Override
	public synchronized void finalise() throws Exception {		
		
		this.doPortDisconnection(erop.getPortURI());
		this.doPortDisconnection(exrop.getPortURI());
		this.doPortDisconnection(esop.getPortURI());
		this.doPortDisconnection(taop.getPortURI());

		super.finalise();
	}
	
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		
		try {
			this.erop.unpublishPort();
			this.exrop.unpublishPort();
			this.esop.unpublishPort();
			this.taop.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e) ;
		}
		
		
		super.shutdown();
	}
	
	@Override
	public void			vehiclePassage(
		String vehicleId,
		Direction d,
		LocalTime occurrence
		) throws Exception
	{
		this.traceMessage("Traffic light at " + this.position +
						  " receives the notification of the passage of " +
						  vehicleId + " in the direction of " + d +
						  " at " + occurrence + "\n");
		PassageVehicule pVehicule = new PassageVehicule(occurrence);
		pVehicule.putProperty("vehicleId", vehicleId);
		pVehicule.putProperty("direction", d);
		
		//this.esop.sendEvent(samuId, pVehicule);
	}
	
}
