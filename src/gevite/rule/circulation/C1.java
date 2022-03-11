package gevite.rule.circulation;

import java.util.ArrayList;

import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;
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

public class C1 implements RuleI{

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		
		EventI dp=null;
		for (int i = 0 ; i < eb.numberOfEvents() && (dp == null ) ; i++) {
			EventI e = eb.getEvent(i);
			if (e instanceof DemandePriorite && e.hasProperty("interPosition") && e.hasProperty("priority")
					&& e.hasProperty("vehicleId")&& e.hasProperty("destination")) {
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
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		CirculationCorrelatorStateI circulationState = (CirculationCorrelatorStateI)c;
		circulationState.changePriority((TypeOfTrafficLightPriority) matchedEvents.get(0).getPropertyValue("priority"));
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		AttentePassage ap = new AttentePassage();
		ap.putProperty("vehicleId", matchedEvents.get(0).getPropertyValue("vehicleId"));
		eb.removeEvent(matchedEvents.get(0));
		matchedEvents.add(ap);
		AttentePassageComplexe attentePassageComplexe = new AttentePassageComplexe(matchedEvents);
		eb.addEvent(attentePassageComplexe);
		
	}

}
