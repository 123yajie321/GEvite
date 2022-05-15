package gevite.rule.pompier;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;

import fr.sorbonne_u.cps.smartcity.SmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.descriptions.AbstractSmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFire;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFirefightingResource;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;

import gevite.evenement.EventBaseI;
import gevite.evenement.EventI;
import gevite.evenement.atomique.AtomicEvent;
import gevite.evenement.atomique.pompier.AlarmFeu;

import gevite.evenement.atomique.pompier.PompierDejaSollicite;
import gevite.evenement.atomique.pompier.PompierPlusPres;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.atomique.samu.SignaleManuel;
import gevite.evenement.complexe.pompier.DemandeInterventionFeu;
import gevite.evenement.complexe.pompier.PremiereAlarmFeu;
import gevite.evenement.complexe.pompier.SecondAlarmFeu;
import gevite.interfaces.CorrelatorStateI;
import gevite.interfaces.PompierCorrelatorStateI;
import gevite.rule.RuleI;

public class F12 implements RuleI{

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		
		EventI fa=null; EventI sa=null;
		for (int i = 0 ; i < eb.numberOfEvents() && (fa == null || sa==null ) ; i++) {
			EventI e = eb.getEvent(i);
			if (fa == null&&e instanceof AlarmFeu && e.hasProperty("type")&& e.hasProperty("position")
					&& e.getPropertyValue("type")== TypeOfFire.House
			) {
				fa = e;
			}
			
			if(fa!=null && e!=fa &&e instanceof AlarmFeu && e.hasProperty("type")&& e.hasProperty("position")
					&& e.getPropertyValue("type")== TypeOfFire.House
			) {
				sa = e;
			}
		}
		if(fa  != null && sa!= null) {
			ArrayList<EventI> matchedEvents = new ArrayList<>();
			matchedEvents.add(fa);
			matchedEvents.add(sa);
			
			//System.out.println("F11 matched");
			return matchedEvents;
		} else {
			return null;
		}
	}

	@Override
	public boolean correlate(ArrayList<EventI> matchedEvents) {
		//System.out.println("F11 correlate");
		return ((AbsolutePosition)matchedEvents.get(0).getPropertyValue("position")).equalAbsolutePosition((AbsolutePosition)matchedEvents.get(1).getPropertyValue("position"))
				&& matchedEvents.get(0).getTimeStamp().isBefore(
		                                        matchedEvents.get(1).getTimeStamp()) &&
		           matchedEvents.get(0).getTimeStamp().plus(
		                       Duration.of(15, ChronoUnit.MINUTES)).isAfter(
		                                        matchedEvents.get(1).getTimeStamp());
	}

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		//System.out.println("F11 filter");
		PompierCorrelatorStateI pompierCorrelatorState = (PompierCorrelatorStateI) c;
		return !pompierCorrelatorState.isCamionDisponible()&&pompierCorrelatorState.caserneNonSolliciteExiste(matchedEvents);
		
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		PompierCorrelatorStateI pompierState = (PompierCorrelatorStateI) c;
		ArrayList<String> pompierNonSolId = new ArrayList<String>();
		ArrayList<String> pomperDejaSolId = new ArrayList<String>();
		pomperDejaSolId.add(pompierState.getExecutorId());
		
		AtomicEvent pompierDejaSol=new PompierDejaSollicite();
		pompierDejaSol.putProperty("FireStationIdList", pomperDejaSolId );
		
		Iterator<String> fireStationsIditerator =
				SmartCityDescriptor.createFireStationIdIterator();
		while (fireStationsIditerator.hasNext()) {
			String fireStationId = fireStationsIditerator.next();
			if(!pomperDejaSolId.contains(fireStationId)) {
				pompierNonSolId.add(fireStationId);
			}
			
		}
		
		double minDistance = AbstractSmartCityDescriptor.distance(pompierState.getExecutorId(), pompierNonSolId.get(0)) ;
		String plusPreStation = pompierNonSolId.get(0);
		
		for(int i=1;i<pompierNonSolId.size();i++) {
			double tempD = AbstractSmartCityDescriptor.distance(pompierState.getExecutorId(), pompierNonSolId.get(i));
			if(tempD < minDistance) {
				minDistance = tempD;
				plusPreStation = pompierNonSolId.get(i);
			}
		}
		AtomicEvent pompierPlusPres = new PompierPlusPres();
		pompierPlusPres.putProperty("pluspresStation", plusPreStation);
		
		ArrayList<EventI> eventComplex = new ArrayList<EventI>() ;
		eventComplex.addAll(matchedEvents);
		eventComplex.add(pompierDejaSol);
		eventComplex.add(pompierPlusPres);

		SecondAlarmFeu secondAlarmFeu = new SecondAlarmFeu(eventComplex);
		
		pompierState.propagerEvent(secondAlarmFeu);
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		ArrayList<EventI> eventComplex = matchedEvents;
		eb.removeEvent(matchedEvents.get(0));
		eb.removeEvent(matchedEvents.get(1));

		SecondAlarmFeu secondAlarmFeu = new SecondAlarmFeu(eventComplex);
		eb.addEvent(secondAlarmFeu);
		System.out.print(" F12 \n");

		
	}
	

}
