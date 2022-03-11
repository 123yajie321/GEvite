package gevite.samu;

import java.io.Serializable;
import java.time.LocalTime;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.smartcity.descriptions.SmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;
import gevite.actions.ActionI;
import gevite.actions.SamuActions;
import gevite.cep.ActionExecutionCI;
import gevite.cep.CEPBusManagementCI;
import gevite.cep.EventEmissionCI;
import gevite.cep.ResponseI;
import gevite.cepbus.CEPBus;
import gevite.connector.ConnectorEmitterRegister;
import gevite.connector.ConnectorEmitterSend;
import gevite.connector.ConnectorExcuteurRegister;
import gevite.connector.ConnectorSAMUAction;
import gevite.emitteur.EmitterRegisterOutboundPort;
import gevite.emitteur.EmitterSendOutboundPort;
import gevite.evenement.atomique.circulation.AtDestination;
import gevite.evenement.atomique.circulation.AtStation;
import gevite.evenement.atomique.circulation.DemandePriorite;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.atomique.samu.AmbulancesAvailable;
import gevite.evenement.atomique.samu.AmbulancesBusy;
import gevite.evenement.atomique.samu.HealthEvent;
import gevite.evenement.atomique.samu.MedecinAvailable;
import gevite.evenement.atomique.samu.MedecinBusy;
import gevite.evenement.atomique.samu.SignaleManuel;
import gevite.executeur.ActionExecutionInboundPort;
import gevite.executeur.ExecuteurRegisterOutboundPort;
import javassist.expr.NewArray;

@OfferedInterfaces(offered= {ActionExecutionCI.class})
@RequiredInterfaces(required = {CEPBusManagementCI.class,EventEmissionCI.class})
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
	protected ActionExecutionInboundPort SAMUaeip;
	

	protected Samu(String registeEmitteurInboundPort,String registeExecuteurInboundPort,String sendInboundPort,String samuInport,
			String samuId,String actionInboundPort) throws Exception {
		super(1,0);
	//	this.sendEventOutboundPort_URI = sendOutport;
		this.registeEmInboundPort_URI = registeEmitteurInboundPort;
		this.registeExInboundPort_URI = registeExecuteurInboundPort;
		this.SAMUReceiveNotifyInboundPort_URI = samuInport;
		this.samuId = samuId;
		this.actionInboundPort_URI = actionInboundPort;
		this.SAMUaeip=new ActionExecutionInboundPort(actionInboundPort, this);
		
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
					this.saop.getPortURI(),
					this.actionInboundPort_URI,
					ConnectorSAMUAction.class.getCanonicalName());
			
			String SendEventInbound_URI=this.erop.registerEmitter(samuId);
			this.doPortConnection(
					this.esop.getPortURI(),
					SendEventInbound_URI,
					ConnectorEmitterSend.class.getCanonicalName());
			
		} catch (Exception e) {
		}
	}
	
	
	
	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		
		
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
	
	
	

	
	
	
public ResponseI execute(ActionI a, Serializable[] params) throws Exception {
	assert a instanceof SamuActions;
	assert params != null && params.length == 3 && params[0] instanceof AbsolutePosition&&params[2] instanceof TypeOfSAMURessources;
	AbsolutePosition position = (AbsolutePosition) params[0];
	String personId=(String)params[1];
	TypeOfSAMURessources type=(TypeOfSAMURessources)params[2];
	
	switch((SamuActions)a) {
	case InterventionAmbulance:this.saop.triggerIntervention(position, personId, type); break;
	case IntervetionMedcin:this.saop.triggerIntervention(position, personId, type);break;
	case AppelMedcin:this.saop.triggerIntervention(position, personId, type);
		
	}
    ResponseI response=null;
	return  response;	
		
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
		aSante.putProperty("type", type);
		aSante.putProperty("position", position);
		
		this.esop.sendEvent(samuId, aSante);
	}


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
		AlarmeSante aSante = new AlarmeSante(occurrence);
		aSante.putProperty("type", TypeOfHealthAlarm.TRACKING);
		aSante.putProperty("position", position);
		aSante.putProperty("personId", personId);

		this.esop.sendEvent(samuId, aSante);
	
	
	}


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
		
		SignaleManuel sManuel = new SignaleManuel(occurrence);
		sManuel.putProperty("personId", personId);
		
		this.esop.sendEvent(samuId, sManuel);
	}

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
	
		DemandePriorite dPriorite = new DemandePriorite(occurrence);
		dPriorite.putProperty("interPosition", intersection);
		dPriorite.putProperty("priority", priority);
		dPriorite.putProperty("vehicleId", vehicleId);
		dPriorite.putProperty("destination", destination);
		
		this.esop.sendEvent(samuId, dPriorite);
	}


	@Override
	public void			atDestination(String vehicleId, LocalTime occurrence)
	throws Exception
	{
		this.traceMessage("Vehicle " + vehicleId +
						  " has arrived at destination at " + occurrence + "\n");
		AtDestination atDestination = new AtDestination(occurrence);
		atDestination.putProperty("vehicleId", vehicleId);
		
		this.esop.sendEvent(samuId, atDestination);
	}


	@Override
	public void			atStation(String vehicleId, LocalTime occurrence)
	throws Exception
	{
		this.traceMessage("Vehicle " + vehicleId + " has arrived at station at "
								+ occurrence + "\n");
		AtStation atStation =  new AtStation(occurrence);
		atStation.putProperty("vehicleId", atStation);
		
		this.esop.sendEvent(samuId, atStation);
	}

	@Override
	public void			notifyMedicsAvailable(LocalTime occurrence)
	throws Exception
	{
		assert	occurrence != null;

		this.traceMessage(
				"Notification that medics are available received at " +
															occurrence + "\n");
		
		MedecinAvailable medecinAvailable = new MedecinAvailable(occurrence);
		this.esop.sendEvent(samuId, medecinAvailable);
	}

	@Override
	public void			notifyNoMedicAvailable(LocalTime occurrence)
	throws Exception
	{
		assert	occurrence != null;

		this.traceMessage(
				"Notification that no medic are available received at " +
															occurrence + "\n");
		MedecinBusy medecinBusy = new MedecinBusy(occurrence);
		this.esop.sendEvent(samuId, medecinBusy);
	}


	@Override
	public void			notifyAmbulancesAvailable(LocalTime occurrence)
	throws Exception
	{
		assert	occurrence != null;

		this.traceMessage(
				"Notification that ambulances are available received at " +
															occurrence + "\n");
		AmbulancesAvailable ambulancesAvailable = new AmbulancesAvailable(occurrence);
		this.esop.sendEvent(samuId, ambulancesAvailable);
	
	}

	@Override
	public void			notifyNoAmbulanceAvailable(LocalTime occurrence)
	throws Exception
	{
		assert	occurrence != null;

		this.traceMessage(
				"Notification that no ambulance are available received at " +
															occurrence + "\n");
		AmbulancesBusy ambulancesBusy = new AmbulancesBusy(occurrence);
		this.esop.sendEvent(samuId, ambulancesBusy);
	
	}

	

}
