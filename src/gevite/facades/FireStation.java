package gevite.facades;

import java.io.Serializable;
import java.time.LocalTime;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.smartcity.connections.FireStationActionConnector;
import fr.sorbonne_u.cps.smartcity.connections.FireStationActionOutboundPort;
import fr.sorbonne_u.cps.smartcity.connections.FireStationNotificationInboundPort;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.FireStationActionCI;
import fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationImplI;
import fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationCI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFire;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFirefightingResource;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;

import gevite.actions.ActionI;
import gevite.actions.FireStationActions;
import gevite.connector.ConnectorCepManagement;
import gevite.evenement.atomique.circulation.AtDestination;
import gevite.evenement.atomique.circulation.AtStation;
import gevite.evenement.atomique.circulation.DemandePriorite;
import gevite.evenement.atomique.pompier.AlarmFeu;
import gevite.evenement.atomique.pompier.EndFire;
import gevite.evenement.atomique.pompier.HighLadderTrucksAvailable;
import gevite.evenement.atomique.pompier.HighLadderTrucksBusy;
import gevite.evenement.atomique.pompier.StandardTrucksAvailable;
import gevite.evenement.atomique.pompier.StandardTrucksBusy;
import gevite.interfaces.ActionExecutionCI;
import gevite.interfaces.ActionExecutionImplementationI;
import gevite.interfaces.CEPBusManagementCI;
import gevite.interfaces.EventEmissionCI;
import gevite.interfaces.ResponseI;
import gevite.plugin.PluginActionExecuteIn;
import gevite.plugin.PluginEmissionOut;
import gevite.port.CepManagementOutboundPort;

/**
 * The class <code>FireStation</code> implements a component that can
 * emit events and execute actions
 * 
 *  <p>
 * The component implements the {@code FireStationNotificationImplI} interface
 * to receive the notification from fire station proxy component and to perform actions,
 * it implements l'interface  {@code ActionExecutionImplementationI} to execute actions that come from
 * correlator
 * </p>
 *    
 *    
 * @author Yajie LIU, Zimeng ZHANG
 */



@OfferedInterfaces(offered= {FireStationNotificationCI.class})
@RequiredInterfaces(required = {CEPBusManagementCI.class,FireStationActionCI.class})
public class FireStation extends AbstractComponent implements FireStationNotificationImplI,ActionExecutionImplementationI{

		/**Outbound port to send event to the cepBus */
		protected EventEmissionCI sendOutRef;
		/**id of fire station */
		protected String fireStationId;
		/**uri of inbound port of component proxy which is used to execute action*/
		protected String actionInboundPort_URI;
		/**uri of inbound port of CEPBus for management*/
	    protected String busManagementInboundPortUri;
	    /**Outbound port of this component to connect to Bus for management  */
		protected CepManagementOutboundPort cmop;
		/** inbound port of this component which is used to receive notification from componet proxy*/
		protected FireStationNotificationInboundPort fnip;
		  /**Outbound port of this component to connect with componet proxy to execute actions  */
		protected FireStationActionOutboundPort faop;
		
		/**inbound port that uesd to connect to correlator for receiving the demande of execution of actions  */
		protected ActionExecutionCI FSaeip;
		
		
		protected FireStation(String fireInport,String fireStationId,String actionInboundPort,String busManagementInboundPortUri) throws Exception {
			super(2,0);
			
			this.fireStationId = fireStationId;
			this.actionInboundPort_URI = actionInboundPort;
			this.busManagementInboundPortUri=busManagementInboundPortUri;
			this.cmop = new CepManagementOutboundPort(this);
			this.cmop.publishPort();
			this.fnip = new FireStationNotificationInboundPort(fireInport, this);
			this.fnip.publishPort();
			this.faop = new FireStationActionOutboundPort(this);
			this.faop.publishPort();
			
			PluginActionExecuteIn pluginActionExecuteIn=new PluginActionExecuteIn();
			pluginActionExecuteIn.setPluginURI("pluginFireStationActionExecute_in"+fireStationId);
			this.installPlugin(pluginActionExecuteIn);
			
			this.getTracer().setTitle("FireStationFacade");
			this.getTracer().setRelativePosition(1, 1);
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
						this.faop.getPortURI(),
						this.actionInboundPort_URI,
						FireStationActionConnector.class.getCanonicalName());	
					System.out.println("action execute connected");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public synchronized void execute() throws Exception {
			super.execute();
			
			String SendEventInbound_URI=this.cmop.registerEmitter(fireStationId);
			String port_uriString=((PluginActionExecuteIn)this.getPlugin("pluginFireStationActionExecute_in"+fireStationId)).getActionEecutionService();
			this.cmop.registerExecutor(this.fireStationId,port_uriString );
			PluginEmissionOut pluginOut = new PluginEmissionOut();
			pluginOut.setInboundPortUri(SendEventInbound_URI);
			pluginOut.setPluginURI("FireStationPluginOut_"+fireStationId);
			this.installPlugin(pluginOut);
			sendOutRef = pluginOut.getEmissionService();
			
		}
		
		@Override
		public synchronized void finalise() throws Exception {		
			
			this.doPortDisconnection(cmop.getPortURI());
			this.doPortDisconnection(faop.getPortURI());
			super.finalise();
		}
		
