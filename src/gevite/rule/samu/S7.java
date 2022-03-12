package gevite.rule.samu;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import gevite.correlateur.CorrelatorStateI;
import gevite.correlateur.SamuCorrelatorStateI;
import gevite.evenement.EventBaseI;
import gevite.evenement.EventI;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.atomique.samu.SignaleManuel;
import gevite.rule.RuleI;

public class S7 implements RuleI{
	@Override
	public ArrayList<EventI> match(EventBaseI eb)throws Exception {
		System.out.println("S7 match");
		EventI he = null; EventI s = null;
	    for (int i = 0 ; i < eb.numberOfEvents() && (he == null || s == null) ; i++) {
	    	EventI e = eb.getEvent(i);
	    	if (e instanceof AlarmeSante && e.hasProperty("type")
	    			&& e.getPropertyValue("type")==TypeOfHealthAlarm.TRACKING) {
	    		he = e;
	    	}
	    	if (e instanceof SignaleManuel) { s = e; }
	    }
	    if (he != null && s != null) {
	    	
	        ArrayList<EventI> matchedEvents = new ArrayList<>();
	        matchedEvents.add(he);
	        matchedEvents.add(s);
	    	System.out.println("S7 fin match");
	        return matchedEvents;
	    } else {
	        return null;
	    } 
	    
	}
			
		

	@Override
	public boolean correlate(ArrayList<EventI> matchedEvents)throws Exception {
		System.out.println("S7 correlate");
		return matchedEvents.get(0).hasProperty("personId") &&
		           matchedEvents.get(1).hasProperty("personId") &&
		           matchedEvents.get(0).getPropertyValue("personId").equals(
		                       matchedEvents.get(1).getPropertyValue("personId")) &&
		           matchedEvents.get(0).getTimeStamp().isBefore(
		                                        matchedEvents.get(1).getTimeStamp()) &&
		           matchedEvents.get(0).getTimeStamp().plus(
		                       Duration.of(10, ChronoUnit.MINUTES)).isAfter(
		                                        matchedEvents.get(1).getTimeStamp());
		
	}

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI cs) throws Exception {
		System.out.println("S7 filter");
		SamuCorrelatorStateI samuState = (SamuCorrelatorStateI)cs;
		return samuState.isMedicAvailable();
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI cs) throws Exception {
		
		
		SamuCorrelatorStateI samuState = (SamuCorrelatorStateI) cs;
		EventI alarmSante=matchedEvents.get(0);
		samuState.triggerMedicCall((AbsolutePosition) alarmSante.getPropertyValue("position"),(String)matchedEvents.get(0).getPropertyValue("personId"),
	    		TypeOfSAMURessources.TELEMEDIC);
		System.out.println("S7 declancher fin");

	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb)throws Exception {
		eb.removeEvent(matchedEvents.get(0));
	    eb.removeEvent(matchedEvents.get(1));
	}

	

}
