package gevite.correlateur;

import java.io.Serializable;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.smartcity.SmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.descriptions.AbstractSmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import fr.sorbonne_u.utils.Pair;
import gevite.actions.SamuActions;
import gevite.cep.ActionExecutionCI;
import gevite.cep.CEPBusManagementCI;
import gevite.cep.EventEmissionCI;
import gevite.cep.EventReceptionCI;
import gevite.cepbus.CEPBus;
import gevite.cepbus.CepEventSendCorrelateurOutboundPort;
import gevite.connector.ConnectorCorrelateurCepServices;
import gevite.connector.ConnectorCorrelateurExecutor;
import gevite.connector.ConnectorCorrelateurSAMU;
import gevite.connector.ConnectorCorrelateurSendCep;
import gevite.evenement.EventBase;
import gevite.evenement.EventI;
import gevite.evenement.atomique.AtomicEvent;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.atomique.samu.SamuDejaSollicite;
import gevite.evenement.atomique.samu.SignaleManuel;
import gevite.evenement.complexe.ComplexEvent;
import gevite.evenement.complexe.samu.ConsciousFall;
import gevite.evenement.complexe.samu.DemandeInterventionSamu;
import gevite.plugin.PluginActionExecuteOut;
import gevite.plugin.PluginEmissionOut;
import gevite.rule.RuleBase;

@OfferedInterfaces(offered = {EventReceptionCI.class})
@RequiredInterfaces(required = {CEPBusManagementCI.class,EventEmissionCI.class,ActionExecutionCI.class})

public class CorrelateurSamu extends AbstractCorrelateur implements SamuCorrelatorStateI{
	
	//boolean pour stocker la disponibilite
	//des ressource d'executeur correspondant
	protected AtomicBoolean ambulancesAvailable;
	protected AtomicBoolean medicsAvailable;
	
	protected CorrelateurSamu(String correlateurId,String executor,ArrayList<String>emitters,RuleBase ruleBase) throws Exception{
		super(correlateurId, executor, emitters, ruleBase);
		this.ambulancesAvailable=new AtomicBoolean(true);
		this.medicsAvailable=new AtomicBoolean(true);
		
	}
	
	
	/*S16*/
    public void setAmbulancesNoAvailable() throws Exception {
    	
    	
    	((AtomicBoolean) this.ambulancesAvailable).getAndSet(false);
    }
	
	
	/*S17*/
    public void setMedcinNoAvailable() throws Exception{
    	this.medicsAvailable.getAndSet(false);
    }
	
	
	/*S18*/
    public void setAmbulancesAvailable() throws Exception{
    	this.ambulancesAvailable.getAndSet(true);
    }
	
	
	/*S19*/
    public void setMedcinAvailable() throws Exception{
    	this.medicsAvailable.getAndSet(true);
    	
    }	

	@Override
	public boolean inZone(AbsolutePosition p) {
		return SmartCityDescriptor.dependsUpon(p,this.executor);
	}


	@Override
	public boolean isAmbulanceAvailable() {
	
		return this.ambulancesAvailable.get();
	}



	@Override
	public void intervanetionAmbulance(AbsolutePosition position,String personId,TypeOfSAMURessources type) throws Exception {
		
		SamuActions	intervention=  SamuActions.InterventionAmbulance;
		String uri=((CorrelateurActionExecutionOutboundPort) caeop).getPortURI();
		System.out.println("the action Port : "+uri);
		this.caeop.executeAction(intervention, new Serializable[] {position,personId,type}); 
		System.out.println("intervanetionAmbulance finished");
	
	}
	
	@Override
	public void intervanetionMedecin(AbsolutePosition position,String personId,TypeOfSAMURessources type) throws Exception {
		
		SamuActions	intervention=  SamuActions.IntervetionMedcin;
		String uri=((CorrelateurActionExecutionOutboundPort) caeop).getPortURI();
		System.out.println("the action Port : "+uri);
		this.caeop.executeAction(intervention, new Serializable[] {position,personId,type}); 
		System.out.println("intervanetionMedecin finished");
	
	}


	@Override
	public void triggerMedicCall(AbsolutePosition position, String personId, TypeOfSAMURessources type)
			throws Exception {
		SamuActions intervention=SamuActions.AppelMedcin;
		this.caeop.executeAction(intervention, new Serializable[] {position,personId,type}); 
		
	}
	
	@Override
	public boolean samuNonSolliciteExiste(ArrayList<EventI>matchedEvents)throws Exception {
		
		EventI eventRecu=matchedEvents.get(0);
		
		//recuperer le nombre  total de samu  et leur id
		int nbsamu=0;
        ArrayList<String>samuList=new ArrayList<String>();
		Iterator<String> samuStationsIditerator =
				SmartCityDescriptor.createSAMUStationIdIterator();
		while (samuStationsIditerator.hasNext()) {
			String samuStationId = samuStationsIditerator.next();
			nbsamu++;
			samuList.add(samuStationId);
		}
		
		//si le event recu n'est pas un complexeEvent comme "DemandeInterventionSamu",
		// on sait que c'est le 1 fois qu'il sollicite un samu , donc si le nombre de samu>1,
		//on sait qu'il existe des samu no encore sollicite
		
		if (eventRecu instanceof AtomicEvent) {
			return nbsamu > 1;
		}
		
		
		ArrayList<EventI>listEvent=((ComplexEvent) eventRecu).getCorrelatedEvents();
	
		
		SamuDejaSollicite samusEvent=null;
		for(EventI e:listEvent) {
			if(e instanceof SamuDejaSollicite) {
				samusEvent=(SamuDejaSollicite) e;
				break;
			}
		}
		if(samusEvent == null) {
			return nbsamu > 1;
		}else {
			for(String s:samuList) {
				//chercher s'il  existe un samu n'est pas dans la liste des samu deja sollicite 
				if( !(((String) samusEvent.getPropertyValue("samuIdList")).contains(s)))
					return true;
			}
		}
		return false;		
			
		/*
		int nbsamu=0;	
		DemandeInterventionSamu demandeInterventionSamu=(DemandeInterventionSamu) matchedEvents.get(0);
		ArrayList<EventI> correlateEvents=demandeInterventionSamu.getCorrelatedEvents();
		
		Iterator<String> samuStationsIditerator =
				SmartCityDescriptor.createSAMUStationIdIterator();
		while (samuStationsIditerator.hasNext()) {
			String samuStationId = samuStationsIditerator.next();
			nbsamu++;
			
		}
		
			return nbsamu > correlateEvents.size()-1;*/
		
	}

	@Override
	public boolean isMedicAvailable() {
		
		return this.medicsAvailable.get();
	}

	@Override
	public void propagerEvent(EventI event) throws Exception {
		this.cscop.sendEvent(this.correlateurId, event);
	}	
	
	

}
