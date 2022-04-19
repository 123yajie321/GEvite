package gevite.rule.pompier;

import java.util.ArrayList;
import java.util.Iterator;

import fr.sorbonne_u.cps.smartcity.SmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.descriptions.AbstractSmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFire;
import gevite.correlateur.CorrelatorStateI;
import gevite.correlateur.PompierCorrelatorStateI;
import gevite.correlateur.SamuCorrelatorStateI;
import gevite.evenement.EventBaseI;
import gevite.evenement.EventI;
import gevite.evenement.atomique.AtomicEvent;
import gevite.evenement.atomique.pompier.AlarmFeu;
import gevite.evenement.atomique.pompier.InterventionCauseFeu;
import gevite.evenement.atomique.pompier.PompierDejaSollicite;
import gevite.evenement.atomique.pompier.PompierPlusPres;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.atomique.samu.SamuDejaSollicite;
import gevite.evenement.atomique.samu.SamuPlusPres;
import gevite.evenement.complexe.pompier.DemandeInterventionFeu;
import gevite.evenement.complexe.pompier.PremiereAlarmFeu;
import gevite.evenement.complexe.samu.DemandeInterventionSamu;
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
		return !pompierCorrelatorState.isEchelleDisponible();
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		PompierCorrelatorStateI pompierState = (PompierCorrelatorStateI) c;
		ArrayList<String> pompierNonSolId = new ArrayList<String>();
		ArrayList<String> pomperDejaSolId = new ArrayList<String>();
		pomperDejaSolId.add(pompierState.getExecutorId());
		
		AtomicEvent pompierDejaSol=new PompierDejaSollicite();
		pompierDejaSol.putProperty("pompierId", pompierState.getExecutorId() );
		
		Iterator<String> fireStationsIditerator =
				SmartCityDescriptor.createFireStationIdIterator();
		while (fireStationsIditerator.hasNext()) {
			String fireStationId = fireStationsIditerator.next();
		}
		
		double minDistance = AbstractSmartCityDescriptor.distance(pompierState.getExecutorId(), pompierNonSolId.get(0)) ;
		String plusPreStation = pompierNonSolId.get(0);
		
		for(int i=1;i<pompierNonSolId.size();i++) {
			double tempD = AbstractSmartCityDescriptor.distance(pompierState.getExecutorId(), pompierNonSolId.get(i));
			if(tempD < minDistance) {
				minDistance = tempD;
				plusPreStation = pompierNonSolId.get(i);
			}
		}
		AtomicEvent pompierPlusPres = new PompierPlusPres();
		pompierPlusPres.putProperty("pluspresStation", plusPreStation);
		
		ArrayList<EventI> eventComplex = new ArrayList<EventI>() ;
		eventComplex.addAll(matchedEvents);
		eventComplex.add(pompierDejaSol);
		eventComplex.add(pompierPlusPres);

		DemandeInterventionFeu dIntervention = new DemandeInterventionFeu(eventComplex);
		
		pompierState.propagerEvent(dIntervention);
		
		
		/*
		PompierCorrelatorStateI pompierState = (PompierCorrelatorStateI) c;
		EventI interventionCauseFeu = new InterventionCauseFeu();
		ArrayList<EventI> eventComplex = matchedEvents;
		eventComplex.add(interventionCauseFeu);
		DemandeInterventionFeu demandeInterventionFeu = new DemandeInterventionFeu(eventComplex);
		pompierState.propagerEvent(demandeInterventionFeu);
		*/
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		eb.removeEvent(matchedEvents.get(0));
		System.out.print(" F3 \n");
	}
	

}
