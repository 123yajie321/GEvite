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
import gevite.evenement.atomique.samu.AmbulancesAvailable;
import gevite.evenement.atomique.samu.AmbulancesBusy;
import gevite.rule.RuleI;

public class S18 implements RuleI {
	public String tag="s18";

	@Override
	public ArrayList<EventI> match(EventBaseI eb)throws Exception {
		EventI AmbulancesAvailable=null;
		for (int i = 0 ; i < eb.numberOfEvents() && (AmbulancesAvailable == null ) ; i++) {
			EventI e = eb.getEvent(i);
			if (e instanceof AmbulancesAvailable ) {
				AmbulancesAvailable = e;
			}
		}	
			if(AmbulancesAvailable  != null ) {
				ArrayList<EventI> matchedEvents = new ArrayList<>();
				matchedEvents.add(AmbulancesAvailable);
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
		SamuCorrelatorStateI samuState = (SamuCorrelatorStateI)c;
		samuState.setAmbulancesAvailable();
		System.out.println(tag);

	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		eb.removeEvent(matchedEvents.get(0));
	}

}
