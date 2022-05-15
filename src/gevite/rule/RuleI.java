package gevite.rule;

import java.util.ArrayList;

import gevite.evenement.EventBaseI;
import gevite.evenement.EventI;
import gevite.interfaces.CorrelatorStateI;

public interface RuleI {
	public ArrayList<EventI> match(EventBaseI eb)throws Exception;
	public boolean correlate(ArrayList<EventI> matchedEvents)throws Exception;
	public boolean filter(ArrayList<EventI> matchedEvents,CorrelatorStateI c) throws Exception;
	public void act(ArrayList<EventI> matchedEvents,CorrelatorStateI c)throws Exception;
	public void update(ArrayList<EventI> matchedEvents,EventBaseI eb)throws Exception;
	
	
	
	
	
	

}
