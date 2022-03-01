package gevite.traffic;

import java.time.LocalTime;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.smartcity.grid.Direction;
import fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationCI;
import fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightNotificationCI;
import fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightNotificationImplI;

public class TrafficNotifyInboundPort extends AbstractInboundPort implements TrafficLightNotificationCI{
	
	private static final long serialVersionUID = 1L;

	public TrafficNotifyInboundPort(ComponentI owner) throws Exception {
		super(SAMUNotificationCI.class,owner);
		// TODO Auto-generated constructor stub
	}
	
	public TrafficNotifyInboundPort(String uri,ComponentI owner) throws Exception {
		super(uri,SAMUNotificationCI.class,owner);
		// TODO Auto-generated constructor stub
	}

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
		this.getOwner().handleRequest(
				o -> { ((TrafficLightNotificationImplI)o).
									vehiclePassage(vehicleId, d, occurrence);
						return null;
				});
	}


}
