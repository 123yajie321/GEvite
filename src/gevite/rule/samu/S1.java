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
import gevite.rule.RuleI;

public class S1 implements RuleI {

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		EventI as=null;
		for (int i = 0 ; i < eb.numberOfEvents() && (as == null ) ; i++) {
			EventI e = eb.getEvent(i);
			if (e instanceof AlarmeSante && e.hasProperty("type")&& e.hasProperty("position")
					&& e.getPropertyValue("type")==TypeOfHealthAlarm.EMERGENCY
					) {
				as = e;
			}
		}	
			if(as  != null ) {
				ArrayList<EventI> matchedEvents = new ArrayList<>();
				matchedEvents.add(as);
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
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI cs) throws Exception {
		SamuCorrelatorStateI samuState = (SamuCorrelatorStateI)cs;
		EventI alarmSante=matchedEvents.get(0);
		return samuState.inZone((AbsolutePosition) alarmSante.getPropertyValue("position"))&& samuState.isAmbulanceAvailable();
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		SamuCorrelatorStateI samuState = (SamuCorrelatorStateI)c;
		EventI alarmSante=matchedEvents.get(0);
		samuState.intervanetionAmbulance((AbsolutePosition) alarmSante.getPropertyValue("position"),null,TypeOfSAMURessources.AMBULANCE);

	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		eb.removeEvent(matchedEvents.get(0));
	}

}
