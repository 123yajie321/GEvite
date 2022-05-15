package gevite.cvm;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;
import fr.sorbonne_u.cps.smartcity.SmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.components.FireStationProxy;
import fr.sorbonne_u.cps.smartcity.components.SAMUStationProxy;
import fr.sorbonne_u.cps.smartcity.components.TrafficLightProxy;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.traffic.components.TrafficLightsSimulator;
import fr.sorbonne_u.cps.smartcity.utils.TimeManager;
import gevite.CEPBus;
import gevite.correlateur.CorrelateurPompier;
import gevite.correlateur.CorrelateurSamu;
import gevite.correlateur.CorrelateurtTraffic;
import gevite.facades.FireStation;
import gevite.facades.Samu;
import gevite.facades.TrafficLight;
import gevite.rule.RuleBase;
import gevite.rule.circulation.C1;
import gevite.rule.pompier.F1;
import gevite.rule.pompier.F11;
import gevite.rule.pompier.F15;
import gevite.rule.pompier.F16;
import gevite.rule.pompier.F17;
import gevite.rule.pompier.F18;
import gevite.rule.pompier.F2;
import gevite.rule.pompier.F3;
import gevite.rule.pompier.F4;
import gevite.rule.samu.S1;
import gevite.rule.samu.S16;
import gevite.rule.samu.S17;
import gevite.rule.samu.S18;
import gevite.rule.samu.S19;
import gevite.rule.samu.S2;
import gevite.rule.samu.S3;
import gevite.rule.samu.S4;
import gevite.rule.samu.S5;
import gevite.rule.samu.S6;
import gevite.rule.samu.S7;
import gevite.rule.samu.S8;

public class DistributedCVM extends AbstractDistributedCVM{
	
	// -------------------------------------------------------------------------
		// Constants and variables
		// -------------------------------------------------------------------------

		public static final boolean	DEBUG = false;
		/** delay before the beginning of the smart city simulation after launching
		 *  the program.														*/
		protected static long		START_DELAY = 6000L;
		/** the start time of the simulation as a Java {@code LocalTime}.		*/
		protected static LocalTime simulatedStartTime;
		/** the end time of the simulation as a Java {@code LocalTime}.			*/
		protected static LocalTime simulatedEndTime;

		/** map that will contain the URI of the action inbound ports used
		 *  in proxy components to offer their services in the smart city
		 *  and the URI of notification inbound ports used by events emitter
		 *  components to receive the notifications from the smart city.	*/
		private	Map<String,String>				facadeNotificationInboundPortsURI;
		/** URI of the fire stations and SAMU centers inbound port used by the
		 *  traffic lights simulator to notify them of events concerning them.	*/
		protected final Map<String,String>					stationsEventIBPURI;
		/** URI of the traffic lights simulator inbound port used by the fire
		 *  stations and SAMU centers to execute the actions concerning them.	*/
		protected final Map<IntersectionPosition,String>	trafficLightsIBPURI;

	
	
	public static final String JVM1_URI="jvm1";
	public static final String JVM2_URI="jvm2";
	public static final String CSIP1_URI = "csip1-uri";		//uri of inbound port for management
	public static final String CERIP1_URI = "cerip1-uri";	//uri of inbound port to recieving evnt from emitter or correlateur
	public static final String CSIP2_URI = "csip2-uri";	
	public static final String CERIP2_URI = "cerip2-uri";
	public static final String REOBIP1_URI="reobip1-uri";//uri of inbound port to recieving evnt from other bus
	public static final String REOBIP2_URI="reobip2-uri";
	public static final String BUSID1="bus1";
	public static final String BUSID2="bus2";
	
	static int correlateurSamuid=1;
	static int correlateurPompierid=1;
	static int correlateurTrafficLight=1;
	static int trafficLightId=0;

