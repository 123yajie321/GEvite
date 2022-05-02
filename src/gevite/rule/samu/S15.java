package gevite.rule.samu;

import java.util.ArrayList;

import gevite.correlateur.CorrelatorStateI;
import gevite.correlateur.SamuCorrelatorStateI;
import gevite.evenement.EventBaseI;
import gevite.evenement.EventI;
import gevite.evenement.atomique.samu.SamuPlusPres;
import gevite.evenement.complexe.samu.ConsciousFall;
import gevite.evenement.complexe.samu.DemandeInterventionSamu;
import gevite.rule.RuleI;

public class S15 implements RuleI {

	@Override
	public ArrayList<EventI> match(EventBaseI eb) throws Exception {
		EventI chuteP=null;
		for (int i = 0 ; i < eb.numberOfEvents() && (chuteP == null ) ; i++) {
			EventI e = eb.getEvent(i);
			if (e instanceof ConsciousFall) {
				chuteP = e;
			}
		}	
			if(chuteP  != null ) {
				ArrayList<EventI> matchedEvents = new ArrayList<>();
				matchedEvents.add(chuteP);
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
		ConsciousFall consciousFall = (ConsciousFall)matchedEvents.get(0);
		ArrayList<EventI> correlateEvents = consciousFall.getCorrelatedEvents();
		for(int i = 0; i< correlateEvents.size();i++) {
			if(correlateEvents.get(i) instanceof SamuPlusPres) {
				if(correlateEvents.get(i).getPropertyValue("pluspresStation").equals(samuState.getExecutorId())) {
					return !samuState.isMedicAvailable()&&!samuState.samuNonSolliciteExiste(matchedEvents);
				}
				
			}
		}
		return false;
		/*
		SamuCorrelatorStateI samuState = (SamuCorrelatorStateI)c;
		return !samuState.isMedicAvailable()&&!samuState.samuNonSolliciteExiste(matchedEvents);
		*/
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) throws Exception {
		eb.removeEvent(matchedEvents.get(0));
		System.out.println("S15 \n");	}

}
