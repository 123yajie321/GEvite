package gevite.connector;

import java.time.LocalTime;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.cps.smartcity.grid.Direction;
import fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightNotificationCI;

public class ConnectorTrafficNotification extends AbstractConnector implements TrafficLightNotificationCI{

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightNotificationCI#vehiclePassage(java.lang.String, fr.sorbonne_u.cps.smartcity.grid.Direction, java.time.LocalTime)
	 */
	@Override
	public void			vehiclePassage(
		String vehicleId,
		Direction d,
		LocalTime occurrence
		) throws Exception
	{
		((TrafficLightNotificationCI)this.offering).
									vehiclePassage(vehicleId, d, occurrence);
	}

	
	

}
