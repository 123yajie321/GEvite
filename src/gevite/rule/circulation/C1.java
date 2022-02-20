package gevite.rule.circulation;

import java.util.ArrayList;

import gevite.correlateur.CorrelatorStateI;
import gevite.correlateur.HealthCorrelatorStateI;
import gevite.correlateur.CirculationCorrelatorStateI;
import gevite.evenement.EventBaseI;
import gevite.evenement.EventI;
import gevite.evenement.atomique.circulation.DemandePriorite;
import gevite.evenement.atomique.circulation.PassageVehicule;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.complexe.circulation.AttentePassageComplexe;
import gevite.rule.RuleI;

public class C1 implements RuleI{

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		
		EventI dp=null;
		for (int i = 0 ; i < eb.numberOfEvents() && (dp == null ) ; i++) {
			EventI e = eb.getEvent(i);
			if (e instanceof DemandePriorite && e.hasProperty("priorite")&& e.hasProperty("vehicule")&& e.hasProperty("destinationF")
					&& ((String)e.getPropertyValue("priorite")).equals("p")
					&&((String)e.getPropertyValue("vehicule")).equals("v")
					&&((String)e.getPropertyValue("destinationF")).equals("df")) {
				dp = e;
			}
		}	
			if(dp != null ) {
				ArrayList<EventI> matchedEvents = new ArrayList<>();
				matchedEvents.add(dp);
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
		return true;
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		CirculationCorrelatorStateI circulationState = (CirculationCorrelatorStateI)c;
		circulationState.passerIntersectionP(matchedEvents.get(0).getPropertyValue("priorite"));
		
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		PassageVehicule pVehicule = new PassageVehicule();
		eb.removeEvent(matchedEvents.get(0));
		matchedEvents.add(pVehicule);
		AttentePassageComplexe attentePassageComplexe = new AttentePassageComplexe(matchedEvents);
		eb.addEvent(attentePassageComplexe);
		
	}

}
