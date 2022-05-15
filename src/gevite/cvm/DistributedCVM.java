package gevite.cvm;

import java.util.ArrayList;
import java.util.Iterator;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;
import fr.sorbonne_u.cps.smartcity.SmartCityDescriptor;
import gevite.CEPBus;
import gevite.facades.FireStation;
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
	}
	
	@Override
	public void instantiateAndPublish() throws Exception {
		
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
		
		
		ArrayList<String>samuCorrelateurAbonnement=new ArrayList<String>();
		ArrayList<String>fireSationCorrelateursAbonnement=new ArrayList<String>();
		ArrayList<String> fireStations = new ArrayList<String>();
		ArrayList<String> samus = new ArrayList<String>();
		
		
		if(AbstractDistributedCVM.getThisJVMURI().equals(JVM1_URI)) {
			AbstractComponent.createComponent(CEPBus.class.getCanonicalName(), new Object[] {BUSID1,BUSID2,CSIP1_URI,CERIP1_URI,REOBIP1_URI});
			
			Iterator<String> fireStationIdsIterator =
					SmartCityDescriptor.createFireStationIdIterator();
		while (fireStationIdsIterator.hasNext()) {
			String fireStationId = fireStationIdsIterator.next();
			abonnementCorrelateurTrafficLight.add(fireStationId);
			String notificationInboundPortURI = AbstractPort.generatePortURI();
			//this.register(fireStationId, notificationInboundPortURI);
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
			
			
		
		}}else if (AbstractDistributedCVM.getThisJVMURI().equals(JVM2_URI)) {
			AbstractComponent.createComponent(CEPBus.class.getCanonicalName(), new Object[] {BUSID2,BUSID1,CSIP2_URI,CERIP2_URI,REOBIP2_URI});
		}else {
			System.out.println("Unknow JVM URI : " + AbstractDistributedCVM.getThisJVMURI());
		}
		super.instantiateAndPublish();
		
	}
	
	@Override
	public void interconnect()throws Exception {
		super.interconnect();
		
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