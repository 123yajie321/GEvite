package fr.sorbonne_u.cps.smartcity;

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
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.cps.smartcity.components.FireStationFacade;
import fr.sorbonne_u.cps.smartcity.components.SAMUStationFacade;
import fr.sorbonne_u.cps.smartcity.components.TrafficLightFacade;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;

import java.util.Iterator;

// -----------------------------------------------------------------------------
/**
 * The class <code>BasicSimFacadeCVM</code> deploys the components that uses
 * the basic smart city simulator.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * This class is meant as an example of how to code events emitter and actions
 * executor components in a complex event processing system connected to the
 * smart city simulators. A simple protocol is used to create and connect
 * facade components that play the role of events emitters and action
 * executors to proxy components that are deployed in the simulator. Proxy
 * components will represent assets in the smart city, such as fire and
 * SAMU stations as well as controllable traffic lights.
 * </p>
 * <p>
 * Information about the smart city and the simulator components are accessible
 * as static members of a descriptor class. Here, the class
 * {@code BasicSimSmartCityDescriptor} plays that role. It allows to get the
 * fire and SAMU stations identifiers, to iterate over them, to get the
 * required proxy components inbound ports URI to be able to connect to them.
 * </p>
 * <p>
 * As proxy components need to known the notification inbound ports URI created
 * in events emitter components, the method {@code deploy} in this class first
 * create its face components passing them newly generated inbound ports URI
 * that are registered using a method {@code register} defined in the inherited
 * class {@code AbstractBasicSimCVM} for each of the assets.
 * </p>
 * <p>
 * {@code AbstractBasicSimCVM}, in charge of deploying the proxy components,
 * has its {@code deploy} method called (with super), will create the each
 * proxy component passing it the inbound port URI of the facade component
 * to allow them to connect properly its notification outbound port.
 * </p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * 
 * <p>Created on : 2022-02-03</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			BasicSimFacadeCVM
extends		AbstractBasicSimCVM
{
	public				BasicSimFacadeCVM() throws Exception
	{
		super();
	}

	/**
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#deploy()
	 */
	@Override
	public void			deploy() throws Exception
	{
		// create an iterator over valid fire station identifiers, which in turn
		// allow to perform operations on the smart city descriptor to get
		// information about them
		Iterator<String> fireStationIdsIterator =
					BasicSimSmartCityDescriptor.createFireStationIdIterator();
		while (fireStationIdsIterator.hasNext()) {
			String fireStationId = fireStationIdsIterator.next();
			// generate an inbound port URI to be used by the facade component
			// and passed to the proxy components
			String notificationInboundPortURI = AbstractPort.generatePortURI();
			// register the notification inbound port URI to be used when
			// creating proxy components
			this.register(fireStationId, notificationInboundPortURI);
			// create the facade component for a fire station, passing the
			// notification inbound port URI to be used to create its port
			// and the services inbound port URI of the proxy component to
			// connect its service outbound port properly to the proxy
			AbstractComponent.createComponent(
				FireStationFacade.class.getCanonicalName(),
				new Object[]{
						fireStationId,
						notificationInboundPortURI,
						BasicSimSmartCityDescriptor.
										getActionInboundPortURI(fireStationId)
						});
		}

		Iterator<String> samuStationsIditerator =
					BasicSimSmartCityDescriptor.createSAMUStationIdIterator();
		while (samuStationsIditerator.hasNext()) {
			String samuStationId = samuStationsIditerator.next();
			String notificationInboundPortURI = AbstractPort.generatePortURI();
			this.register(samuStationId, notificationInboundPortURI);
			AbstractComponent.createComponent(
					SAMUStationFacade.class.getCanonicalName(),
					new Object[]{
							samuStationId,
							notificationInboundPortURI,
							BasicSimSmartCityDescriptor.
										getActionInboundPortURI(samuStationId)
							});
		}

		Iterator<IntersectionPosition> trafficLightsIterator =
					BasicSimSmartCityDescriptor.createTrafficLightPositionIterator();
		while (trafficLightsIterator.hasNext()) {
			IntersectionPosition p = trafficLightsIterator.next();
			String notificationInboundPortURI = AbstractPort.generatePortURI();
			this.register(p.toString(), notificationInboundPortURI);
			AbstractComponent.createComponent(
					TrafficLightFacade.class.getCanonicalName(),
					new Object[]{
							p,
							notificationInboundPortURI,
							BasicSimSmartCityDescriptor.
												getActionInboundPortURI(p)
							});
		}

		super.deploy();
	}

	public static void	main(String[] args)
	{
		try {
			BasicSimFacadeCVM c = new BasicSimFacadeCVM();
			c.startStandardLifeCycle(10000L);
			Thread.sleep(100000L);
			System.exit(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
// -----------------------------------------------------------------------------
