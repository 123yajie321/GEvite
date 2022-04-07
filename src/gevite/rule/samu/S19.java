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
import gevite.evenement.atomique.samu.AmbulancesBusy;
import gevite.evenement.atomique.samu.MedecinAvailable;
import gevite.evenement.atomique.samu.MedecinBusy;
import gevite.rule.RuleI;

public class S19 implements RuleI {

	@Override
	public ArrayList<EventI> match(EventBaseI eb)throws Exception {
		EventI MedecinAvailable=null;
		for (int i = 0 ; i < eb.numberOfEvents() && (MedecinAvailable == null ) ; i++) {
			EventI e = eb.getEvent(i);
			if (e instanceof MedecinAvailable ) {
				MedecinAvailable = e;
			}
		}	
			if(MedecinAvailable  != null ) {
				ArrayList<EventI> matchedEvents = new ArrayList<>();
				matchedEvents.add(MedecinAvailable);
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
		samuState.setMedcinAvailable();

	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		eb.removeEvent(matchedEvents.get(0));
		System.out.println("S19 \n");
	}

}