	public DistributedCVM(String [] args) throws Exception {
		
		super(args);	
		
		// initialise the basic simulator smart city descriptor.
		SmartCityDescriptor.initialise();
		assert	simulatedStartTime != null && simulatedEndTime != null
				&& simulatedEndTime.isAfter(simulatedStartTime);
		long realTimeOfStart = System.currentTimeMillis() + START_DELAY;
		new TimeManager(realTimeOfStart, simulatedStartTime, simulatedEndTime);
		// create a map that will contain the URI of the notification inbound
		// ports used in event emitter components to receive the notifications
		// from the smart city.
		this.facadeNotificationInboundPortsURI = new HashMap<>();

		AbstractCVM.getThisJVMURI();

		this.stationsEventIBPURI = new HashMap<>();
		Iterator<String> iterStation =
							SmartCityDescriptor.createFireStationIdIterator();
		while (iterStation.hasNext()) {
			String id = iterStation.next();
			this.stationsEventIBPURI.put(id, AbstractPort.generatePortURI());
		}
		iterStation = SmartCityDescriptor.createSAMUStationIdIterator();
		while (iterStation.hasNext()) {
			stationsEventIBPURI.put(iterStation.next(),
									AbstractPort.generatePortURI());
		}

		this.trafficLightsIBPURI = new HashMap<>();
		Iterator<IntersectionPosition> iterTL =
				SmartCityDescriptor.createTrafficLightPositionIterator();
		while (iterTL.hasNext()) {
			this.trafficLightsIBPURI.put(iterTL.next(),
										 AbstractPort.generatePortURI());
		}
	}
	
