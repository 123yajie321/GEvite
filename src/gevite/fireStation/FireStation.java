package gevite.fireStation;

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
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;
import gevite.actions.ActionI;
import gevite.actions.FireStationActions;
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

import gevite.emitteur.EmitterRegisterOutboundPort;
import gevite.emitteur.EmitterSendOutboundPort;
import gevite.evenement.atomique.circulation.AtDestination;
import gevite.evenement.atomique.circulation.AtStation;
import gevite.evenement.atomique.circulation.DemandePriorite;
import gevite.evenement.atomique.pompier.AlarmFeu;
import gevite.evenement.atomique.pompier.EndFire;
import gevite.evenement.atomique.pompier.HighLadderTrucksAvailable;
import gevite.evenement.atomique.pompier.HighLadderTrucksBusy;
import gevite.evenement.atomique.pompier.StandardTrucksAvailable;
import gevite.evenement.atomique.pompier.StandardTrucksBusy;
import gevite.evenement.atomique.samu.MedecinAvailable;

import gevite.executeur.ExecuteurRegisterOutboundPort;
import gevite.plugin.PluginActionExecuteIn;
import gevite.plugin.PluginEmissionOut;

@OfferedInterfaces(offered= {ActionExecutionCI.class,FireStationNotificationCI.class})
@RequiredInterfaces(required = {CEPBusManagementCI.class,EventEmissionCI.class,FireStationActionCI.class})
public class FireStation extends AbstractComponent implements FireStationNotificationImplI,ActionExecutionCI{

		//EmitteurOutboundPort
		protected EventEmissionCI sendOutRef;
		
		//protected String sendEventOutboundPort_URI;
		protected String registeEmInboundPort_URI;
		protected String registeExInboundPort_URI;

		protected String FIREReceiveNotifyInboundPort_URI;
		protected String fireStationId;
		protected String actionInboundPort_URI;
		//protected String actionOutboundPort_URI;

		protected EmitterRegisterOutboundPort erop;
		protected ExecuteurRegisterOutboundPort exrop;

		//protected EmitterSendOutboundPort esop;
		
		protected FireStationNotificationInboundPort fnip;
		protected FireStationActionOutboundPort faop;
		//protected PompierActionExecutionInboundPort FSaeip;
		protected ActionExecutionCI FSaeip;
		
		//String registeEmitteurInboundPort ,String registeExecuteurInboundPort(utiliser dans le cas deux CEPbus)
		//String sendInboundPort
		protected FireStation(String fireInport,String fireStationId,String actionInboundPort) throws Exception {
			super(2,0);
			//this.sendEventOutboundPort_URI = sendOutport;
			//this.registeEmInboundPort_URI = registeEmitteurInboundPort;
			//this.registeExInboundPort_URI = registeExecuteurInboundPort;
			this.FIREReceiveNotifyInboundPort_URI = fireInport; 
			this.fireStationId = fireStationId;
			this.actionInboundPort_URI = actionInboundPort;
			
			//this.FSaeip=new PompierActionExecutionInboundPort(this);
			//this.FSaeip.publishPort();
			
			
			this.erop = new EmitterRegisterOutboundPort(this);
			this.erop.publishPort();
			this.exrop = new ExecuteurRegisterOutboundPort(this);
			this.exrop.publishPort();
			//this.esop = new EmitterSendOutboundPort(this);
			//this.esop.publishPort();
			this.fnip = new FireStationNotificationInboundPort(FIREReceiveNotifyInboundPort_URI, this);
			this.fnip.publishPort();
			this.faop = new FireStationActionOutboundPort(this);
			this.faop.publishPort();
			
			PluginActionExecuteIn pluginActionExecuteIn=new PluginActionExecuteIn();
			pluginActionExecuteIn.setPluginURI("pluginFireStationActionExecute_in"+fireStationId);
			this.installPlugin(pluginActionExecuteIn);
			
			this.getTracer().setTitle("FireStation");
			this.getTracer().setRelativePosition(1, 1);
			this.toggleTracing();
		}
		
		
		@Override
		public synchronized void	start() throws ComponentStartException
		{
			super.start();

			try {
				this.doPortConnection(
						this.erop.getPortURI(),
						CEPBus.CSIP_URI,
						ConnectorEmitterRegister.class.getCanonicalName());
				this.doPortConnection(
						this.exrop.getPortURI(),
						CEPBus.CSIP_URI,
						ConnectorExcuteurRegister.class.getCanonicalName());
				
				this.doPortConnection(
						this.faop.getPortURI(),
						this.actionInboundPort_URI,
						FireStationActionConnector.class.getCanonicalName());	
			} catch (Exception e) {
				throw new ComponentStartException(e) ;
			}
		}
		
