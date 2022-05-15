package gevite.rule.samu;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;

import fr.sorbonne_u.cps.smartcity.SmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.descriptions.AbstractSmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import gevite.evenement.EventBaseI;
import gevite.evenement.EventI;
import gevite.evenement.atomique.AtomicEvent;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.atomique.samu.SamuDejaSollicite;
import gevite.evenement.atomique.samu.SamuPlusPres;
import gevite.evenement.atomique.samu.SignaleManuel;
import gevite.evenement.complexe.samu.ConsciousFall;
import gevite.evenement.complexe.samu.DemandeInterventionSamu;
import gevite.interfaces.CorrelatorStateI;
import gevite.interfaces.SamuCorrelatorStateI;
import gevite.rule.RuleI;

public class S8 implements RuleI{
	@Override
	public ArrayList<EventI> match(EventBaseI eb)throws Exception {
		EventI he = null; EventI s = null;
	    for (int i = 0 ; i < eb.numberOfEvents() && (he == null || s == null) ; i++) {
	    	EventI e = eb.getEvent(i);
	    	if (e instanceof AlarmeSante && e.hasProperty("type")
	    			&& e.getPropertyValue("type")==TypeOfHealthAlarm.TRACKING) {
	    		he = e;
	    	}
	    	if (e instanceof SignaleManuel) { s = e; }
	    }
	    if (he != null && s != null) {
	    	
	        ArrayList<EventI> matchedEvents = new ArrayList<>();
	        matchedEvents.add(he);
	        matchedEvents.add(s);
	        
	        return matchedEvents;
	    } else {
	        return null;
	    } 
	    
	}
			
		

	@Override
	public boolean correlate(ArrayList<EventI> matchedEvents)throws Exception {
		
		return matchedEvents.get(0).hasProperty("personId") &&
		           matchedEvents.get(1).hasProperty("personId") &&
		           matchedEvents.get(0).getPropertyValue("personId").equals(
		                       matchedEvents.get(1).getPropertyValue("personId")) &&
		           matchedEvents.get(0).getTimeStamp().isBefore(
		                                        matchedEvents.get(1).getTimeStamp()) &&
		           matchedEvents.get(0).getTimeStamp().plus(
		                       Duration.of(10, ChronoUnit.MINUTES)).isAfter(
		                                        matchedEvents.get(1).getTimeStamp());
	}

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI cs) throws Exception {
		SamuCorrelatorStateI samuState = (SamuCorrelatorStateI)cs;
		return samuState.isMedicAvailable()&&samuState.samuNonSolliciteExiste(matchedEvents);
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		AlarmeSante alarmSante=(AlarmeSante)matchedEvents.get(0);
		SamuCorrelatorStateI samuState = (SamuCorrelatorStateI)c;
		ArrayList<String> samuNonSolId = new ArrayList<String>();
		ArrayList<String> samuDejaSolId = new ArrayList<String>();
		samuDejaSolId.add(samuState.getExecutorId());
		
		AtomicEvent samuDejaSol=new SamuDejaSollicite();
		
		samuDejaSol.putProperty("samuIdList", samuDejaSolId );
		
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
		
		ArrayList<EventI> eventComplex = new ArrayList<EventI>() ;
		eventComplex.add(samuDejaSol);
		eventComplex.add(samuPlusPres);	
		eventComplex.addAll(matchedEvents);
		ConsciousFall fall = new ConsciousFall(eventComplex);
		samuState.propagerEvent(fall);
		/*
		SamuCorrelatorStateI samuState = (SamuCorrelatorStateI)c;
		ConsciousFall fall = new ConsciousFall(matchedEvents);
		samuState.propagerEvent(fall);
		*/
		
		

	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb)throws Exception {
		eb.removeEvent(matchedEvents.get(0));
	    eb.removeEvent(matchedEvents.get(1));
	    System.out.println("S8 \n");
	}

	

}
