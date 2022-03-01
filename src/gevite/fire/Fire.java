package gevite.fire;

import java.time.LocalTime;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationImplI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFire;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;
import gevite.cepbus.CEPBus;
import gevite.connector.ConnectorEmitterRegister;
import gevite.connector.ConnectorEmitterSend;
import gevite.connector.ConnectorExcuteurRegister;
import gevite.connector.ConnectorFIREAction;
import gevite.emitteur.EmitterRegisterOutboundPort;
import gevite.emitteur.EmitterSendOutboundPort;
import gevite.executeur.ExecuteurRegisterOutboundPort;


public class Fire extends AbstractComponent implements FireStationNotificationImplI{

		//protected String sendEventOutboundPort_URI;
		protected String registeEmInboundPort_URI;
		protected String registeExInboundPort_URI;

		protected String FIREReceiveNotifyInboundPort_URI;
		protected String fireId;
		protected String actionInboundPort_URI;
		//protected String actionOutboundPort_URI;

		protected EmitterRegisterOutboundPort erop;
		protected ExecuteurRegisterOutboundPort exrop;

		protected EmitterSendOutboundPort esop;
		
		protected FIRENotifyInboundPort fnip;
		protected FIREActionOutboundPort faop;
		
		protected Fire(String registeEmitteurInboundPort,String registeExecuteurInboundPort,String sendInboundPort,String fireInport,
				String fireId,String actionInboundPort) throws Exception {
			super(1,0);
		//	this.sendEventOutboundPort_URI = sendOutport;
			this.registeEmInboundPort_URI = registeEmitteurInboundPort;
			this.registeExInboundPort_URI = registeExecuteurInboundPort;
			this.FIREReceiveNotifyInboundPort_URI = fireInport;
			this.fireId = fireId;
			this.actionInboundPort_URI = actionInboundPort;
			
			
			this.erop = new EmitterRegisterOutboundPort(this);
			this.erop.publishPort();
			this.exrop = new ExecuteurRegisterOutboundPort(this);
			this.exrop.publishPort();
			this.esop = new EmitterSendOutboundPort(this);
			this.esop.publishPort();
			this.fnip = new FIRENotifyInboundPort(FIREReceiveNotifyInboundPort_URI, this);
			this.fnip.publishPort();
			this.faop = new FIREActionOutboundPort(this);
			this.faop.publishPort();
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
						this.faop.getPortURI(),
						this.actionInboundPort_URI,
						ConnectorFIREAction.class.getCanonicalName());
				
				
				
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
			this.doPortDisconnection(faop.getPortURI());

			super.finalise();
		}
		
		@Override
		public synchronized void shutdown() throws ComponentShutdownException {
			
			try {
				this.erop.unpublishPort();
				this.exrop.unpublishPort();
				this.esop.unpublishPort();
				this.faop.unpublishPort();
			} catch (Exception e) {
				throw new ComponentShutdownException(e) ;
			}
			
			
			super.shutdown();
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
		}

		/**
		 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationImplI#endOfFire(fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition, java.time.LocalTime)
		 */
		@Override
		public void			endOfFire(
			AbsolutePosition position,
			LocalTime occurrence
			) throws Exception
		{
			this.traceMessage("End of fire received from position " + position +
							  " at " + occurrence + "\n");
		}

		/**
		 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationImplI#requestPriority(fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition, fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority, java.lang.String, fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition, java.time.LocalTime)
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
							  " towards " + destination + " at " + occurrence +
							  "\n");
		}

		/**
		 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationImplI#atDestination(java.lang.String, java.time.LocalTime)
		 */
		@Override
		public void			atDestination(String vehicleId, LocalTime occurrence)
		throws Exception
		{
			this.traceMessage("Vehicle " + vehicleId +
							   " has arrived at destination\n");
		}

		/**
		 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationImplI#atStation(java.lang.String, java.time.LocalTime)
		 */
		@Override
		public void			atStation(String vehicleId, LocalTime occurrence)
		throws Exception
		{
			this.traceMessage("Vehicle " + vehicleId + " has arrived at station\n");
		}

		/**
		 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationImplI#notifyNoStandardTruckAvailable(java.time.LocalTime)
		 */
		@Override
		public void			notifyNoStandardTruckAvailable(LocalTime occurrence)
		throws Exception
		{
			this.traceMessage("No standard truck available received at " +
							  occurrence + "\n");
		}

		/**
		 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationImplI#notifyStandardTrucksAvailable(java.time.LocalTime)
		 */
		@Override
		public void			notifyStandardTrucksAvailable(LocalTime occurrence)
		throws Exception
		{
			this.traceMessage("Standard trucks available received at " +
							  occurrence + "\n");		
		}

		/**
		 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationImplI#notifyNoHighLadderTruckAvailable(java.time.LocalTime)
		 */
		@Override
		public void			notifyNoHighLadderTruckAvailable(LocalTime occurrence)
		throws Exception
		{
			this.traceMessage("No high ladder truck available received at " +
							  occurrence + "\n");
		}

		/**
		 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationImplI#notifyHighLadderTrucksAvailable(java.time.LocalTime)
		 */
		@Override
		public void 		notifyHighLadderTrucksAvailable(LocalTime occurrence)
		throws Exception
		{
			this.traceMessage("High ladder trucks available received at " +
							  occurrence + "\n");
		}
		
		
		
		
		
		
		

}
