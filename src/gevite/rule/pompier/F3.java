package gevite.rule.pompier;

import java.util.ArrayList;

import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFire;
import gevite.correlateur.CorrelatorStateI;
import gevite.correlateur.PompierCorrelatorStateI;
import gevite.evenement.EventBaseI;
import gevite.evenement.EventI;
import gevite.evenement.atomique.pompier.AlarmFeu;
import gevite.evenement.atomique.pompier.InterventionCauseFeu;
import gevite.evenement.complexe.pompier.DemandeInterventionFeu;
import gevite.evenement.complexe.pompier.PremiereAlarmFeu;
import gevite.rule.RuleI;

public class F3 implements RuleI{

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		EventI af=null;
		for (int i = 0 ; i < eb.numberOfEvents() && (af == null ) ; i++) {
			EventI e = eb.getEvent(i);
			if (e instanceof AlarmFeu && e.hasProperty("type")&& e.hasProperty("position")
					&& e.getPropertyValue("type")== TypeOfFire.Building
			) {
				af = e;
			}
		}
		if(af  != null ) {
			ArrayList<EventI> matchedEvents = new ArrayList<>();
			matchedEvents.add(af);
			return matchedEvents;
		} else {
			return null;
		}
	}

	@Override
	public boolean correlate(ArrayList<EventI> matchedEvents) {
		return true;
	}

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		PompierCorrelatorStateI pompierCorrelatorState = (PompierCorrelatorStateI) c;
		return (!pompierCorrelatorState.isEchelleDisponible())&&pompierCorrelatorState.procheCaserneExiste();
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		PompierCorrelatorStateI pompierState = (PompierCorrelatorStateI) c;
		EventI interventionCauseFeu = new InterventionCauseFeu();
		ArrayList<EventI> eventComplex = matchedEvents;
		eventComplex.add(interventionCauseFeu);
		DemandeInterventionFeu demandeInterventionFeu = new DemandeInterventionFeu(eventComplex);
		pompierState.propagerEvent(demandeInterventionFeu);
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		eb.removeEvent(matchedEvents.get(0));
		System.out.print(" F3 \n");
	}
	

}
