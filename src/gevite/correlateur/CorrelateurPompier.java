package gevite.correlateur;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.smartcity.SmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.descriptions.AbstractSmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFirefightingResource;
import gevite.actions.FireStationActions;
import gevite.actions.SamuActions;
import gevite.cep.ActionExecutionCI;
import gevite.cep.CEPBusManagementCI;
import gevite.cep.EventEmissionCI;
import gevite.cep.EventReceptionCI;
import gevite.cepbus.CEPBus;
import gevite.connector.ConnectorCorrelateurCepServices;
import gevite.connector.ConnectorCorrelateurExecutor;
import gevite.connector.ConnectorCorrelateurPompier;
import gevite.connector.ConnectorCorrelateurSendCep;
import gevite.evenement.EventBase;
import gevite.evenement.EventI;
import gevite.evenement.atomique.AtomicEvent;
import gevite.evenement.atomique.pompier.PompierDejaSollicite;
import gevite.evenement.atomique.samu.SamuDejaSollicite;
import gevite.evenement.complexe.ComplexEvent;
import gevite.evenement.complexe.samu.ConsciousFall;
import gevite.evenement.complexe.samu.DemandeInterventionSamu;
import gevite.plugin.PluginActionExecuteOut;
import gevite.plugin.PluginEmissionOut;
import gevite.rule.RuleBase;

@OfferedInterfaces(offered = {EventReceptionCI.class})
@RequiredInterfaces(required = {CEPBusManagementCI.class,EventEmissionCI.class,ActionExecutionCI.class})

public class CorrelateurPompier extends AbstractCorrelateur implements PompierCorrelatorStateI  {
	
	
	
	protected AtomicBoolean echelleAvailable;
	protected AtomicBoolean camionAvailable;
	
	protected CorrelateurPompier(String correlateurId,String executor,ArrayList<String>emitters,RuleBase ruleBase,String busManagementInboundPortUri) throws Exception{
		super(correlateurId, executor, emitters, ruleBase,busManagementInboundPortUri);
		this.echelleAvailable=new AtomicBoolean(true);
		this.camionAvailable=new AtomicBoolean(true);
	}
	


	
	@Override
	public boolean inZone(AbsolutePosition p) throws Exception {
		
		return SmartCityDescriptor.dependsUpon(p,this.executor);

	}



	@Override
	public void declancheFirstAlarme(AbsolutePosition position, TypeOfFirefightingResource type) throws Exception {
		FireStationActions firstAlarmActions = FireStationActions.FirstAlarme;
		
		this.caeop.executeAction(firstAlarmActions, new Serializable[] {position,type}); 			
	}



	@Override
	public void declancheSecondAlarme(AbsolutePosition position) throws Exception {
		FireStationActions secondAlarmActions = FireStationActions.FirstAlarme;
		this.caeop.executeAction(secondAlarmActions, new Serializable[] {position}); 			
	}		

	@Override
	public void propagerEvent(EventI event) throws Exception {
		this.cscop.sendEvent(this.correlateurId, event);		
	}



	@Override
	public boolean isEchelleDisponible() throws Exception {
		return echelleAvailable.get();
	}



	@Override
	public boolean isCamionDisponible() throws Exception {
		return camionAvailable.get();
	}


	@Override
	public void setHighLadderTrucksBusy() throws Exception {
		this.echelleAvailable.getAndSet(false);
    
	}


	@Override
	public void setStandardTrucksBusy() throws Exception {
		this.camionAvailable.getAndSet(false);
    
		
	}


	@Override
	public void setHighLadderTrucksAvailable() throws Exception {
		
		this.echelleAvailable.getAndSet(true);
		
	}


	@Override
	public void setStandardTRucksAvailable() throws Exception {
		
		this.camionAvailable.getAndSet(true);
		
	}
	
	@Override
	public boolean caserneNonSolliciteExiste(ArrayList<EventI>matchedEvents)throws Exception {
		EventI eventRecu=matchedEvents.get(0);
		
		//recuperer le nombre total de firestation et leur id
		int nbCaserne=0;
		ArrayList<String> fireStationlist = new ArrayList<String>();
		Iterator<String> fireStationsIditerator =
				SmartCityDescriptor.createFireStationIdIterator();
		
		while (fireStationsIditerator.hasNext()) {
			String fireStationId = fireStationsIditerator.next();
			nbCaserne++;
			fireStationlist.add(fireStationId);
		}
		
		//si le event recu n'est pas un complexeEvent comme "DemandeInterventionFeu",
		// on sait que c'est le 1 fois qu'il sollicite un firestation , donc si le nombre de firestation>1,
		//on sait qu'il existe des firestation no encore sollicite
				
		if (eventRecu instanceof AtomicEvent) {
			return nbCaserne > 1;
		}
		
		
		ArrayList<EventI>listEvent=((ComplexEvent) eventRecu).getCorrelatedEvents();
		PompierDejaSollicite pompiersollicite=null;
		for(EventI e:listEvent) {
			if(e instanceof PompierDejaSollicite) {
				pompiersollicite=(PompierDejaSollicite) e;
				break;
			}
		}
		if(pompiersollicite == null) {
			return nbCaserne > 1;
		}else {
			for(String s:fireStationlist) {
				if( !(((String) pompiersollicite.getPropertyValue("FireStationIdList")).contains(s)))
					return true;
			}
		}
		return false;		
		
		
		
	}

	
	

}
