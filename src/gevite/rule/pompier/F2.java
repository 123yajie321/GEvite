package gevite.rule.pompier;

import java.util.ArrayList;

import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFire;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFirefightingResource;
import gevite.evenement.EventBaseI;
import gevite.evenement.EventI;
import gevite.evenement.atomique.pompier.AlarmFeu;
import gevite.evenement.complexe.pompier.PremiereAlarmFeu;
import gevite.interfaces.CorrelatorStateI;
import gevite.interfaces.PompierCorrelatorStateI;
import gevite.rule.RuleI;

public class F2 implements RuleI{

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		EventI af=null;
		for (int i = 0 ; i < eb.numberOfEvents() && (af == null ) ; i++) {
			EventI e = eb.getEvent(i);
			if (e instanceof AlarmFeu && e.hasProperty("type")&& e.hasProperty("position")
					&& e.getPropertyValue("type")== TypeOfFire.House
			) {
				af = e;
			}
		}
		if(af  != null ) {
			ArrayList<EventI> matchedEvents = new ArrayList<>();
			matchedEvents.add(af);
			return matchedEvents;
		} else {
			return null;
		}
			
		
	}

	@Override
	public boolean correlate(ArrayList<EventI> matchedEvents) {
		return true;
	}

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {

		PompierCorrelatorStateI pompierCorrelatorState = (PompierCorrelatorStateI) c;
		EventI alarmFeu = matchedEvents.get(0);
		return pompierCorrelatorState.inZone((AbsolutePosition)alarmFeu.getPropertyValue("position")) && pompierCorrelatorState.isCamionDisponible();
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		PompierCorrelatorStateI pompierCorrelatorState = (PompierCorrelatorStateI) c;
		EventI alarmFeu = matchedEvents.get(0);
		pompierCorrelatorState.declancheFirstAlarme((AbsolutePosition) alarmFeu.getPropertyValue("position"), TypeOfFirefightingResource.StandardTruck);
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		/*
		EventI alarmeFeuCause = new AlarmeFeuCause();
		ArrayList<EventI> eventComplex = matchedEvents;
		eventComplex.add(alarmeFeuCause);
		eb.removeEvent(matchedEvents.get(0));
		PremiereAlarmFeu premiereAlarmFeu = new PremiereAlarmFeu(eventComplex);
		eb.addEvent(premiereAlarmFeu);
		*/
		eb.removeEvent(matchedEvents.get(0));
		PremiereAlarmFeu premiereAlarmFeu = new PremiereAlarmFeu(matchedEvents);
		eb.addEvent(premiereAlarmFeu);
		System.out.print(" F2 \n");
		
	}
	

}
