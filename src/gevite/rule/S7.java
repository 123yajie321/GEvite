package gevite.rule;

import java.util.ArrayList;

import gevite.correlateur.CorrelatorStateI;
import gevite.correlateur.HealthCorrelatorStateI;
import gevite.evenement.EventBaseI;
import gevite.evenement.EventI;
import gevite.evenement.atomique.AlarmeSante;
import gevite.evenement.atomique.SignaleManuel;

public class S7 implements RuleI{
	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		EventI he = null; EventI s = null;
	    for (int i = 0 ; i < eb.numberOfEvents() && (he == null || s == null) ; i++) {
	    	EventI e = eb.getEvent(i);
	    	if (e instanceof AlarmeSante && e.hasProperty("type")
	    			&& ((String)e.getPropertyValue("type")).equals("tracking")) {
	    		he = e;
	    	}
	    	if (e instanceof SignaleManuel) { s = e; }
	    }
	    if (he != null && s != null) {
	    	
	        ArrayList<EventI> matchedEvents = new ArrayList<>();
	        matchedEvents.add(he);
	        matchedEvents.add(s);
	        
	        return matchedEvents;
	    } else {
	        return null;
	    } 
	    
	}
			
		

	@Override
	public boolean correlate(ArrayList<EventI> matchedEvents) {
		
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
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI cs) {
		HealthCorrelatorStateI samuState = (HealthCorrelatorStateI)cs;
		return samuState.isAmbulanceAvailable();
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI cs) {
		HealthCorrelatorStateI samuState = (HealthCorrelatorStateI) cs;
	    samuState.triggerMedicCall(matchedEvents.get(0).getPropertyValue("personId"));

	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		eb.removeEvent(matchedEvents.get(0));
	    eb.removeEvent(matchedEvents.get(1));
	}

	

}
