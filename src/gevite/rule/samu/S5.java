package gevite.rule.samu;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import fr.sorbonne_u.cps.smartcity.utils.TimeManager;
import gevite.evenement.EventBaseI;
import gevite.evenement.EventI;
import gevite.evenement.atomique.AtomicEvent;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.atomique.samu.SignaleManuel;
import gevite.interfaces.CorrelatorStateI;
import gevite.interfaces.SamuCorrelatorStateI;
import gevite.rule.RuleI;

public class S5 implements RuleI{
	@Override
	public ArrayList<EventI> match(EventBaseI eb) throws Exception{
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
	public boolean correlate(ArrayList<EventI> matchedEvents)throws Exception {
		return true;
		
	}

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI cs) throws Exception {
		 SamuCorrelatorStateI samuState = (SamuCorrelatorStateI)cs; 
		 
//        TimeManager.get().getCurrentLocalTime();
//        long nanotime1 = TimeManager.get().localTime2nanoDelay(matchedEvents.get(0).getTimeStamp());
//        long nanotime2 = TimeManager.get().localTime2nanoDelay(matchedEvents.get(0).getTimeStamp());
//       
       // long delai=TimeManager.get().localTime2nanoDelay(matchedEvents.get(0).getTimeStamp().plusMinutes(10));
       /* if(nanotime2-nanotime1 < 10/TimeManager.ACCELERATION_FACTOR) {
        	return false;
        }*/
        
        
       return
         matchedEvents.get(0).getTimeStamp().isBefore(
                                      TimeManager.get().getCurrentLocalTime()) &&
        
       
         matchedEvents.get(0).getTimeStamp().plus(
                     Duration.of( 10, ChronoUnit.MINUTES)).isBefore(
                    		 TimeManager.get().getCurrentLocalTime())&& samuState.isMedicAvailable();
		

	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI cs)throws Exception {
		

	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb)throws Exception {
		eb.removeEvent(matchedEvents.get(0));
		EventI event=matchedEvents.get(0);
	    ((AtomicEvent) event).removeProperty("type");
	    ((AtomicEvent) event).putProperty("type",TypeOfHealthAlarm.MEDICAL);
	   
	    //System.out.println("S5!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	    System.out.println("S5 \n");
	   
	}

	

}
