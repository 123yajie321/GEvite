package gevite.rule.pompier;

import java.util.ArrayList;

import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import gevite.evenement.EventBaseI;
import gevite.evenement.EventI;
import gevite.evenement.atomique.pompier.HighLadderTrucksBusy;
import gevite.evenement.atomique.pompier.StandardTrucksAvailable;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.atomique.samu.AmbulancesBusy;
import gevite.interfaces.CorrelatorStateI;
import gevite.interfaces.PompierCorrelatorStateI;
import gevite.interfaces.SamuCorrelatorStateI;
import gevite.rule.RuleI;

public class F18 implements RuleI {

	@Override
	public ArrayList<EventI> match(EventBaseI eb)throws Exception {
		EventI noEchelle=null;
		for (int i = 0 ; i < eb.numberOfEvents() && (noEchelle == null ) ; i++) {
			EventI e = eb.getEvent(i);
			if (e instanceof StandardTrucksAvailable ) {
				noEchelle = e;
			}
		}	
			if(noEchelle  != null ) {
				ArrayList<EventI> matchedEvents = new ArrayList<>();
				matchedEvents.add(noEchelle);
				return matchedEvents;
			} else {
				return null;
			}
			
		
	}

	@Override
	public boolean correlate(ArrayList<EventI> matchedEvents)throws Exception  {
		
		return true;
	}

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI cs) throws Exception {
		
		return true;
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		PompierCorrelatorStateI pompState = (PompierCorrelatorStateI)c;
		pompState.setStandardTRucksAvailable();
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		eb.removeEvent(matchedEvents.get(0));
		System.out.print(" F18 \n");
	}

}
