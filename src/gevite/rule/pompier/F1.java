package gevite.rule.pompier;

import java.util.ArrayList;

import gevite.correlateur.CorrelatorStateI;
import gevite.correlateur.PompierCorrelatorStateI;
import gevite.evenement.EventBaseI;
import gevite.evenement.EventI;
import gevite.evenement.atomique.pompier.AlarmFeu;
import gevite.evenement.atomique.pompier.AlarmeFeuCause;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.complexe.pompier.PremiereAlarmFeu;
import gevite.rule.RuleI;

public class F1 implements RuleI{

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		EventI af=null;
		for (int i = 0 ; i < eb.numberOfEvents() && (af == null ) ; i++) {
			EventI e = eb.getEvent(i);
			if (e instanceof AlarmFeu && e.hasProperty("type")&& e.hasProperty("position")
					&& ((String)e.getPropertyValue("type")).equals("immeuble")
					&&((String)e.getPropertyValue("position")).equals("p")) {
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
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		PompierCorrelatorStateI pompierCorrelatorState = (PompierCorrelatorStateI) c;
		return pompierCorrelatorState.inZone("p") && pompierCorrelatorState.isEchelleDisponible();
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		PompierCorrelatorStateI pompierCorrelatorState = (PompierCorrelatorStateI) c;
		pompierCorrelatorState.declancheAlarme();;		
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		AlarmeFeuCause alarmeFeuCause = new AlarmeFeuCause();
		eb.removeEvent(matchedEvents.get(0));
		matchedEvents.add(alarmeFeuCause);
		PremiereAlarmFeu premiereAlarmFeu = new PremiereAlarmFeu(matchedEvents);
		eb.addEvent(premiereAlarmFeu);
		
	}
	

}
