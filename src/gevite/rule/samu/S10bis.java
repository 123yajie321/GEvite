package gevite.rule.samu;

import java.util.ArrayList;

import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import gevite.correlateur.CorrelatorStateI;
import gevite.correlateur.SamuCorrelatorStateI;
import gevite.evenement.EventBaseI;
import gevite.evenement.EventI;
import gevite.evenement.complexe.samu.DemandeInterventionSamu;
import gevite.rule.RuleI;

public class S10bis implements RuleI  {

	@Override
	public ArrayList<EventI> match(EventBaseI eb) throws Exception {
		EventI dIntervention=null;
		for (int i = 0 ; i < eb.numberOfEvents() && (dIntervention == null ) ; i++) {
			EventI e = eb.getEvent(i);
			if (e instanceof DemandeInterventionSamu && e.hasProperty("type")
					&& e.getPropertyValue("type")==TypeOfHealthAlarm.EMERGENCY
					) {
				dIntervention = e;
			}
		}	
			if(dIntervention  != null ) {
				ArrayList<EventI> matchedEvents = new ArrayList<>();
				matchedEvents.add(dIntervention);
				return matchedEvents;
			} else {
				return null;
			}
	}

	@Override
	public boolean correlate(ArrayList<EventI> matchedEvents) throws Exception {
		return true;
	}

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		SamuCorrelatorStateI samuState = (SamuCorrelatorStateI)c;
		return !samuState.isAmbulanceAvailable();
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
