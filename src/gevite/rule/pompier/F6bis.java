package gevite.rule.pompier;

import java.util.ArrayList;

import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFire;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFirefightingResource;
import gevite.evenement.EventBaseI;
import gevite.evenement.EventI;
import gevite.evenement.atomique.pompier.AlarmFeu;
import gevite.evenement.atomique.pompier.PompierPlusPres;
import gevite.evenement.complexe.pompier.DemandeInterventionFeu;
import gevite.evenement.complexe.pompier.PremiereAlarmFeu;
import gevite.interfaces.CorrelatorStateI;
import gevite.interfaces.PompierCorrelatorStateI;
import gevite.rule.RuleI;

public class F6bis implements RuleI{

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		EventI dIntervention=null;
		for (int i = 0 ; i < eb.numberOfEvents() && (dIntervention == null ) ; i++) {
			EventI e = eb.getEvent(i);
			if (e instanceof DemandeInterventionFeu && ((DemandeInterventionFeu) e).getCorrelatedEvents().get(0).hasProperty("type")&& ((DemandeInterventionFeu) e).getCorrelatedEvents().get(0).hasProperty("position")
					&& ((DemandeInterventionFeu) e).getCorrelatedEvents().get(0).getPropertyValue("type")== TypeOfFire.Building
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
	public boolean correlate(ArrayList<EventI> matchedEvents) {
		return true;
	}

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		PompierCorrelatorStateI pompierCorrelatorState = (PompierCorrelatorStateI) c;
		DemandeInterventionFeu dInterventionPompier = (DemandeInterventionFeu)matchedEvents.get(0);
		ArrayList<EventI> correlateEvents = dInterventionPompier.getCorrelatedEvents();
		for(int i = 0; i< correlateEvents.size();i++) {
			if(correlateEvents.get(i) instanceof PompierPlusPres) {
				if(correlateEvents.get(i).getPropertyValue("pluspresStation").equals(pompierCorrelatorState.getExecutorId())) {
					return !pompierCorrelatorState.isEchelleDisponible()&&!pompierCorrelatorState.caserneNonSolliciteExiste(matchedEvents);
				}
				
			}
		}
		return false;
	
	
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		eb.removeEvent(matchedEvents.get(0));
		System.out.print(" F6bis \n");
	}

}
