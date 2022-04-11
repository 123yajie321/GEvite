package gevite.cep.cvm;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.cps.smartcity.AbstractSmartCityCVM;
import fr.sorbonne_u.cps.smartcity.SmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.utils.TimeManager;
import gevite.cep.CEPBusManagementCI;
import gevite.cepbus.CEPBus;
import gevite.connector.ConnectorCorrelateurCepServices;
import gevite.connector.ConnectorEmitterRegister;
import gevite.correlateur.CorrelateurPompier;
import gevite.correlateur.CorrelateurSamu;
import gevite.correlateur.CorrelateurtTraffic;
import gevite.fireStation.FireStation;
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
import gevite.samu.Samu;
import gevite.traffic.TrafficLight;

public class CVM extends AbstractSmartCityCVM {
	static int correlateurSamuid=1;
	static int correlateurPompierid=1;
	static int correlateurTrafficLight=1;
	static int trafficLightId=0;
	
	
	public CVM() throws Exception {
		super();
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
		
		// create RuleBase
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
		
		RuleBase ruleBaseTrafficLight = new RuleBase();
		C1 c1 = new C1();
		ruleBaseTrafficLight.addRule(c1);
		
		//stoker la liste des emitters que le correlateur traffic veut subscribe
		ArrayList<String>abonnementCorrelateurTrafficLight=new ArrayList<String>();
		
		//stcoker id des TrafficLight
		ArrayList<String> trafficLightIdList = new ArrayList<String>();
		
		

		//create CEPBus
		AbstractComponent.createComponent(CEPBus.class.getCanonicalName(), new Object[] {});


		ArrayList<String>samuCorrelateurAbonnement=new ArrayList<String>();
		ArrayList<String>fireSationCorrelateursAbonnement=new ArrayList<String>();
		ArrayList<String> fireStations = new ArrayList<String>();
		ArrayList<String> samus = new ArrayList<String>();
		
		//String correlateurURI = AbstractComponent.createComponent(CorrelateurSamu.class.getCanonicalName(), new Object[] {});
		
		

				Iterator<String> fireStationIdsIterator =
							SmartCityDescriptor.createFireStationIdIterator();
				while (fireStationIdsIterator.hasNext()) {
					String fireStationId = fireStationIdsIterator.next();
					abonnementCorrelateurTrafficLight.add(fireStationId);
					String notificationInboundPortURI = AbstractPort.generatePortURI();
					this.register(fireStationId, notificationInboundPortURI);
					//pompier
					AbstractComponent.createComponent(
						FireStation.class.getCanonicalName(),
						new Object[]{
								notificationInboundPortURI,
								fireStationId,
								SmartCityDescriptor.
												getActionInboundPortURI(fireStationId)
								});
					
					String correlateurId="correlateurPompier"+correlateurPompierid;
					correlateurPompierid++;
					fireSationCorrelateursAbonnement.add(correlateurId);
					fireStations.add(fireStationId);
					fireSationCorrelateursAbonnement.add(fireStationId);
					//correlateur pompier
					
				}
				
				for(int i=0;i<correlateurPompierid-1;i++) {
					int id=i+1;
					String correlateurId="correlateurPompier"+id;
					ArrayList<String>tmp=new ArrayList<>();
					tmp.add(fireStations.get(i));
					//correlateur pompier
					AbstractComponent.createComponent(CorrelateurPompier.class.getCanonicalName(), 
							new Object[]{
									correlateurId,
									tmp,
									fireSationCorrelateursAbonnement,
									ruleBasePompier
							});
				}
				
				

				Iterator<String> samuStationsIditerator =
							SmartCityDescriptor.createSAMUStationIdIterator();
				while (samuStationsIditerator.hasNext()) {
					String samuStationId = samuStationsIditerator.next();
					abonnementCorrelateurTrafficLight.add(samuStationId);
					String notificationInboundPortURI = AbstractPort.generatePortURI();
					this.register(samuStationId, notificationInboundPortURI);
					//samu
					AbstractComponent.createComponent(
							Samu.class.getCanonicalName(),
							new Object[]{
									notificationInboundPortURI,
									samuStationId,
									SmartCityDescriptor.
												getActionInboundPortURI(samuStationId)
									});
					String correlateurId="correlateurSamu"+correlateurSamuid;
					correlateurSamuid++;
					samus.add(samuStationId);
					samuCorrelateurAbonnement.add(correlateurId);
					samuCorrelateurAbonnement.add(samuStationId);
				
				}
				
				for(int i=0;i<correlateurSamuid-1;i++) {
					int id=i+1;
					String correlateurId="correlateurSamu"+id;

					//correlateur sa,u
					AbstractComponent.createComponent(CorrelateurSamu.class.getCanonicalName(), 
							new Object[]{
									correlateurId,
									samus.get(i),
									samuCorrelateurAbonnement,
									ruleBaseSamu
							});
				}
				
				
				Iterator<IntersectionPosition> trafficLightsIterator =
							SmartCityDescriptor.createTrafficLightPositionIterator();
				while (trafficLightsIterator.hasNext()) {
					IntersectionPosition p = trafficLightsIterator.next();
					String notificationInboundPortURI = AbstractPort.generatePortURI();
					this.register(p.toString(), notificationInboundPortURI);
					String trafficId = "trafficLight "+ trafficLightId;
					trafficLightIdList.add(trafficId);
					abonnementCorrelateurTrafficLight.add(trafficId);
					
					//trafficLight
					AbstractComponent.createComponent(
							TrafficLight.class.getCanonicalName(),
							new Object[]{
									notificationInboundPortURI,
									p,
									SmartCityDescriptor.
														getActionInboundPortURI(p),
									trafficId
									});
					trafficLightId++;
				}
				
				
				for(int i=0;i<trafficLightId;i++) {
					
					String correlateurId="correlateurTrafficLight "+correlateurTrafficLight;
					correlateurTrafficLight++;
						
					ArrayList<String>trafficLight=new ArrayList<String>();
					trafficLight.add(trafficLightIdList.get(i));
					AbstractComponent.createComponent(CorrelateurtTraffic.class.getCanonicalName(), 
							new Object[]{
									correlateurId,
									trafficLight,
									abonnementCorrelateurTrafficLight,
									ruleBaseTrafficLight
							});
				}
				super.deploy();
		
		
	}

	public static void main(String[] args){
		
		try {
			// start time, in the logical time view; the choice is arbitrary
						simulatedStartTime = LocalTime.of(12, 0);
						// end time, in the logical time view; the chosen value must allow
						// the whole test scenario to be executed within the logical time
						// period between the start and the end times; the actual duration
						// of the program execution also depends upon the acceleration
						// factor defined in the class TimeManager
						simulatedEndTime = LocalTime.of(12, 0).plusMinutes(30);
						CVM c = new CVM();
						// start the program execution which duration includes a simulation
						// start delay to allow for the interconnection of components and
						// then the duration of the simulation itself computed from the
						// start time, the end time and the acceleration factor
						c.startStandardLifeCycle(
								START_DELAY + TimeManager.get().computeExecutionDuration());
						// delay after the execution during which the widows opened by
						// components remain visible
						Thread.sleep(10000L);
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
