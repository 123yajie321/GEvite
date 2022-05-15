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
import gevite.evenement.atomique.pompier.GeneralAlarmFeu;
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

public class F10bis implements RuleI{

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		
		EventI alarmGeneral=null;
		for (int i = 0 ; i < eb.numberOfEvents() && (alarmGeneral == null ) ; i++) {
			EventI e = eb.getEvent(i);
			if (e instanceof GeneralAlarmFeu) {
				alarmGeneral = e;
			}
		}
		if(alarmGeneral  != null ) {
			ArrayList<EventI> matchedEvents = new ArrayList<>();
			matchedEvents.add(alarmGeneral);
			return matchedEvents;
		} else {
			return null;
		}
	
	}

	@Override
	public boolean correlate(ArrayList<EventI> matchedEvents) {
		//System.out.println("F11 correlate");
		return true;
	}

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		//System.out.println("F11 filter");
		PompierCorrelatorStateI pompierCorrelatorState = (PompierCorrelatorStateI) c;
		GeneralAlarmFeu generalAlarmFeu = (GeneralAlarmFeu) matchedEvents.get(0);
		ArrayList<EventI> correlateEvents = generalAlarmFeu.getCorrelatedEvents();
		
		for(int i = 0; i< correlateEvents.size();i++) {
			if(correlateEvents.get(i) instanceof TouslesFireStation) {
				ArrayList<String> touslesFireStation = (ArrayList<String>) correlateEvents.get(i).getPropertyValue("tousLesfireStation");
				if(touslesFireStation.contains(pompierCorrelatorState.getExecutorId())) {
					return !pompierCorrelatorState.isEchelleDisponible();
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
		System.out.print(" F10 \n");

		
	}
	

}
