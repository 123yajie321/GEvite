package gevite.cep.gestionEvent.correlateur;

import fr.sorbonne_u.components.AbstractComponent;
import gevite.evenement.EventBase;
import gevite.rule.RuleBase;

public class CorrelateurComposant extends AbstractComponent {

	protected CorrelateurComposant(int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
		// TODO Auto-generated constructor stub
	}
	
	protected EventBase baseEvent;
	protected RuleBase baseRule;
	
	
	
	

}