	@Override
	public void instantiateAndPublish() throws Exception {
			
		
		
		if(AbstractDistributedCVM.getThisJVMURI().equals(JVM1_URI)) {
			AbstractComponent.createComponent(CEPBus.class.getCanonicalName(), new Object[] {BUSID1,CSIP1_URI,CERIP1_URI,REOBIP1_URI,REOBIP2_URI});
			RuleBase ruleBaseSamu = new RuleBase();
			S1 S1 = new S1();
			S2 S2 = new S2();
			S3 S3 = new S3();
			S4 S4 = new S4();
			S5 S5 = new S5();
			S6 S6 = new S6();
			S7 S7 = new S7();
			S8 S8 = new S8();
			S16 S16= new S16();
			S17 S17 = new S17();
			S18 S18 = new S18();
			S19 S19 = new S19();

			ruleBaseSamu.addRule(S1);
			ruleBaseSamu.addRule(S2);
			ruleBaseSamu.addRule(S3);
			ruleBaseSamu.addRule(S4);
			ruleBaseSamu.addRule(S5);
			ruleBaseSamu.addRule(S6);
			ruleBaseSamu.addRule(S7);
			ruleBaseSamu.addRule(S8);
			ruleBaseSamu.addRule(S16);
			ruleBaseSamu.addRule(S17);
			ruleBaseSamu.addRule(S18);
			ruleBaseSamu.addRule(S19);
			
			RuleBase ruleBasePompier = new RuleBase();
			F1 f1 = new F1();
			F2 f2 = new F2();
			F3 f3 = new F3();
			F4 f4 = new F4();
			F11 f11 = new F11();
			F15 f15 = new F15();
			F16 f16= new F16();
			F17 f17 = new F17();
			F18 f18 = new F18();
			
			ruleBasePompier.addRule(f1);
			ruleBasePompier.addRule(f2);
			ruleBasePompier.addRule(f3);
			ruleBasePompier.addRule(f4);
			ruleBasePompier.addRule(f11);
			ruleBasePompier.addRule(f15);
			ruleBasePompier.addRule(f16);
			ruleBasePompier.addRule(f17);
			ruleBasePompier.addRule(f18);
			ArrayList<String>samuCorrelateurAbonnement=new ArrayList<String>();
			ArrayList<String>fireSationCorrelateursAbonnement=new ArrayList<String>();
			ArrayList<String> fireStations = new ArrayList<String>();
			ArrayList<String> samus = new ArrayList<String>();
			
			
			
			

			Iterator<String> fireStationIdsIterator =
						SmartCityDescriptor.createFireStationIdIterator();
			while (fireStationIdsIterator.hasNext()) {
				String fireStationId = fireStationIdsIterator.next();
				//abonnementCorrelateurTrafficLight.add(fireStationId);
				String notificationInboundPortURI = AbstractPort.generatePortURI();
				this.register(fireStationId, notificationInboundPortURI);
				//pompier
				AbstractComponent.createComponent(
					FireStation.class.getCanonicalName(),
					new Object[]{
							notificationInboundPortURI,
							fireStationId,
							SmartCityDescriptor.
											getActionInboundPortURI(fireStationId),
											CSIP1_URI
							});
				
				String correlateurId="correlateurPompier"+correlateurPompierid;
				correlateurPompierid++;
				fireSationCorrelateursAbonnement.add(correlateurId);
				fireStations.add(fireStationId);
				
				
			}
			
			
			for(int i=0;i<correlateurPompierid-1;i++) {
				int id=i+1;
				String correlateurId="correlateurPompier"+id;
				 ArrayList<String> abonnement=new ArrayList<>();
				   abonnement.addAll(fireSationCorrelateursAbonnement);
				   abonnement.add(fireStations.get(i));
				//correlateur pompier
				AbstractComponent.createComponent(CorrelateurPompier.class.getCanonicalName(), 
						new Object[]{
								correlateurId,
								fireStations.get(i),
								abonnement,
								ruleBasePompier,
								CSIP1_URI

						});
			}
			
			
			
			Iterator<String> samuStationsIditerator =
					SmartCityDescriptor.createSAMUStationIdIterator();
		while (samuStationsIditerator.hasNext()) {
			String samuStationId = samuStationsIditerator.next();
			//abonnementCorrelateurTrafficLight.add(samuStationId);
			String notificationInboundPortURI = AbstractPort.generatePortURI();
			this.register(samuStationId, notificationInboundPortURI);
			//samu
			AbstractComponent.createComponent(
					Samu.class.getCanonicalName(),
					new Object[]{
							notificationInboundPortURI,
							samuStationId,
							SmartCityDescriptor.
										getActionInboundPortURI(samuStationId),CSIP1_URI
							});
			String correlateurId="correlateurSamu"+correlateurSamuid;
			correlateurSamuid++;
			samus.add(samuStationId);
			samuCorrelateurAbonnement.add(correlateurId);
		
		}
		
		for(int i=0;i<correlateurSamuid-1;i++) {
			int id=i+1;
			String correlateurId="correlateurSamu"+id;
		   ArrayList<String> abonnement=new ArrayList<>();
		   abonnement.addAll(samuCorrelateurAbonnement);
		   abonnement.add(samus.get(i));
		
			AbstractComponent.createComponent(CorrelateurSamu.class.getCanonicalName(), 
					new Object[]{
							correlateurId,
							samus.get(i),
							abonnement,
							ruleBaseSamu,CSIP1_URI
					});
		}
		
		AbstractComponent.createComponent(
				TrafficLightsSimulator.class.getCanonicalName(),
				new Object[]{this.stationsEventIBPURI,
							 this.trafficLightsIBPURI});

		Iterator<String> iterStation =
						SmartCityDescriptor.createFireStationIdIterator();
		while (iterStation.hasNext()) {
			String id = iterStation.next();
			AbstractComponent.createComponent(
					FireStationProxy.class.getCanonicalName(),
					new Object[]{
							SmartCityDescriptor.getActionInboundPortURI(id),
							this.facadeNotificationInboundPortsURI.get(id),
							id,
							SmartCityDescriptor.getPosition(id),
							this.stationsEventIBPURI.get(id),
							2,
							2
							});
		}

		iterStation = SmartCityDescriptor.createSAMUStationIdIterator();
		while (iterStation.hasNext()) {
			String id = iterStation.next();
			AbstractComponent.createComponent(
					SAMUStationProxy.class.getCanonicalName(),
					new Object[]{
							SmartCityDescriptor.getActionInboundPortURI(id),
							this.facadeNotificationInboundPortsURI.get(id),
							id,
							SmartCityDescriptor.getPosition(id),
							this.stationsEventIBPURI.get(id),
							2,
							2
							});
		}
		
		
			
		
		}else if (AbstractDistributedCVM.getThisJVMURI().equals(JVM2_URI)) {
			
			RuleBase ruleBaseTrafficLight = new RuleBase();
			C1 c1 = new C1();
			ruleBaseTrafficLight.addRule(c1);
			
			//stoker la liste des emitters que le correlateur traffic veut subscribe
			ArrayList<String>abonnementCorrelateurTrafficLight=new ArrayList<String>();
			
			//stcoker id des TrafficLight
			ArrayList<String> trafficLightIdList = new ArrayList<String>();
			AbstractComponent.createComponent(CEPBus.class.getCanonicalName(), new Object[] {BUSID2,CSIP2_URI,CERIP2_URI,REOBIP2_URI,REOBIP1_URI});
			

			Iterator<IntersectionPosition> trafficLightsIterator =
						SmartCityDescriptor.createTrafficLightPositionIterator();
			while (trafficLightsIterator.hasNext()) {
				IntersectionPosition p = trafficLightsIterator.next();
				String notificationInboundPortURI = AbstractPort.generatePortURI();
				this.register(p.toString(), notificationInboundPortURI);
				String trafficId = "trafficLight "+ trafficLightId;
				trafficLightIdList.add(trafficId);
				
				//trafficLight
				AbstractComponent.createComponent(
						TrafficLight.class.getCanonicalName(),
						new Object[]{
								notificationInboundPortURI,
								p,
								SmartCityDescriptor.
													getActionInboundPortURI(p),
								trafficId,CSIP1_URI
								});
				trafficLightId++;
			}
				
			
			
			for(int i=0;i<trafficLightId;i++) {
				
				String correlateurId="correlateurTrafficLight "+correlateurTrafficLight;
				correlateurTrafficLight++;
					
				 ArrayList<String> abonnement=new ArrayList<>();
				 abonnement.addAll(abonnementCorrelateurTrafficLight);
				 abonnement.add(trafficLightIdList.get(i));
				AbstractComponent.createComponent(CorrelateurtTraffic.class.getCanonicalName(), 
						new Object[]{
								correlateurId,
								trafficLightIdList.get(i),
								abonnement,
								ruleBaseTrafficLight,CSIP1_URI
						});
			}
			

			Iterator<IntersectionPosition> trafficLightsIterator2 =
						SmartCityDescriptor.createTrafficLightPositionIterator();
			while (trafficLightsIterator2.hasNext()) {
				IntersectionPosition p = trafficLightsIterator2.next();
				AbstractComponent.createComponent(
						TrafficLightProxy.class.getCanonicalName(),
						new Object[]{
								p,
								SmartCityDescriptor.getActionInboundPortURI(p),
								this.facadeNotificationInboundPortsURI.
									 							get(p.toString()),
								this.trafficLightsIBPURI.get(p)
								});
			}
			
		
		
		}else {
			System.out.println("Unknow JVM URI : " + AbstractDistributedCVM.getThisJVMURI());
		}
		super.instantiateAndPublish();
		
	}
	
