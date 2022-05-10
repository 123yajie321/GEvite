package gevite.traffic;

import java.io.Serializable;
import java.time.LocalTime;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.smartcity.connections.TrafficLightActionConnector;
import fr.sorbonne_u.cps.smartcity.connections.TrafficLightActionOutboundPort;
import fr.sorbonne_u.cps.smartcity.connections.TrafficLightNotificationInboundPort;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.grid.Direction;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightActionCI;
import fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightNotificationImplI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;
import gevite.actions.ActionI;
import gevite.actions.SamuActions;
import gevite.actions.TrafficLightActions;
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
import fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightNotificationCI;
import gevite.emitteur.EmitterRegisterOutboundPort;
import gevite.emitteur.EmitterSendOutboundPort;
import gevite.evenement.atomique.circulation.DemandePriorite;
import gevite.evenement.atomique.circulation.PassageVehicule;

import gevite.executeur.ExecuteurRegisterOutboundPort;
import gevite.plugin.PluginActionExecuteIn;
import gevite.plugin.PluginEmissionOut;
import gevite.port.CepManagementOutboundPort;
@OfferedInterfaces(offered= {ActionExecutionCI.class,TrafficLightNotificationCI.class})
@RequiredInterfaces(required = {CEPBusManagementCI.class,EventEmissionCI.class,TrafficLightActionCI.class})
public class TrafficLight extends AbstractComponent implements TrafficLightNotificationImplI,ActionExecutionImplementationCI{
	//EmitteurOutboundPort
	protected EventEmissionCI sendOutRef;
	
	protected IntersectionPosition					position;
	protected String idTrafficLight; 

	//protected String sendEventOutboundPort_URI;
	protected String registeEmInboundPort_URI;
	protected String registeExInboundPort_URI;

	protected String TrafficReceiveNotifyInboundPort_URI;
	protected String actionInboundPort_URI;
	protected String busManagementInboundPortUri;
	//protected String actionOutboundPort_URI;

	//protected EmitterRegisterOutboundPort cmop;
	protected CepManagementOutboundPort cmop;
	//protected ExecuteurRegisterOutboundPort exrop;

	//protected EmitterSendOutboundPort esop;
	
	protected TrafficLightNotificationInboundPort tnip;
	protected TrafficLightActionOutboundPort taop;
	//protected TrafficLightActionExecutionInboundPort TrafficLightAeip;
	
	protected ActionExecutionCI TrafficLightAeip;


	//String registeEmitteurInboundPort ,String registeExecuteurInboundPort(utiliser dans le cas deux CEPbus)
	//String sendInboundPort
	protected TrafficLight(String trafficInport,IntersectionPosition position,String actionInboundPort,String id,String busManagementInboundPortUri ) throws Exception {
		super(2,0);
		//this.sendEventOutboundPort_URI = sendOutport;
		//this.registeEmInboundPort_URI = registeEmitteurInboundPort;
		//this.registeExInboundPort_URI = registeExecuteurInboundPort;
		this.TrafficReceiveNotifyInboundPort_URI = trafficInport;
		this.position = position;
		this.idTrafficLight=id;
		this.actionInboundPort_URI = actionInboundPort;
		this.busManagementInboundPortUri=busManagementInboundPortUri;
		//this.TrafficLightAeip=new TrafficLightActionExecutionInboundPort( this);
		//this.TrafficLightAeip.publishPort();
		
		this.cmop = new CepManagementOutboundPort(this);
		this.cmop.publishPort();
		//this.exrop = new ExecuteurRegisterOutboundPort(this);
		//this.exrop.publishPort();
		//this.esop = new EmitterSendOutboundPort(this);
		//this.esop.publishPort();
		this.tnip = new TrafficLightNotificationInboundPort(TrafficReceiveNotifyInboundPort_URI, this);
		this.tnip.publishPort();
		this.taop = new TrafficLightActionOutboundPort(this);
		this.taop.publishPort();
		
		PluginActionExecuteIn pluginActionExecuteIn=new PluginActionExecuteIn();
		pluginActionExecuteIn.setPluginURI("pluginTrafficLightActionExecute_in"+idTrafficLight);
		this.installPlugin(pluginActionExecuteIn);
		
		this.getTracer().setTitle("TrafficLightFacade");
		this.getTracer().setRelativePosition(1, 2);
		this.toggleTracing();
	}
	
	
	@Override
	public synchronized void	start() throws ComponentStartException
	{
		super.start();

		try {
			this.doPortConnection(
					this.cmop.getPortURI(),
					busManagementInboundPortUri,
					ConnectorCepManagement.class.getCanonicalName());
			/*
			 * this.doPortConnection( this.exrop.getPortURI(), CEPBus.CSIP_URI,
			 * ConnectorExcuteurRegister.class.getCanonicalName());
			 */
			
			this.doPortConnection(
					this.taop.getPortURI(),
					this.actionInboundPort_URI,
					TrafficLightActionConnector.class.getCanonicalName());	
			
		} catch (Exception e) {
			throw new ComponentStartException(e) ;
		}
	}
	
	
	
	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		String SendEventInbound_URI=this.cmop.registerEmitter(idTrafficLight);
		/*this.doPortConnection(
				this.esop.getPortURI(),
				SendEventInbound_URI,
				ConnectorEmitterSend.class.getCanonicalName());
			*/
		
		String port_uriString=((PluginActionExecuteIn)this.getPlugin("pluginTrafficLightActionExecute_in"+idTrafficLight)).getActionEecutionService();
		this.cmop.registerExecutor(this.idTrafficLight,port_uriString );
		PluginEmissionOut pluginOut = new PluginEmissionOut();
		pluginOut.setInboundPortUri(SendEventInbound_URI);
		pluginOut.setPluginURI("TrafficLightPluginOut_"+idTrafficLight);
		this.installPlugin(pluginOut);
		
		sendOutRef = pluginOut.getEmissionService();
		
	}
	
	@Override
	public synchronized void finalise() throws Exception {		
		
		this.doPortDisconnection(cmop.getPortURI());
		//this.doPortDisconnection(exrop.getPortURI());
		//this.doPortDisconnection(esop.getPortURI());
		this.doPortDisconnection(taop.getPortURI());

		super.finalise();
	}
	
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		
		try {
			this.cmop.unpublishPort();
			//this.exrop.unpublishPort();
			//this.esop.unpublishPort();
			this.taop.unpublishPort();
			this.tnip.unpublishPort();
			
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
		
		//this.esop.sendEvent(idTrafficLight, pVehicule);
		this.sendOutRef.sendEvent(idTrafficLight, pVehicule);

	}
	@Override
	public ResponseI executeAction(ActionI a, Serializable[] params) throws Exception {
		assert a instanceof TrafficLightActions;
		TypeOfTrafficLightPriority priority=null;
		if(params !=null) {
			priority=(TypeOfTrafficLightPriority) params[0];
		}
		switch((TrafficLightActions)a) {
		case changePriority:this.taop.changePriority(priority);this.traceMessage("Change priority \n"); break;
		case returnToNormalMode:this.taop.returnToNormalMode();	this.traceMessage("Return to normal mode\n");
		
			
		}
	    ResponseI response=null;
		return  response;	
			
		}
	
	
	
}
