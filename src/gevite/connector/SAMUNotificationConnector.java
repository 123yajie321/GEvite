package gevite.connector;

import java.time.LocalTime;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationCI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;

public class SAMUNotificationConnector extends AbstractConnector implements SAMUNotificationCI{

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationCI#healthAlarm(fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition, fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm, java.time.LocalTime)
	 */
	@Override
	public void			healthAlarm(
		AbsolutePosition position,
		TypeOfHealthAlarm type,
		LocalTime occurrence
		) throws Exception
	{
		((SAMUNotificationCI)this.offering).
									healthAlarm(position, type, occurrence);
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationCI#trackingAlarm(fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition, java.lang.String, java.time.LocalTime)
	 */
	@Override
	public void			trackingAlarm(
		AbsolutePosition position,
		String personId,
		LocalTime occurrence
		) throws Exception
	{
		((SAMUNotificationCI)this.offering).
								trackingAlarm(position, personId, occurrence);
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationCI#manualSignal(java.lang.String, java.time.LocalTime)
	 */
	@Override
	public void			manualSignal(String personId, LocalTime occurrence)
	throws Exception
	{
		((SAMUNotificationCI)this.offering).manualSignal(personId, occurrence);
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationCI#requestPriority(fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition, fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority, java.lang.String, fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition, java.time.LocalTime)
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
		((SAMUNotificationCI)this.offering).
						requestPriority(intersection, priority, vehicleId,
										destination, occurrence);
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationCI#atDestination(java.lang.String, java.time.LocalTime)
	 */
	@Override
	public void			atDestination(String vehicleId, LocalTime occurrence)
	throws Exception
	{
		((SAMUNotificationCI)this.offering).
									atDestination(vehicleId, occurrence);
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationCI#atStation(java.lang.String, java.time.LocalTime)
	 */
	@Override
	public void			atStation(String vehicleId, LocalTime occurrence)
	throws Exception
	{
		((SAMUNotificationCI)this.offering).atStation(vehicleId, occurrence);
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationCI#notifyMedicsAvailable(java.time.LocalTime)
	 */
	@Override
	public void			notifyMedicsAvailable(LocalTime occurrence)
	throws Exception
	{
		((SAMUNotificationCI)this.offering).notifyMedicsAvailable(occurrence);
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationCI#notifyNoMedicAvailable(java.time.LocalTime)
	 */
	@Override
	public void			notifyNoMedicAvailable(LocalTime occurrence)
	throws Exception
	{
		((SAMUNotificationCI)this.offering).notifyNoMedicAvailable(occurrence);
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationCI#notifyAmbulancesAvailable(java.time.LocalTime)
	 */
	@Override
	public void			notifyAmbulancesAvailable(LocalTime occurrence)
	throws Exception
	{
		((SAMUNotificationCI)this.offering).
									notifyAmbulancesAvailable(occurrence);
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationCI#notifyNoAmbulanceAvailable(java.time.LocalTime)
	 */
	@Override
	public void			notifyNoAmbulanceAvailable(LocalTime occurrence)
	throws Exception
	{
		((SAMUNotificationCI)this.offering).
									notifyNoAmbulanceAvailable(occurrence);
	}
	

}
