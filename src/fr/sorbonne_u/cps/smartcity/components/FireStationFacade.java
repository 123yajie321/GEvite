package fr.sorbonne_u.cps.smartcity.components;

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

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.smartcity.BasicSimSmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.connections.FireStationNotificationInboundPort;
import fr.sorbonne_u.cps.smartcity.connections.FireStationActionConnector;
import fr.sorbonne_u.cps.smartcity.connections.FireStationActionOutboundPort;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationCI;
import fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationImplI;
import fr.sorbonne_u.cps.smartcity.interfaces.FireStationActionCI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFire;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFirefightingResource;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;

import java.time.LocalTime;

// -----------------------------------------------------------------------------
/**
 * The class <code>FireStationFacade</code> implements a component that can
 * inspire the programming of events emitter and action executors components
 * in the CEP system applied to the smart city application.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2022-01-27</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
@OfferedInterfaces(offered={FireStationNotificationCI.class})
@RequiredInterfaces(required={FireStationActionCI.class})
public class			FireStationFacade
extends		AbstractComponent
implements	FireStationNotificationImplI
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	/** identifier of the corresponding fire station.						*/
	protected String								stationId;
	/** URI of the action inbound port.										*/
	protected String								actionInboundPortURI;
	/** notifucation inbound port.											*/
	protected FireStationNotificationInboundPort	notificationIBP;
	/** action outbound port.												*/
	protected FireStationActionOutboundPort			actionOBP;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a fire station facade component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code BasicSimSmartCityDescriptor.isValidFireStationId(stationId)}
	 * pre	{@code notificationInboundPortURI != null && !notificationInboundPortURI.isEmpty()}
	 * pre	{@code actionInboundPortURI != null && !actionInboundPortURI.isEmpty()}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param stationId						identifier of the corresponding fire station.
	 * @param notificationInboundPortURI	URI of the notification inbound port to be used by this facade component.
	 * @param actionInboundPortURI			URI of the action inbound port of the proxy component.
	 * @throws Exception					<i>to do</i>.
	 */
	protected			FireStationFacade(
		String stationId,
		String notificationInboundPortURI,
		String actionInboundPortURI
		) throws Exception
	{
		super(2, 0);

		assert	BasicSimSmartCityDescriptor.isValidFireStationId(stationId);
		assert	notificationInboundPortURI != null &&
										!notificationInboundPortURI.isEmpty();
		assert	actionInboundPortURI != null &&
										!actionInboundPortURI.isEmpty();

		this.stationId = stationId;
		this.actionInboundPortURI = actionInboundPortURI;
		this.notificationIBP =
				new FireStationNotificationInboundPort(
											notificationInboundPortURI, this);
		this.notificationIBP.publishPort();
		this.actionOBP = new FireStationActionOutboundPort(this);
		this.actionOBP.publishPort();

		this.getTracer().setTitle("FireStationFacade");
		this.getTracer().setRelativePosition(1, 1);
		this.toggleTracing();
	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#start()
	 */
	@Override
	public synchronized void	start() throws ComponentStartException
	{
		super.start();

		try {
			this.doPortConnection(
					this.actionOBP.getPortURI(),
					this.actionInboundPortURI,
					FireStationActionConnector.class.getCanonicalName());
		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#execute()
	 */
	@Override
	public synchronized void	execute() throws Exception
	{
		Thread.sleep(2000);
		AbsolutePosition p = new AbsolutePosition(1.5, 2.0);
		this.traceMessage("Trigger first alarm with standard truck\n");
		this.actionOBP.triggerFirstAlarm(p, TypeOfFirefightingResource.StandardTruck);
		Thread.sleep(100);
		this.traceMessage("Trigger second alarm with standard truck\n");
		this.actionOBP.triggerSecondAlarm(p);
		Thread.sleep(100);
		p = new AbsolutePosition(2.0, 0.5);
		this.traceMessage("Trigger first alarm with high ladder truck\n");
		this.actionOBP.triggerFirstAlarm(p, TypeOfFirefightingResource.HighLadderTruck);
		Thread.sleep(100);
		this.traceMessage("Trigger general alarm\n");
		this.actionOBP.triggerGeneralAlarm(p);
		Thread.sleep(100);
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#finalise()
	 */
	@Override
	public synchronized void	finalise() throws Exception
	{
		this.doPortDisconnection(this.actionOBP.getPortURI());
		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void	shutdown() throws ComponentShutdownException
	{
		try {
			this.notificationIBP.unpublishPort();
			this.actionOBP.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationImplI#fireAlarm(fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition, java.time.LocalTime, fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFire)
	 */
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
// -----------------------------------------------------------------------------
