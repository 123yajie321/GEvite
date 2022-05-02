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
import gevite.evenement.atomique.samu.SamuPlusPres;
import gevite.evenement.complexe.ComplexEventI;
import gevite.evenement.complexe.samu.ConsciousFall;
import gevite.evenement.complexe.samu.DemandeInterventionSamu;
import gevite.rule.RuleI;

public class S13 implements RuleI {

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
					return samuState.isMedicAvailable();
				}
				
			}
		}
		return false;
		
		/*
		SamuCorrelatorStateI samuState = (SamuCorrelatorStateI)c;
		return samuState.isMedicAvailable();
		*/
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		SamuCorrelatorStateI samuState = (SamuCorrelatorStateI) c;
		ComplexEventI consiousFall = (ComplexEventI)matchedEvents.get(0);
		ArrayList<EventI> correlateEvents = consiousFall.getCorrelatedEvents();
		samuState.triggerMedicCall((AbsolutePosition) correlateEvents.get(0).getPropertyValue("position"),(String)correlateEvents.get(0).getPropertyValue("personId"),
	    		TypeOfSAMURessources.TELEMEDIC);

	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) throws Exception {
		eb.removeEvent(matchedEvents.get(0));
		System.out.println("S13 \n");	}

}
