package gevite.connector;

import java.time.LocalTime;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationCI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFire;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;

public class ConnectorFIRENotification extends AbstractConnector implements FireStationNotificationCI{

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationCI#fireAlarm(fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition, java.time.LocalTime, fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFire)
	 */
	@Override
	public void			fireAlarm(
		AbsolutePosition position,
		LocalTime occurrence,
		TypeOfFire type
		) throws Exception
	{
		((FireStationNotificationCI)this.offering).
										fireAlarm(position, occurrence, type);
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
		((FireStationNotificationCI)this.offering).
										endOfFire(position, occurrence);
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
		((FireStationNotificationCI)this.offering).
						requestPriority(intersection, priority, vehicleId,
										destination, occurrence);
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationCI#atDestination(java.lang.String, java.time.LocalTime)
	 */
	@Override
	public void			atDestination(String vehicleId, LocalTime occurrence)
	throws Exception
	{
		((FireStationNotificationCI)this.offering).
										atDestination(vehicleId, occurrence);
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationCI#atStation(java.lang.String, java.time.LocalTime)
	 */
	@Override
	public void			atStation(String vehicleId, LocalTime occurrence)
	throws Exception
	{
		((FireStationNotificationCI)this.offering).
										atStation(vehicleId, occurrence);
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationCI#notifyNoStandardTruckAvailable(java.time.LocalTime)
	 */
	@Override
	public void			notifyNoStandardTruckAvailable(LocalTime occurrence)
	throws Exception
	{
		((FireStationNotificationCI)this.offering).
									notifyNoStandardTruckAvailable(occurrence);
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationCI#notifyStandardTrucksAvailable(java.time.LocalTime)
	 */
	@Override
	public void			notifyStandardTrucksAvailable(LocalTime occurrence)
	throws Exception
	{
		((FireStationNotificationCI)this.offering).
									notifyStandardTrucksAvailable(occurrence);
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationCI#notifyNoHighLadderTruckAvailable(java.time.LocalTime)
	 */
	@Override
	public void			notifyNoHighLadderTruckAvailable(LocalTime occurrence)
	throws Exception
	{
		((FireStationNotificationCI)this.offering).
								notifyNoHighLadderTruckAvailable(occurrence);
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationCI#notifyHighLadderTrucksAvailable(java.time.LocalTime)
	 */
	@Override
	public void			notifyHighLadderTrucksAvailable(LocalTime occurrence)
	throws Exception
	{
		((FireStationNotificationCI)this.offering).
								notifyHighLadderTrucksAvailable(occurrence);
	}

	
	

}
