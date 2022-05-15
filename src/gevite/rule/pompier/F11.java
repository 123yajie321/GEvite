package gevite.rule.pompier;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFire;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFirefightingResource;
import gevite.evenement.EventBaseI;
import gevite.evenement.EventI;
import gevite.evenement.atomique.pompier.AlarmFeu;
import gevite.evenement.complexe.pompier.PremiereAlarmFeu;
import gevite.evenement.complexe.pompier.SecondAlarmFeu;
import gevite.interfaces.CorrelatorStateI;
import gevite.interfaces.PompierCorrelatorStateI;
import gevite.rule.RuleI;

public class F11 implements RuleI{

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
			
			
			return matchedEvents;
		} else {
			return null;
		}
	}

	@Override
	public boolean correlate(ArrayList<EventI> matchedEvents) {
		
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
		return pompierCorrelatorState.isCamionDisponible();
		
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
	
		PompierCorrelatorStateI pompierCorrelatorState = (PompierCorrelatorStateI) c;
		EventI alarmFeu = matchedEvents.get(0);
		pompierCorrelatorState.declancheSecondAlarme((AbsolutePosition) alarmFeu.getPropertyValue("position"));
		
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		ArrayList<EventI> eventComplex = matchedEvents;
		eb.removeEvent(matchedEvents.get(0));
		eb.removeEvent(matchedEvents.get(1));

		SecondAlarmFeu secondAlarmFeu = new SecondAlarmFeu(eventComplex);
		eb.addEvent(secondAlarmFeu);
		System.out.print(" F11 \n");

		
	}
	

}
