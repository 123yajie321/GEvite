package gevite.traffic;

import java.io.Serializable;
import java.time.LocalTime;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.grid.Direction;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightNotificationImplI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import gevite.actions.ActionI;
import gevite.actions.SamuActions;
import gevite.actions.TrafficLightActions;
import gevite.cep.ActionExecutionCI;
import gevite.cep.CEPBusManagementCI;
import gevite.cep.EventEmissionCI;
import gevite.cep.ResponseI;
import gevite.cepbus.CEPBus;
import gevite.connector.ConnectorEmitterRegister;
import gevite.connector.ConnectorEmitterSend;
import gevite.connector.ConnectorExcuteurRegister;
import gevite.connector.ConnectorTrafficAction;
import gevite.emitteur.EmitterRegisterOutboundPort;
import gevite.emitteur.EmitterSendOutboundPort;
import gevite.evenement.atomique.circulation.DemandePriorite;
import gevite.evenement.atomique.circulation.PassageVehicule;
import gevite.executeur.ActionExecutionInboundPort;
import gevite.executeur.ExecuteurRegisterOutboundPort;
@OfferedInterfaces(offered= {ActionExecutionCI.class})
@RequiredInterfaces(required = {CEPBusManagementCI.class,EventEmissionCI.class})
public class Traffic extends AbstractComponent implements TrafficLightNotificationImplI{
	
	protected IntersectionPosition					position;
	protected String idTrafficLight; 

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
	protected ActionExecutionInboundPort TrafficLightAeip;


	protected Traffic(String registeEmitteurInboundPort,String registeExecuteurInboundPort,String sendInboundPort,String trafficInport,
			IntersectionPosition position,String actionInboundPort,String id) throws Exception {
		super(1,0);
	//	this.sendEventOutboundPort_URI = sendOutport;
		this.registeEmInboundPort_URI = registeEmitteurInboundPort;
		this.registeExInboundPort_URI = registeExecuteurInboundPort;
		this.TrafficReceiveNotifyInboundPort_URI = trafficInport;
		this.position = position;
		this.idTrafficLight=id;
		this.actionInboundPort_URI = actionInboundPort;
		this.TrafficLightAeip=new ActionExecutionInboundPort(actionInboundPort, this);
		
		
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
					this.taop.getPortURI(),
					this.actionInboundPort_URI,
					ConnectorTrafficAction.class.getCanonicalName());
			
			
			String SendEventInbound_URI=this.erop.registerEmitter(idTrafficLight);
			this.doPortConnection(
					this.esop.getPortURI(),
					SendEventInbound_URI,
					ConnectorEmitterSend.class.getCanonicalName());
			
			
			
		} catch (Exception e) {
			throw new ComponentStartException(e) ;
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
		
		this.esop.sendEvent(idTrafficLight, pVehicule);
	}
	
	public ResponseI execute(ActionI a, Serializable[] params) throws Exception {
		assert a instanceof TrafficLightActions;
		assert params != null && params.length == 3 && params[0] instanceof AbsolutePosition&&params[2] instanceof TypeOfSAMURessources;
		AbsolutePosition position = (AbsolutePosition) params[0];
		String personId=(String)params[1];
		TypeOfSAMURessources type=(TypeOfSAMURessources)params[2];
		
		switch((TrafficLightActions)tf) {
		case InterventionAmbulance:this.saop.triggerIntervention(position, personId, type); break;
		case IntervetionMedcin:this.saop.triggerIntervention(position, personId, type);break;
		case AppelMedcin:this.saop.triggerIntervention(position, personId, type);
			
		}
	    ResponseI response=null;
		return  response;	
			
		}
	
	
	
}
