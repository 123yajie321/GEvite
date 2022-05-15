package gevite.rule;

import java.util.ArrayList;

import gevite.evenement.EventBaseI;
import gevite.evenement.EventI;
import gevite.interfaces.CorrelatorStateI;

public class RuleBase {
	
	ArrayList<RuleI> rules;
	
	public RuleBase() {
		rules = new ArrayList<>();
	}
	
	
	public void addRule(RuleI r) throws Exception {
		rules.add(r);
		
	}
	
	public int sizeofrules() {
		return rules.size();
	}
	
	public boolean fireFirstOn(EventBaseI eb,CorrelatorStateI c) throws Exception {
		
		for(RuleI rule: rules) {
			ArrayList<EventI> eventmatched = rule.match(eb);
			if(eventmatched != null) {
				Boolean boolCor = rule.correlate(eventmatched);
				if(boolCor == true) {
					Boolean boolFil = rule.filter(eventmatched, c);
					if(boolFil == true) {
						rule.act(eventmatched, c);
						rule.update(eventmatched, eb);
						return true;
					}
				}
			}
		}
		return false;
	}
	public boolean fireAllOn(EventBaseI eb,CorrelatorStateI c) throws Exception {
		boolean matched=false;
		for(RuleI rule: rules) {
			ArrayList<EventI> eventmatched = rule.match(eb);
			if(eventmatched != null) {
				Boolean boolCor = rule.correlate(eventmatched);
				if(boolCor == true) {
					Boolean boolFil = rule.filter(eventmatched, c);
					if(boolFil == true) {
						rule.act(eventmatched, c);
						rule.update(eventmatched, eb);
						matched= true;
					}
				}
			}
		}
		return matched;
		
	}

}
