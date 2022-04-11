package gevite.rule.samu;

import java.util.ArrayList;

import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import gevite.correlateur.CorrelatorStateI;
import gevite.correlateur.SamuCorrelatorStateI;
import gevite.evenement.EventBaseI;
import gevite.evenement.EventI;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.complexe.samu.DemandeInterventionSamu;
import gevite.rule.RuleI;

public class S9 implements RuleI {

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
		return samuState.isAmbulanceAvailable();
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		SamuCorrelatorStateI samuState = (SamuCorrelatorStateI)c;
		DemandeInterventionSamu demandeInterventionSamu=(DemandeInterventionSamu) matchedEvents.get(0);
		AlarmeSante aSante = (AlarmeSante) (demandeInterventionSamu.getCorrelatedEvents().get(0));
		samuState.intervanetionAmbulance((AbsolutePosition)aSante.getPropertyValue("position"), 
			null, TypeOfSAMURessources.AMBULANCE);
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) throws Exception {
		eb.removeEvent(matchedEvents.get(0));
		System.out.println("S9 \n");	}

}
