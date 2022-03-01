package gevite.samu;

import java.time.LocalTime;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationCI;
import fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI;

import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;

public class SAMUNotifyInboundPort extends AbstractInboundPort implements SAMUNotificationCI{
	
	private static final long serialVersionUID = 1L;

	public SAMUNotifyInboundPort(ComponentI owner) throws Exception {
		super(SAMUNotificationCI.class,owner);
		// TODO Auto-generated constructor stub
	}
	
	public SAMUNotifyInboundPort(String uri,ComponentI owner) throws Exception {
		super(uri,SAMUNotificationCI.class,owner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void			healthAlarm(
		AbsolutePosition position,
		TypeOfHealthAlarm type,
		LocalTime occurrence
		) throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((SAMUNotificationImplI)o).
									healthAlarm(position, type, occurrence);
						return null;
					 });
	}

	@Override
	public void			trackingAlarm(
		AbsolutePosition position,
		String personId,
		LocalTime occurrence
		) throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((SAMUNotificationImplI)o).
								trackingAlarm(position, personId, occurrence);
						return null;
					 });
	}

	@Override
	public void			manualSignal(String personId, LocalTime occurrence)
	throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((SAMUNotificationImplI)o).
										manualSignal(personId, occurrence);
						return null;
					 });
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
		this.getOwner().handleRequest(
				o -> {	((SAMUNotificationImplI)o).
									requestPriority(intersection, priority,
													vehicleId, destination,
													occurrence);
						return null;
					 });
	}


	@Override
	public void			atDestination(String vehicleId, LocalTime occurrence)
	throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((SAMUNotificationImplI)o).
										atDestination(vehicleId, occurrence);
						return null;
					 });
	}


	@Override
	public void			atStation(String vehicleId, LocalTime occurrence)
	throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((SAMUNotificationImplI)o).
										atStation(vehicleId, occurrence);
						return null;
					 });
	}


	@Override
	public void			notifyMedicsAvailable(LocalTime occurrence)
	throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((SAMUNotificationImplI)o).
											notifyMedicsAvailable(occurrence);
						return null;
					 });
	}


	@Override
	public void			notifyNoMedicAvailable(LocalTime occurrence)
	throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((SAMUNotificationImplI)o).
											notifyNoMedicAvailable(occurrence);
						return null;
					 });
	}


	@Override
	public void			notifyAmbulancesAvailable(LocalTime occurrence)
	throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((SAMUNotificationImplI)o).
										notifyAmbulancesAvailable(occurrence);
						return null;
					 });
	}


	@Override
	public void			notifyNoAmbulanceAvailable(LocalTime occurrence)
	throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((SAMUNotificationImplI)o).
										notifyNoAmbulanceAvailable(occurrence);
						return null;
					 });
	}


}
