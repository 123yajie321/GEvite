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
import gevite.evenement.atomique.samu.InterventionCause;
import gevite.evenement.atomique.samu.SignaleManuel;
import gevite.evenement.complexe.samu.ConsciousFall;
import gevite.evenement.complexe.samu.DemandeIntervention;
import gevite.rule.RuleI;

public class S8 implements RuleI{
	@Override
	public ArrayList<EventI> match(EventBaseI eb)throws Exception {
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
	        
	        return matchedEvents;
	    } else {
	        return null;
	    } 
	    
	}
			
		

	@Override
	public boolean correlate(ArrayList<EventI> matchedEvents)throws Exception {
		
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
		SamuCorrelatorStateI samuState = (SamuCorrelatorStateI)cs;
		return samuState.isMedicAvailable()&&samuState.procheSamuExiste();
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI cs) throws Exception {
		
		SamuCorrelatorStateI samuState = (SamuCorrelatorStateI)cs;
		ConsciousFall fall = new ConsciousFall(matchedEvents);
		samuState.propagerEvent(fall);
		
		

	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb)throws Exception {
		eb.removeEvent(matchedEvents.get(0));
	    eb.removeEvent(matchedEvents.get(1));
	}

	

}