		@Override
		public synchronized void execute() throws Exception {
			super.execute();
			String SendEventInbound_URI=this.erop.registerEmitter(fireStationId);
			/*this.doPortConnection(
					this.esop.getPortURI(),
					SendEventInbound_URI,
					ConnectorEmitterSend.class.getCanonicalName());
					*/
			
			String port_uriString=((PluginActionExecuteIn)this.getPlugin("pluginFireStationActionExecute_in"+fireStationId)).getActionEecutionService();
			this.exrop.registerExecutor(this.fireStationId,port_uriString );
			PluginEmissionOut pluginOut = new PluginEmissionOut();
			pluginOut.setInboundPortUri(SendEventInbound_URI);
			pluginOut.setPluginURI("FireStationPluginOut_"+fireStationId);
			this.installPlugin(pluginOut);
			
			sendOutRef = pluginOut.getEmissionService();
			
		}
		
		@Override
		public synchronized void finalise() throws Exception {		
			
			this.doPortDisconnection(erop.getPortURI());
			this.doPortDisconnection(exrop.getPortURI());
			//this.doPortDisconnection(esop.getPortURI());
			this.doPortDisconnection(faop.getPortURI());

			super.finalise();
		}
		
		@Override
		public synchronized void shutdown() throws ComponentShutdownException {
			
			try {
				this.erop.unpublishPort();
				this.exrop.unpublishPort();
				//this.esop.unpublishPort();
				this.faop.unpublishPort();
				this.fnip.unpublishPort();
				
			} catch (Exception e) {
				throw new ComponentShutdownException(e) ;
			}
			
			
			super.shutdown();
		}
		
		@Override
		public ResponseI execute(ActionI a, Serializable[] params) throws Exception {
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
			
			//this.esop.sendEvent(fireStationId, aFeu);
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
			
			//this.esop.sendEvent(fireStationId, endFire);
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
			
			//this.esop.sendEvent(fireStationId, dPriorite);
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
			
			//this.esop.sendEvent(fireStationId, atDestination);
			this.sendOutRef.sendEvent(fireStationId, atDestination);

		}


		@Override
		public void			atStation(String vehicleId, LocalTime occurrence)
		throws Exception
		{
			this.traceMessage("Vehicle " + vehicleId + " has arrived at station\n");
			AtStation atStation =  new AtStation(occurrence);
			atStation.putProperty("vehicleId", atStation);
			
			//this.esop.sendEvent(fireStationId, atStation);
			this.sendOutRef.sendEvent(fireStationId, atStation);

		}


		@Override
		public void			notifyNoStandardTruckAvailable(LocalTime occurrence)
		throws Exception
		{
			this.traceMessage("No standard truck available received at " +
							  occurrence + "\n");
			StandardTrucksBusy StandardTrucksBusy = new StandardTrucksBusy(occurrence);
			//this.esop.sendEvent(fireStationId, StandardTrucksBusy);
			this.sendOutRef.sendEvent(fireStationId, StandardTrucksBusy);

		}


		@Override
		public void			notifyStandardTrucksAvailable(LocalTime occurrence)
		throws Exception
		{
			this.traceMessage("Standard trucks available received at " +
							  occurrence + "\n");	
			StandardTrucksAvailable StandardTrucksAvailable = new StandardTrucksAvailable(occurrence);
			//this.esop.sendEvent(fireStationId, StandardTrucksAvailable);
			this.sendOutRef.sendEvent(fireStationId, StandardTrucksAvailable);

		}


		@Override
		public void			notifyNoHighLadderTruckAvailable(LocalTime occurrence)
		throws Exception
		{
			this.traceMessage("No high ladder truck available received at " +
							  occurrence + "\n");
			HighLadderTrucksBusy HighLadderTrucksBusy = new HighLadderTrucksBusy(occurrence);
			//this.esop.sendEvent(fireStationId, HighLadderTrucksBusy);
			this.sendOutRef.sendEvent(fireStationId, HighLadderTrucksBusy);

		}

	
		@Override
		public void 		notifyHighLadderTrucksAvailable(LocalTime occurrence)
		throws Exception
		{
			this.traceMessage("High ladder trucks available received at " +
							  occurrence + "\n");
			HighLadderTrucksAvailable HighLadderTrucksAvailable = new HighLadderTrucksAvailable(occurrence);
			//this.esop.sendEvent(fireStationId, HighLadderTrucksAvailable);
			this.sendOutRef.sendEvent(fireStationId, HighLadderTrucksAvailable);
		}
		
		
		
		
		
		
		

}
