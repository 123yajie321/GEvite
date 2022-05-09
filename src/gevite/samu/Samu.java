package gevite.samu;

import java.io.Serializable;
import java.time.LocalTime;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.SAMUActionCI;
import fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationCI;
import fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;
import fr.sorbonne_u.cps.smartcity.SmartCityDescriptor;

import gevite.actions.ActionI;
import gevite.actions.SamuActions;
import gevite.cep.ActionExecutionCI;
import gevite.cep.ActionExecutionImplementationCI;
import gevite.cep.CEPBusManagementCI;
import gevite.cep.EventEmissionCI;
import gevite.cep.ResponseI;
import gevite.cepbus.CEPBus;
import gevite.connector.ConnectorCepManagement;
import gevite.connector.ConnectorEmitterRegister;
import gevite.connector.ConnectorEmitterSend;
import gevite.connector.ConnectorExcuteurRegister;

import gevite.emitteur.EmitterRegisterOutboundPort;
import gevite.emitteur.EmitterSendOutboundPort;
import gevite.evenement.atomique.circulation.AtDestination;
import gevite.evenement.atomique.circulation.AtStation;
import gevite.evenement.atomique.circulation.DemandePriorite;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.atomique.samu.AmbulancesAvailable;
import gevite.evenement.atomique.samu.AmbulancesBusy;
import fr.sorbonne_u.cps.smartcity.connections.SAMUActionConnector;
import fr.sorbonne_u.cps.smartcity.connections.SAMUActionInboundPort;
import fr.sorbonne_u.cps.smartcity.connections.SAMUActionOutboundPort;
import fr.sorbonne_u.cps.smartcity.connections.SAMUNotificationInboundPort;
import gevite.evenement.atomique.samu.MedecinAvailable;
import gevite.evenement.atomique.samu.MedecinBusy;
import gevite.evenement.atomique.samu.SignaleManuel;

import gevite.executeur.ExecuteurRegisterOutboundPort;
import gevite.plugin.PluginActionExecuteIn;
import gevite.plugin.PluginEmissionIn;
import gevite.plugin.PluginEmissionOut;
import gevite.port.CepManagementOutboundPort;


@OfferedInterfaces(offered= {ActionExecutionCI.class,SAMUNotificationCI.class})
@RequiredInterfaces(required = {CEPBusManagementCI.class,EventEmissionCI.class,SAMUActionCI.class})
public class Samu extends AbstractComponent implements SAMUNotificationImplI,ActionExecutionImplementationCI{
	
	protected EventEmissionCI sendOutRef;
	protected String registeEmInboundPort_URI;
	protected String registeExInboundPort_URI;
	protected String SAMUReceiveNotifyInboundPort_URI;
	protected String samuId;
	protected String actionInboundPort_URI;
	protected CepManagementOutboundPort cmop;
	protected SAMUNotificationInboundPort snip;
	protected SAMUActionOutboundPort saop;
	//receive action from correlateur
	protected ActionExecutionCI SAMUaeip;
	protected String busManagementInboundPortUri;
	
	
	
	protected Samu(String samuInport,String samuId,String actionInboundPort,String busManagementInboundPortUri) throws Exception {
		super(2,0);
		this.SAMUReceiveNotifyInboundPort_URI = samuInport;
		this.samuId = samuId;
		
		//The uri inboudport of samuProxy
		this.actionInboundPort_URI = actionInboundPort;	
		this.cmop = new CepManagementOutboundPort(this);
		this.cmop.publishPort();
		this.snip = new SAMUNotificationInboundPort(SAMUReceiveNotifyInboundPort_URI, this);
		this.snip.publishPort();
		this.saop = new SAMUActionOutboundPort(this);
		this.saop.publishPort();
		this.busManagementInboundPortUri=busManagementInboundPortUri;
		
		PluginActionExecuteIn pluginActionExecuteIn=new PluginActionExecuteIn();
		pluginActionExecuteIn.setPluginURI("pluginSamuActionExecute_in"+samuId);
		this.installPlugin(pluginActionExecuteIn);
		
		this.getTracer().setTitle("SAMUStationFacade");
		this.getTracer().setRelativePosition(1, 0);
		this.toggleTracing();
	}
	
	
	@Override
	public synchronized void	start() throws ComponentStartException
	{
		super.start();

		try {
			this.doPortConnection(
					this.cmop.getPortURI(),
					this.busManagementInboundPortUri,
					ConnectorCepManagement.class.getCanonicalName());
			
			this.doPortConnection(
					this.saop.getPortURI(),
					this.actionInboundPort_URI,
					SAMUActionConnector.class.getCanonicalName());

		} catch (Exception e) {
		}
	}
	
	
	
	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		String SendEventInbound_URI=this.cmop.registerEmitter(samuId);
		String port_uriString=((PluginActionExecuteIn)this.getPlugin("pluginSamuActionExecute_in"+samuId)).getActionEecutionService();
		this.cmop.registerExecutor(this.samuId,port_uriString );
		PluginEmissionOut pluginOut = new PluginEmissionOut();
		pluginOut.setInboundPortUri(SendEventInbound_URI);
		pluginOut.setPluginURI("SamuPluginOut_"+samuId);
		this.installPlugin(pluginOut);
		
