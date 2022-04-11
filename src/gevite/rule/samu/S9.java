package gevite.rule.samu;

import java.util.ArrayList;

import gevite.correlateur.CorrelatorStateI;
import gevite.evenement.EventBaseI;
import gevite.evenement.EventI;
import gevite.rule.RuleI;

public class S9 implements RuleI {

	@Override
	public ArrayList<EventI> match(EventBaseI eb) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean correlate(ArrayList<EventI> matchedEvents) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
