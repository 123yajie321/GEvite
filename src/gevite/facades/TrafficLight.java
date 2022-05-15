package gevite.facades;

/**
 * The class <code>TrafficLight</code> implements a component that can
 * emit events and execute actions
 * 
 *  <p>
 * The component implements the {@code TrafficLightNotificationImplI} interface
 * to receive the notification from samu proxy component and to perform actions,
 * it implements l'interface  {@code ActionExecutionImplementationI} to execute actions that come from
 * correlator
 * </p>
 *    
 *    
 * @author Yajie LIU, Zimeng ZHANG
 */

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
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;
import gevite.actions.ActionI;
import gevite.actions.TrafficLightActions;
import gevite.connector.ConnectorCepManagement;
import fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightNotificationCI;
import gevite.evenement.atomique.circulation.DemandePriorite;
import gevite.evenement.atomique.circulation.PassageVehicule;
import gevite.interfaces.ActionExecutionCI;
import gevite.interfaces.ActionExecutionImplementationI;
import gevite.interfaces.CEPBusManagementCI;
import gevite.interfaces.EventEmissionCI;
import gevite.interfaces.ResponseI;
import gevite.plugin.PluginActionExecuteIn;
import gevite.plugin.PluginEmissionOut;
import gevite.port.CepManagementOutboundPort;
@OfferedInterfaces(offered= {TrafficLightNotificationCI.class})
@RequiredInterfaces(required = {CEPBusManagementCI.class,TrafficLightActionCI.class})
public class TrafficLight extends AbstractComponent implements TrafficLightNotificationImplI,ActionExecutionImplementationI{
	
	/**Outbound port to send event to the cepBus */
	protected EventEmissionCI sendOutRef;
	/**The position of Traffic Light, unique */
	protected IntersectionPosition	position;
	/**identifier of Traffic Light */
	protected String idTrafficLight;
	/**uri of inbound port of component proxy which is used to execute action*/
	protected String actionInboundPort_URI;
	/**uri of inbound port of CEPBus for management*/
	protected String busManagementInboundPortUri;
	/** inbound port of this component which is used to receive notification from  proxy componet*/
	protected String TrafficReceiveNotifyInboundPort_URI;
	 /**Outbound port of this component to connect to Bus for management  */
	protected CepManagementOutboundPort cmop;
	/** inbound port of this component which is used to receive notification from  proxy componet*/
	protected TrafficLightNotificationInboundPort tnip;
	 /**Outbound port of this component to connect with componet proxy to execute actions  */
	protected TrafficLightActionOutboundPort taop;
	/**inbound port that uesd to connect to correlator for receiving the demande of execution of actions  */
	protected ActionExecutionCI TrafficLightAeip;


	
	protected TrafficLight(String trafficInport,IntersectionPosition position,String actionInboundPort,String id,String busManagementInboundPortUri ) throws Exception {
		super(2,0);
		
		this.TrafficReceiveNotifyInboundPort_URI = trafficInport;
		this.position = position;
		this.idTrafficLight=id;
		this.actionInboundPort_URI = actionInboundPort;
		this.busManagementInboundPortUri=busManagementInboundPortUri;
		this.cmop = new CepManagementOutboundPort(this);
		this.cmop.publishPort();
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
		this.doPortDisconnection(taop.getPortURI());

		super.finalise();
	}
	
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		
		try {
			this.cmop.unpublishPort();
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
		case changePriority:this.taop.changePriority(priority);this.traceMessage("Traffic Light "+this.position+" Change priority \n"); break;
		case returnToNormalMode:this.taop.returnToNormalMode();	this.traceMessage("Traffic Light"+ this.position +"Return to normal mode\n");
		
			
		}
	    ResponseI response=null;
		return  response;	
			
		}
	
	
	
}
