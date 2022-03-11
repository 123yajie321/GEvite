package gevite.rule.samu;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import gevite.correlateur.CorrelatorStateI;
import gevite.correlateur.SamuCorrelatorStateI;
import gevite.evenement.EventBaseI;
import gevite.evenement.EventI;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.atomique.samu.InterventionCause;
import gevite.evenement.atomique.samu.SignaleManuel;
import gevite.evenement.complexe.samu.DemandeIntervention;
import gevite.rule.RuleI;

public class S6 implements RuleI{
	@Override
	public ArrayList<EventI> match(EventBaseI eb)throws Exception {
		EventI as=null;
		for (int i = 0 ; i < eb.numberOfEvents() && (as == null ) ; i++) {
			EventI e = eb.getEvent(i);
			if (e instanceof AlarmeSante && e.hasProperty("type")&& e.hasProperty("position")
					&& e.getPropertyValue("type")==TypeOfHealthAlarm.TRACKING					) {
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
	public boolean correlate(ArrayList<EventI> matchedEvents) throws Exception{
		
		return true;
	}

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI cs) throws Exception {
		 SamuCorrelatorStateI samuState = (SamuCorrelatorStateI)cs; 
		 
			
	       return
	         matchedEvents.get(0).getTimeStamp().isBefore(
	                                      matchedEvents.get(1).getTimeStamp()) &&
	         matchedEvents.get(0).getTimeStamp().plus(
	                     Duration.of(10, ChronoUnit.MINUTES)).isBefore(
	                                      matchedEvents.get(1).getTimeStamp())&& !samuState.isMedicAvailable()&&samuState.procheSamuExiste();
			
			
			 
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI cs)throws Exception {
		SamuCorrelatorStateI samuState = (SamuCorrelatorStateI)cs;
		EventI interventionCause=new InterventionCause();
		ArrayList<EventI> eventComplex = matchedEvents; 
		eventComplex.add(interventionCause);
		DemandeIntervention dIntervention = new DemandeIntervention(eventComplex);
		samuState.propagerEvent(dIntervention);
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) throws Exception{
		eb.removeEvent(matchedEvents.get(0));
	   
	}

	

}