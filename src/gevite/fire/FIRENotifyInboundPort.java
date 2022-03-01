package gevite.fire;

import java.time.LocalTime;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationCI;
import fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationImplI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFire;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;

public class FIRENotifyInboundPort extends AbstractInboundPort implements FireStationNotificationCI{

	private static final long serialVersionUID = 1L;

	public FIRENotifyInboundPort(ComponentI owner) throws Exception {
		super(FireStationNotificationCI.class,owner);
	}
	
	public FIRENotifyInboundPort(String uri,ComponentI owner) throws Exception {
		super(uri,FireStationNotificationCI.class,owner);
	}


	@Override
	public void			fireAlarm(
		AbsolutePosition position,
		LocalTime occurrence,
		TypeOfFire type
		) throws Exception
	{
		this.getOwner().handleRequest(
			o -> {	((FireStationNotificationImplI)o).fireAlarm(
												position, occurrence, type);
					return null;
				 });
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationCI#endOfFire(fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition, java.time.LocalTime)
	 */
	@Override
	public void			endOfFire(
		AbsolutePosition position,
		LocalTime occurrence
		) throws Exception
	{
		this.getOwner().handleRequest(
			o -> {	((FireStationNotificationImplI)o).endOfFire(
												position, occurrence);
					return null;
				 });
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationCI#requestPriority(fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition, fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority, java.lang.String, fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition, java.time.LocalTime)
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
		this.getOwner().handleRequest(
				o -> {	((FireStationNotificationImplI)o).
								requestPriority(intersection, priority,
												vehicleId, destination,
												occurrence);
						return null;
					 });
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationCI#atDestination(java.lang.String, java.time.LocalTime)
	 */
	@Override
	public void			atDestination(String vehicleId, LocalTime occurrence)
	throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((FireStationNotificationImplI)o).
										atDestination(vehicleId, occurrence);
						return null;
					 });
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationCI#atStation(java.lang.String, java.time.LocalTime)
	 */
	@Override
	public void			atStation(String vehicleId, LocalTime occurrence)
	throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((FireStationNotificationImplI)o).
										atStation(vehicleId, occurrence);
						return null;
					 });
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationCI#notifyNoStandardTruckAvailable(java.time.LocalTime)
	 */
	@Override
	public void			notifyNoStandardTruckAvailable(LocalTime occurrence)
	throws Exception
	{
		this.getOwner().handleRequest(
			o -> {	((FireStationNotificationImplI)o).
								notifyNoStandardTruckAvailable(occurrence);
					return null;
				 });
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationCI#notifyStandardTrucksAvailable(java.time.LocalTime)
	 */
	@Override
	public void			notifyStandardTrucksAvailable(LocalTime occurrence)
	throws Exception
	{
		this.getOwner().handleRequest(
			o -> {	((FireStationNotificationImplI)o).
								notifyStandardTrucksAvailable(occurrence);
					return null;
				 });
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationCI#notifyNoHighLadderTruckAvailable(java.time.LocalTime)
	 */
	@Override
	public void			notifyNoHighLadderTruckAvailable(LocalTime occurrence)
	throws Exception
	{
		this.getOwner().handleRequest(
			o -> {	((FireStationNotificationImplI)o).
								notifyNoHighLadderTruckAvailable(occurrence);
					return null;
				 });
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationCI#notifyHighLadderTrucksAvailable(java.time.LocalTime)
	 */
	@Override
	public void			notifyHighLadderTrucksAvailable(LocalTime occurrence)
	throws Exception
	{
		this.getOwner().handleRequest(
			o -> {	((FireStationNotificationImplI)o).
								notifyHighLadderTrucksAvailable(occurrence);
					return null;
				 });
	}
	

}
