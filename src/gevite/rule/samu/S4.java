package gevite.rule.samu;

import java.util.ArrayList;
import java.util.Iterator;

import fr.sorbonne_u.cps.smartcity.SmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.descriptions.AbstractSmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import gevite.evenement.EventBaseI;
import gevite.evenement.EventI;
import gevite.evenement.atomique.AtomicEvent;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.atomique.samu.SamuDejaSollicite;
import gevite.evenement.atomique.samu.SamuPlusPres;
import gevite.evenement.complexe.samu.DemandeInterventionSamu;
import gevite.interfaces.CorrelatorStateI;
import gevite.interfaces.SamuCorrelatorStateI;
import gevite.rule.RuleI;

public class S4 implements RuleI{

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		EventI as=null;
		for (int i = 0 ; i < eb.numberOfEvents() && (as == null ) ; i++) {
			EventI e = eb.getEvent(i);
			if (e instanceof AlarmeSante && e.hasProperty("type")&& e.hasProperty("position")
					&& e.getPropertyValue("type")==TypeOfHealthAlarm.MEDICAL
				) {
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
	public boolean correlate(ArrayList<EventI> matchedEvents) {
		
		return true;
	}

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI cs) throws Exception {
		SamuCorrelatorStateI samuState = (SamuCorrelatorStateI)cs;
		EventI alarmSante=matchedEvents.get(0);
		return samuState.inZone((AbsolutePosition) alarmSante.getPropertyValue("position"))&& !samuState.isMedicAvailable()&&samuState.samuNonSolliciteExiste(matchedEvents);
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		AlarmeSante alarmSante=(AlarmeSante)matchedEvents.get(0);
		SamuCorrelatorStateI samuState = (SamuCorrelatorStateI)c;
		ArrayList<String> samuNonSolId = new ArrayList<String>();
		ArrayList<String> samuDejaSolId = new ArrayList<String>();
		samuDejaSolId.add(samuState.getExecutorId());
		
		AtomicEvent samuDejaSol=new SamuDejaSollicite();
		samuDejaSol.putProperty("samuIdList",samuDejaSolId );
		
		Iterator<String> samuStationsIditerator =
				SmartCityDescriptor.createSAMUStationIdIterator();
		while (samuStationsIditerator.hasNext()) {
			String samuStationId = samuStationsIditerator.next();
			if(!samuDejaSolId.contains(samuStationId)) {
				samuNonSolId.add(samuStationId);
			}
		}
		double minDistance = AbstractSmartCityDescriptor.distance(samuState.getExecutorId(), samuNonSolId.get(0)) ;
		String plusPreStation = samuNonSolId.get(0);
		
		for(int i=1;i<samuNonSolId.size();i++) {
			double tempD = AbstractSmartCityDescriptor.distance(samuState.getExecutorId(), samuNonSolId.get(i));
			if(tempD < minDistance) {
				minDistance = tempD;
				plusPreStation = samuNonSolId.get(i);
			}
		}
		AtomicEvent samuPlusPres = new SamuPlusPres();
		samuPlusPres.putProperty("pluspresStation", plusPreStation);
		
		ArrayList<EventI> eventlist = new ArrayList<EventI>() ;
		eventlist.add(samuDejaSol);
		eventlist.add(samuPlusPres);
		eventlist.addAll(matchedEvents);

		DemandeInterventionSamu dIntervention = new DemandeInterventionSamu(eventlist);
		
		samuState.propagerEvent(dIntervention);
		
		/*
		SamuCorrelatorStateI samuState = (SamuCorrelatorStateI)c;
		EventI interventionCause=new InterventionCausesSamu();
		ArrayList<EventI> eventComplex = matchedEvents; 
		eventComplex.add(interventionCause);
		DemandeInterventionSamu dIntervention = new DemandeInterventionSamu(eventComplex);
		samuState.propagerEvent(dIntervention);
		*/
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		eb.removeEvent(matchedEvents.get(0));
		System.out.println("S4 \n");
		
	}

}
