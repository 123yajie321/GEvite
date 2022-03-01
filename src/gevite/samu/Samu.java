package gevite.samu;

import java.time.LocalTime;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.smartcity.descriptions.SmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;
import gevite.cepbus.CEPBus;
import gevite.connector.ConnectorEmitterRegister;
import gevite.connector.ConnectorEmitterSend;
import gevite.connector.ConnectorExcuteurRegister;
import gevite.connector.ConnectorSAMUAction;
import gevite.emitteur.EmitterRegisterOutboundPort;
import gevite.emitteur.EmitterSendOutboundPort;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.atomique.samu.HealthEvent;
import gevite.executeur.ExecuteurRegisterOutboundPort;

public class Samu extends AbstractComponent implements SAMUNotificationImplI{
	
	//protected String sendEventOutboundPort_URI;
	protected String registeEmInboundPort_URI;
	protected String registeExInboundPort_URI;

	protected String SAMUReceiveNotifyInboundPort_URI;
	protected String samuId;
	protected String actionInboundPort_URI;
	//protected String actionOutboundPort_URI;

	protected EmitterRegisterOutboundPort erop;
	protected ExecuteurRegisterOutboundPort exrop;

	protected EmitterSendOutboundPort esop;
	
	protected SAMUNotifyInboundPort snip;
	protected SAMUActionOutboundPort saop;
	

	protected Samu(String registeEmitteurInboundPort,String registeExecuteurInboundPort,String sendInboundPort,String samuInport,
			String samuId,String actionInboundPort) throws Exception {
		super(1,0);
	//	this.sendEventOutboundPort_URI = sendOutport;
		this.registeEmInboundPort_URI = registeEmitteurInboundPort;
		this.registeExInboundPort_URI = registeExecuteurInboundPort;
		this.SAMUReceiveNotifyInboundPort_URI = samuInport;
		this.samuId = samuId;
		this.actionInboundPort_URI = actionInboundPort;
		
		
		this.erop = new EmitterRegisterOutboundPort(this);
		this.erop.publishPort();
		this.exrop = new ExecuteurRegisterOutboundPort(this);
		this.exrop.publishPort();
		this.esop = new EmitterSendOutboundPort(this);
		this.esop.publishPort();
		this.snip = new SAMUNotifyInboundPort(SAMUReceiveNotifyInboundPort_URI, this);
		this.snip.publishPort();
		this.saop = new SAMUActionOutboundPort(this);
		this.saop.publishPort();
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
					CEPBus.CEREIP_URI,
					ConnectorEmitterSend.class.getCanonicalName());
			this.doPortConnection(
					this.saop.getPortURI(),
					this.actionInboundPort_URI,
					ConnectorSAMUAction.class.getCanonicalName());
			
			
			
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
		this.doPortDisconnection(saop.getPortURI());

		super.finalise();
	}
	
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		
		try {
			this.erop.unpublishPort();
			this.exrop.unpublishPort();
			this.esop.unpublishPort();
			this.saop.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e) ;
		}
		
		
		super.shutdown();
	}
	@Override
	public void			healthAlarm(
		AbsolutePosition position,
		TypeOfHealthAlarm type,
		LocalTime occurrence
		) throws Exception
	{
		assert	position != null;
		assert	!type.isTracking();
		assert	occurrence != null;

		assert	SmartCityDescriptor.dependsUpon(position, this.samuId);

		this.traceMessage("Health notification of type " + type +
						  " at position " + position +
						  " received at " + occurrence + "\n");
		AlarmeSante aSante = new AlarmeSante(occurrence);
		aSante.putProperty("type", type.toString());
		aSante.putProperty("position", position);
		
		this.esop.sendEvent(esop.getPortURI(), aSante);
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI#trackingAlarm(fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition, java.lang.String, java.time.LocalTime)
	 */
	@Override
	public void			trackingAlarm(
		AbsolutePosition position,
		String personId,
		LocalTime occurrence
		) throws Exception
	{
		assert	position != null;
		assert	personId != null && !personId.isEmpty();
		assert	occurrence != null;

		assert	SmartCityDescriptor.dependsUpon(position, this.samuId);

		this.traceMessage("Health notification of type tracking for " +
						  personId + " at position " + position +
						  " received at " + occurrence + "\n");
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI#manualSignal(java.lang.String, java.time.LocalTime)
	 */
	@Override
	public void			manualSignal(
		String personId,
		LocalTime occurrence
		) throws Exception
	{
		assert	personId != null && !personId.isEmpty();
		assert	occurrence != null;

		this.traceMessage("Manual signal emitted by " + personId +
						  " received at " + occurrence + "\n");
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI#requestPriority(fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition, fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority, java.lang.String, fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition, java.time.LocalTime)
	 */
	@Override
	public void			requestPriority(
		IntersectionPosition intersection,
		TypeOfTrafficLightPriority priority,
		String vehicleId,
		AbsolutePosition destination,
		LocalTime occurrence
		) throws Exception
	{
		this.traceMessage("priority " + priority + " requested for vehicle " +
						  vehicleId + " at intersection " + intersection +
						  " towards " + destination + " at " + occurrence + "\n");
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI#atDestination(java.lang.String, java.time.LocalTime)
	 */
	@Override
	public void			atDestination(String vehicleId, LocalTime occurrence)
	throws Exception
	{
		this.traceMessage("Vehicle " + vehicleId +
						  " has arrived at destination at " + occurrence + "\n");
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI#atStation(java.lang.String, java.time.LocalTime)
	 */
	@Override
	public void			atStation(String vehicleId, LocalTime occurrence)
	throws Exception
	{
		this.traceMessage("Vehicle " + vehicleId + " has arrived at station at "
								+ occurrence + "\n");
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI#notifyMedicsAvailable(java.time.LocalTime)
	 */
	@Override
	public void			notifyMedicsAvailable(LocalTime occurrence)
	throws Exception
	{
		assert	occurrence != null;

		this.traceMessage(
				"Notification that medics are available received at " +
															occurrence + "\n");
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI#notifyNoMedicAvailable(java.time.LocalTime)
	 */
	@Override
	public void			notifyNoMedicAvailable(LocalTime occurrence)
	throws Exception
	{
		assert	occurrence != null;

		this.traceMessage(
				"Notification that no medic are available received at " +
															occurrence + "\n");
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI#notifyAmbulancesAvailable(java.time.LocalTime)
	 */
	@Override
	public void			notifyAmbulancesAvailable(LocalTime occurrence)
	throws Exception
	{
		assert	occurrence != null;

		this.traceMessage(
				"Notification that ambulances are available received at " +
															occurrence + "\n");
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI#notifyNoAmbulanceAvailable(java.time.LocalTime)
	 */
	@Override
	public void			notifyNoAmbulanceAvailable(LocalTime occurrence)
	throws Exception
	{
		assert	occurrence != null;

		this.traceMessage(
				"Notification that no ambulance are available received at " +
															occurrence + "\n");
	}

}