		@Override
		public synchronized void shutdown() throws ComponentShutdownException {
			
			try {
				this.cmop.unpublishPort();
				this.faop.unpublishPort();
				this.fnip.unpublishPort();
				
			} catch (Exception e) {
				throw new ComponentShutdownException(e) ;
			}
			
			
			super.shutdown();
		}
		
		
		/**
		 * @see gevite.facades.FireStation#executeAction(gevite.actions.ActionI,Serializable[])
		 * 
		 */
		@Override
		public ResponseI executeAction(ActionI a, Serializable[] params) throws Exception {
			assert a instanceof FireStationActions;
			assert params != null &&params[0] instanceof AbsolutePosition;
			
			AbsolutePosition position=(AbsolutePosition) params[0];
			
			TypeOfFirefightingResource resource=null;
		
			if(params[1] !=null) {
				resource=(TypeOfFirefightingResource) params[1];
			}
			switch((FireStationActions)a) {
			case FirstAlarme:this.faop.triggerFirstAlarm(position, resource);; break;
			case SecondAlarme:this.faop.triggerSecondAlarm(position);break;
			case GeneraleAlarme:this.faop.triggerGeneralAlarm(position);
			default:
				System.out.println("Invalide Action");
				
			}
		    ResponseI response=null;
			return  response;	
				
			}
		
		// -------------------------------------------------------------------------
		// Component services implementation
		// -------------------------------------------------------------------------

		
		@Override
		public void			fireAlarm(
			AbsolutePosition position,
			LocalTime occurrence,
			TypeOfFire type
			) throws Exception
		{
			this.traceMessage("Fire alarm of type " + type +
							  " received from position " + position +
							  " at " + occurrence + "\n");
			AlarmFeu aFeu = new AlarmFeu(occurrence);
			aFeu.putProperty("position", position);
			aFeu.putProperty("type", type);
			
			this.sendOutRef.sendEvent(fireStationId, aFeu);
			System.out.println("firestation send");

		}


		@Override
		public void			endOfFire(
			AbsolutePosition position,
			LocalTime occurrence
			) throws Exception
		{
			this.traceMessage("End of fire received from position " + position +
							  " at " + occurrence + "\n");
			EndFire endFire = new EndFire(occurrence);
			endFire.putProperty("position", position);
			
			
			this.sendOutRef.sendEvent(fireStationId, endFire);

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
							  " towards " + destination + " at " + occurrence +
							  "\n");
			
			DemandePriorite dPriorite = new DemandePriorite(occurrence);
			dPriorite.putProperty("interPosition", intersection);
			dPriorite.putProperty("priority", priority);
			dPriorite.putProperty("vehicleId", vehicleId);
			dPriorite.putProperty("destination", destination);
			this.sendOutRef.sendEvent(fireStationId, dPriorite);

		}


		@Override
		public void			atDestination(String vehicleId, LocalTime occurrence)
		throws Exception
		{
			this.traceMessage("Vehicle " + vehicleId +
							   " has arrived at destination\n");
			AtDestination atDestination = new AtDestination(occurrence);
			atDestination.putProperty("vehicleId", vehicleId);
			this.sendOutRef.sendEvent(fireStationId, atDestination);

		}


		@Override
		public void			atStation(String vehicleId, LocalTime occurrence)
		throws Exception
		{
			this.traceMessage("Vehicle " + vehicleId + " has arrived at station\n");
			AtStation atStation =  new AtStation(occurrence);
			atStation.putProperty("vehicleId", atStation);
			this.sendOutRef.sendEvent(fireStationId, atStation);

		}


		@Override
		public void			notifyNoStandardTruckAvailable(LocalTime occurrence)
		throws Exception
		{
			this.traceMessage("No standard truck available received at " +
							  occurrence + "\n");
			StandardTrucksBusy StandardTrucksBusy = new StandardTrucksBusy(occurrence);
			StandardTrucksBusy.putProperty("fireStationId", fireStationId);
			this.sendOutRef.sendEvent(fireStationId, StandardTrucksBusy);

		}


		@Override
		public void			notifyStandardTrucksAvailable(LocalTime occurrence)
		throws Exception
		{
			this.traceMessage("Standard trucks available received at " +
							  occurrence + "\n");	
			StandardTrucksAvailable StandardTrucksAvailable = new StandardTrucksAvailable(occurrence);
			StandardTrucksAvailable.putProperty("fireStationId", fireStationId);
			this.sendOutRef.sendEvent(fireStationId, StandardTrucksAvailable);

		}


		@Override
		public void			notifyNoHighLadderTruckAvailable(LocalTime occurrence)
		throws Exception
		{    
			this.traceMessage("No high ladder truck available received at " +
							  occurrence + "\n");
			HighLadderTrucksBusy HighLadderTrucksBusy = new HighLadderTrucksBusy(occurrence);
			HighLadderTrucksBusy.putProperty("fireStationId", fireStationId);
			this.sendOutRef.sendEvent(fireStationId, HighLadderTrucksBusy);

		}

	
		@Override
		public void 		notifyHighLadderTrucksAvailable(LocalTime occurrence)
		throws Exception
		{
			this.traceMessage("High ladder trucks available received at " +
							  occurrence + "\n");
			HighLadderTrucksAvailable HighLadderTrucksAvailable = new HighLadderTrucksAvailable(occurrence);
			HighLadderTrucksAvailable.putProperty("fireStationId", fireStationId);
			this.sendOutRef.sendEvent(fireStationId, HighLadderTrucksAvailable);
		}
		
		
		
		
		
		
		

}
