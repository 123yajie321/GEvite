package gevite.cep.cvm;

import java.util.Iterator;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.cps.smartcity.AbstractBasicSimCVM;
import fr.sorbonne_u.cps.smartcity.BasicSimSmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import gevite.cep.CEPBusManagementCI;
import gevite.cepbus.CEPBus;
import gevite.connector.ConnectorCorrelateurCepServices;
import gevite.connector.ConnectorEmitterRegister;
import gevite.correlateur.CorrelateurSamu;
import gevite.emitteur.Emetteur;
import gevite.fire.FireStation;
import gevite.samu.Samu;
import gevite.traffic.Traffic;

public class CVM extends AbstractBasicSimCVM {

	public CVM() throws Exception {
	}
	
	@Override
	public void deploy() throws Exception {
		/*
		
		AbstractComponent.createComponent(CEPBus.class.getCanonicalName(), new Object[] {});
		String desURI = AbstractComponent.createComponent(Emetteur.class.getCanonicalName(), new Object[] {});
		this.doPortConnection(desURI, Emetteur.EROP_URI, CEPBus.CRIP_URI, ConnectorEmitterRegister.class.getCanonicalName());

		String correlateurURI = AbstractComponent.createComponent(Correlateur.class.getCanonicalName(), new Object[] {});
		//System.out.println(correlateurURI+";"+Correlateur.CCROP_URI+";"+CEPBus.CCRIP_URI);
		this.doPortConnection(correlateurURI,Correlateur.CCROP_URI, CEPBus.CRIP_URI, ConnectorCorrelateurRegister.class.getCanonicalName());

		super.deploy();
		*/
		
		AbstractComponent.createComponent(CEPBus.class.getCanonicalName(), new Object[] {});


		String correlateurURI = AbstractComponent.createComponent(CorrelateurSamu.class.getCanonicalName(), new Object[] {});
		
		
		
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
						FireStation.class.getCanonicalName(),
						new Object[]{
								CEPBus.CRIP_URI,
								CEPBus.CRIP_URI,
								CEPBus.CERIP_URI,
								notificationInboundPortURI,
								fireStationId,
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
							Samu.class.getCanonicalName(),
							new Object[]{
									CEPBus.CRIP_URI,
									CEPBus.CRIP_URI,
									CEPBus.CERIP_URI,
									notificationInboundPortURI,
									samuStationId,
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
							Traffic.class.getCanonicalName(),
							new Object[]{
									CEPBus.CRIP_URI,
									CEPBus.CRIP_URI,
									CEPBus.CERIP_URI,
									notificationInboundPortURI,
									p,
									BasicSimSmartCityDescriptor.
														getActionInboundPortURI(p)
									});
				}

				super.deploy();
		
		
	}

	public static void main(String[] args){
		
		try {
			CVM c = new CVM();
			c.startStandardLifeCycle(10000L);
			Thread.sleep(100000L);
			System.exit(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		/*
		try {
			CVM cvm = new CVM();
			cvm.startStandardLifeCycle(5000L);
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		
	}

}