	@Override
	public void interconnect()throws Exception {
		super.interconnect();
		
	}
	
	
	// -------------------------------------------------------------------------
		// Methods
		// -------------------------------------------------------------------------

		/**
		 * return true if the asset has already a URI registered, false otherwise.
		 * 
		 * <p><strong>Contract</strong></p>
		 * 
		 * <pre>
		 * pre	{@code assetId != null && !assetId.isEmpty()}
		 * post	true		// no postcondition.
		 * </pre>
		 *
		 * @param assetId	asset identifier as define the the smart city descriptor.
		 * @return			true if the asset has already a URI registered, false otherwise.
		 */
		protected boolean	registered(String assetId)
		{
			assert	assetId != null && !assetId.isEmpty();
			return this.facadeNotificationInboundPortsURI.containsKey(assetId);
		}

		/**
		 * register the URI if the notification inbound port used in the events
		 * emitter component associated with the asset identifier {@code assetId}.
		 * 
		 * <p><strong>Contract</strong></p>
		 * 
		 * <pre>
		 * pre	{@code assetId != null && !assetId.isEmpty()}
		 * pre	{@code !registered(assetId)}
		 * pre	{@code uri != null && !uri.isEmpty()}
		 * post	{@code registered(assetId)}
		 * </pre>
		 *
		 * @param assetId	asset identifier as define the the smart city descriptor.
		 * @param uri		URI of the notification inbound port of the corresponding events emitter component.
		 */
		protected void		register(String assetId, String uri)
		{
			assert	assetId != null && !assetId.isEmpty();
			assert	!this.registered(assetId);
			assert	uri != null && !uri.isEmpty();
			this.facadeNotificationInboundPortsURI.put(assetId, uri);
		}

	
	
	
	
	
	public static void main(String[] args) {
		
		try {
			DistributedCVM c = new DistributedCVM(args);
			c.startStandardLifeCycle(10000L);
			System.exit(0);
		} catch (Exception e) {
		
			e.printStackTrace();
		}

		
	}
	
	
	
	




}
