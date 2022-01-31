package gevite.rule;

import gevite.evenement.EventBaseI;

public interface RuleBase {
	
	public void addRule(RuleI r);
	public boolean fireFirstOn(EventBaseI eb,CorrelatorStateI c);
	public boolean fireAllOn(EventBaseI eb,CorrelatorStateI c);
	
	
}
