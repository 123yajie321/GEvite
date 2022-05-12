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

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationCI;
import fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationImplI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFire;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;

import java.time.LocalTime;

// -----------------------------------------------------------------------------
/**
 * The class <code>FireStationNotificationInboundPort</code> implements the
 * inbound port for the {@code FireStationNotificationCI} interface.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * 
 * <p>Created on : 2022-02-01</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			FireStationNotificationInboundPort
extends		AbstractInboundPort
implements	FireStationNotificationCI
{
	private static final long serialVersionUID = 1L;

	/**
	 * create the inbound port instance.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code owner instanceof FireStationNotificationImplI}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param owner			component that owns this port.
	 * @throws Exception	<i>todo.</i>
	 */
	public				FireStationNotificationInboundPort(ComponentI owner)
	throws Exception
	{
		super(FireStationNotificationCI.class, owner);
		assert	owner instanceof FireStationNotificationImplI;
	}

	/**
	 * create the inbound port instance with the given URI.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code owner instanceof FireStationNotificationImplI}
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param uri			unique identifier of the port.
	 * @param owner			component that owns this port.
	 * @throws Exception	<i>todo.</i>
	 */
	public				FireStationNotificationInboundPort(
		String uri,
		ComponentI owner
		) throws Exception
	{
		super(uri, FireStationNotificationCI.class, owner);
		assert	uri != null && !uri.isEmpty();
		assert	owner instanceof FireStationNotificationImplI;
	}

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
// -----------------------------------------------------------------------------
