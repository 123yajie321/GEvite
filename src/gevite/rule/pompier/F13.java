package gevite.rule.pompier;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;

import fr.sorbonne_u.cps.smartcity.SmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFire;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFirefightingResource;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import gevite.evenement.EventBaseI;
import gevite.evenement.EventI;
import gevite.evenement.atomique.pompier.AlarmFeu;

import gevite.evenement.atomique.pompier.PompierPlusPres;
import gevite.evenement.atomique.pompier.TouslesFireStation;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.atomique.samu.SignaleManuel;
import gevite.evenement.complexe.pompier.DemandeInterventionFeu;
import gevite.evenement.complexe.pompier.PremiereAlarmFeu;
import gevite.evenement.complexe.pompier.SecondAlarmFeu;
import gevite.interfaces.CorrelatorStateI;
import gevite.interfaces.PompierCorrelatorStateI;
import gevite.rule.RuleI;

public class F13 implements RuleI{

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		
		EventI secondAlarm=null;
		for (int i = 0 ; i < eb.numberOfEvents() && (secondAlarm == null ) ; i++) {
			EventI e = eb.getEvent(i);
			if (e instanceof SecondAlarmFeu) {
				secondAlarm = e;
			}
		}
		if(secondAlarm  != null ) {
			ArrayList<EventI> matchedEvents = new ArrayList<>();
			matchedEvents.add(secondAlarm);
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
					return pompierCorrelatorState.isCamionDisponible();
				}
				
			}
		}
		return false;
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		PompierCorrelatorStateI pompierCorrelatorState = (PompierCorrelatorStateI) c;
		SecondAlarmFeu secondAlarm = (SecondAlarmFeu) matchedEvents.get(0);
		pompierCorrelatorState.declancheSecondAlarme((AbsolutePosition) secondAlarm.getCorrelatedEvents().get(0).getPropertyValue("position"));

	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		System.out.print(" F13 \n");
	}
	

}