		sendOutRef = pluginOut.getEmissionService();
		
		
	}
	
	@Override
	public synchronized void finalise() throws Exception {		
		
		this.doPortDisconnection(cmop.getPortURI());
		this.doPortDisconnection(saop.getPortURI());

		super.finalise();
	}
	
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		
		try {
			this.cmop.unpublishPort();
			this.saop.unpublishPort();
			this.snip.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e) ;
		}
		
		
		super.shutdown();
	}
	
	
	

	
	
	@Override
public ResponseI executeAction(ActionI a, Serializable[] params) throws Exception {

	assert a instanceof SamuActions;
	assert params != null && params.length == 3 && params[0] instanceof AbsolutePosition&&params[2] instanceof TypeOfSAMURessources;
	AbsolutePosition position = (AbsolutePosition) params[0];
	String personId=(String)params[1];
	TypeOfSAMURessources type=(TypeOfSAMURessources)params[2];
	switch((SamuActions)a) {
	case InterventionAmbulance:this.saop.triggerIntervention(position, personId, type); this.traceMessage("Trigger ambulance intervention\n");break;
	case IntervetionMedcin:this.saop.triggerIntervention(position, personId, type);this.traceMessage("Trigger medic intervention\n");break;
	case AppelMedcin:this.saop.triggerIntervention(position, personId, type);this.traceMessage("Trigger medic call\n");
		
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
		this.sendOutRef.sendEvent(samuId, aSante);
		System.out.println("samu send");
		
		
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
		//aSante.putProperty("samuId", this.samuId);

		this.sendOutRef.sendEvent(samuId, aSante);
	
	
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
		
		this.sendOutRef.sendEvent(samuId, sManuel);
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
		
		this.sendOutRef.sendEvent(samuId, dPriorite);
	}


	@Override
	public void			atDestination(String vehicleId, LocalTime occurrence)
	throws Exception
	{
		this.traceMessage("Vehicle " + vehicleId +
						  " has arrived at destination at " + occurrence + "\n");
		AtDestination atDestination = new AtDestination(occurrence);
		atDestination.putProperty("vehicleId", vehicleId);
		
		this.sendOutRef.sendEvent(samuId, atDestination);
	}


	@Override
	public void			atStation(String vehicleId, LocalTime occurrence)
	throws Exception
	{
		this.traceMessage("Vehicle " + vehicleId + " has arrived at station at "
								+ occurrence + "\n");
		AtStation atStation =  new AtStation(occurrence);
		atStation.putProperty("vehicleId", atStation);
		
		this.sendOutRef.sendEvent(samuId, atStation);
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
		medecinAvailable.putProperty("samuId", samuId);
		this.sendOutRef.sendEvent(samuId, medecinAvailable);
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
		medecinBusy.putProperty("samuId", samuId);
		this.sendOutRef.sendEvent(samuId, medecinBusy);
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
		ambulancesAvailable.putProperty("samuId", samuId);
		this.sendOutRef.sendEvent(samuId, ambulancesAvailable);
	
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
		ambulancesBusy.putProperty("samuId", samuId);
		this.sendOutRef.sendEvent(samuId, ambulancesBusy);
	
	}

	

}
