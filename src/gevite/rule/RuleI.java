package gevite.rule;

import java.util.ArrayList;

import gevite.correlateur.CorrelatorStateI;
import gevite.evenement.EventBaseI;
import gevite.evenement.EventI;

public interface RuleI {
	public ArrayList<EventI> match(EventBaseI eb);
	public boolean correlate(ArrayList<EventI> matchedEvents);
	public boolean filter(ArrayList<EventI> matchedEvents,CorrelatorStateI c);
	public void act(ArrayList<EventI> matchedEvents,CorrelatorStateI c);
	public void update(ArrayList<EventI> matchedEvents,EventBaseI eb);
	
	
	
	
	
	

}
