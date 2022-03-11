package fr.sorbonne_u.cps.smartcity.connections;

// Copyright Jacques Malenfant, Sorbonne Universite.
// Jacques.Malenfant@lip6.fr
//
// This software is a computer program whose purpose is to provide a
// basic component programming model to program with components
// distributed applications in the Java programming language.
//
// This software is governed by the CeCILL-C license under French law and
// abiding by the rules of distribution of free software.  You can use,
// modify and/ or redistribute the software under the terms of the
// CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
// URL "http://www.cecill.info".
//
// As a counterpart to the access to the source code and  rights to copy,
// modify and redistribute granted by the license, users are provided only
// with a limited warranty  and the software's author,  the holder of the
// economic rights,  and the successive licensors  have only  limited
// liability. 
//
// In this respect, the user's attention is drawn to the risks associated
// with loading,  using,  modifying and/or developing or reproducing the
// software by the user in light of its specific status of free software,
// that may mean  that it is complicated to manipulate,  and  that  also
// therefore means  that it is reserved for developers  and  experienced
// professionals having in-depth computer knowledge. Users are therefore
// encouraged to load and test the software's suitability as regards their
// requirements in conditions enabling the security of their systems and/or 
// data to be ensured and,  more generally, to use and operate it in the 
// same conditions as regards security. 
//
// The fact that you are presently reading this means that you have had
// knowledge of the CeCILL-C license and that you accept its terms.

import java.time.LocalTime;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationCI;
import fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;

// -----------------------------------------------------------------------------
/**
 * The class <code>SAMUNotificationInboundPort</code> implements the inbound
 * port for the {@code SAMUNotificationCI} interface.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * 
 * <p>Created on : 2022-02-11</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			SAMUNotificationInboundPort
extends		AbstractInboundPort
implements	SAMUNotificationCI
{
	private static final long serialVersionUID = 1L;

	public				SAMUNotificationInboundPort(ComponentI owner)
	throws Exception
	{
		super(SAMUNotificationCI.class, owner);

		assert	owner instanceof SAMUNotificationImplI;
	}

	public				SAMUNotificationInboundPort(
		String uri,
		ComponentI owner
		) throws Exception
	{
		super(uri, SAMUNotificationCI.class, owner);

		assert	owner instanceof SAMUNotificationImplI;
	}

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
		this.getOwner().handleRequest(
				o -> {	((SAMUNotificationImplI)o).
									healthAlarm(position, type, occurrence);
						return null;
					 });
		
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
		this.getOwner().handleRequest(
				o -> {	((SAMUNotificationImplI)o).
								trackingAlarm(position, personId, occurrence);
						return null;
					 });
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationCI#manualSignal(java.lang.String, java.time.LocalTime)
	 */
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
		this.getOwner().handleRequest(
				o -> {	((SAMUNotificationImplI)o).
									requestPriority(intersection, priority,
													vehicleId, destination,
													occurrence);
						return null;
					 });
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationCI#atDestination(java.lang.String, java.time.LocalTime)
	 */
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

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationCI#atStation(java.lang.String, java.time.LocalTime)
	 */
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

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationCI#notifyMedicsAvailable(java.time.LocalTime)
	 */
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

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationCI#notifyNoMedicAvailable(java.time.LocalTime)
	 */
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

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationCI#notifyAmbulancesAvailable(java.time.LocalTime)
	 */
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

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationCI#notifyNoAmbulanceAvailable(java.time.LocalTime)
	 */
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
// -----------------------------------------------------------------------------
