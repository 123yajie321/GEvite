package gevite.rule.circulation;

import java.io.Serializable;
import java.util.ArrayList;

import gevite.correlateur.CorrelatorStateI;
import gevite.correlateur.SamuCorrelatorStateI;
import gevite.correlateur.CirculationCorrelatorStateI;
import gevite.evenement.EventBaseI;
import gevite.evenement.EventI;
import gevite.evenement.atomique.circulation.AttentePassage;
import gevite.evenement.atomique.circulation.DemandePriorite;
import gevite.evenement.atomique.circulation.PassageVehicule;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.complexe.circulation.AttentePassageComplexe;
import gevite.rule.RuleI;

public class C3 implements RuleI{

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		
		EventI ap=null;	EventI pv = null;

		for (int i = 0 ; i < eb.numberOfEvents() && (ap == null || pv == null) ; i++) {
			EventI e = eb.getEvent(i);
			if(e instanceof AttentePassage 
					&&e.hasProperty("vehicule")&&e.hasProperty("direction")&&e.hasProperty("destinationF")
					&&e.getPropertyValue("vehicule").equals("v")&&e.getPropertyValue("direction").equals("d")&&e.getPropertyValue("destinationF").equals("df")) {
				ap = e;
			}
			if(e instanceof PassageVehicule
					&& e.hasProperty("vehicule")&&e.hasProperty("direction")
					&& e.getPropertyValue("vehicule").equals("v")&&e.getPropertyValue("direction").equals("d")) {
				pv = e;
			}
			
		}	
		if(ap != null && pv != null ) {
			ArrayList<EventI> matchedEvents = new ArrayList<>();
			matchedEvents.add(ap);
			matchedEvents.add(pv);
			return matchedEvents;
		} else {
			return null;
		}
			
	}

	@Override
	public boolean correlate(ArrayList<EventI> matchedEvents) {
		return matchedEvents.get(0).hasProperty("vehicule")
				&& matchedEvents.get(1).hasProperty("vehicule")
				&& matchedEvents.get(0).getPropertyValue("vehicule").equals(matchedEvents.get(1).getPropertyValue("vehicule"));
	}

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		CirculationCorrelatorStateI circulationState = (CirculationCorrelatorStateI)c;
		Serializable s = matchedEvents.get(0).getPropertyValue("destinationF");
		return circulationState.estApresDestination(s);
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		CirculationCorrelatorStateI circulationState = (CirculationCorrelatorStateI)c;
		circulationState.passerIntersectionN(matchedEvents.get(0).getPropertyValue("priorite"));//probleme
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		eb.removeEvent(matchedEvents.get(0));
	    eb.removeEvent(matchedEvents.get(1));
		
	}

}
