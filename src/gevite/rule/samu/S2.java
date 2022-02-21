package gevite.rule.samu;

import java.util.ArrayList;

import gevite.correlateur.CorrelatorStateI;
import gevite.correlateur.HealthCorrelatorStateI;
import gevite.evenement.EventBaseI;
import gevite.evenement.EventI;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.atomique.samu.InterventionCause;
import gevite.evenement.complexe.samu.DemandeIntervention;
import gevite.rule.RuleI;

public class S2 implements RuleI {

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		EventI as=null;
		for (int i = 0 ; i < eb.numberOfEvents() && (as == null ) ; i++) {
			EventI e = eb.getEvent(i);
			if (e instanceof AlarmeSante && e.hasProperty("type")&& e.hasProperty("position")
					&& ((String)e.getPropertyValue("type")).equals("urgence")
					&&((String)e.getPropertyValue("position")).equals("p")) {
				as = e;
			}
		}	
			if(as  != null ) {
				ArrayList<EventI> matchedEvents = new ArrayList<>();
				matchedEvents.add(as);
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
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI cs) {
		HealthCorrelatorStateI samuState = (HealthCorrelatorStateI)cs;
		return samuState.inZone("p")&& samuState.isNotAmbulanceAvailable();
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		InterventionCause iCause = new InterventionCause();
		ArrayList<EventI> eventComplex = matchedEvents; 
		eventComplex.add(iCause);
		DemandeIntervention dIntervention = new DemandeIntervention(eventComplex);
		HealthCorrelatorStateI samuState = (HealthCorrelatorStateI)c;
		samuState.intervanetionAmbulance();

	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		eb.removeEvent(matchedEvents.get(0));
	}

}
