
package gevite.correlateur;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.smartcity.SmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.descriptions.AbstractSmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;
import gevite.actions.TrafficLightActions;
import gevite.cep.ActionExecutionCI;
import gevite.cep.CEPBusManagementCI;
import gevite.cep.EventEmissionCI;
import gevite.cep.EventReceptionCI;
import gevite.cepbus.CEPBus;
import gevite.connector.ConnectorCorrelateurCepServices;
import gevite.evenement.EventBase;
import gevite.evenement.EventI;
import gevite.plugin.PluginActionExecuteOut;
import gevite.plugin.PluginEmissionOut;
import gevite.rule.RuleBase;

@OfferedInterfaces(offered = {EventReceptionCI.class})
@RequiredInterfaces(required = {CEPBusManagementCI.class,EventEmissionCI.class,ActionExecutionCI.class})

public class CorrelateurtTraffic extends AbstractCorrelateur implements CirculationCorrelatorStateI{
	
	
	
	protected CorrelateurtTraffic(String correlateurId,String executor,ArrayList<String>emitters,RuleBase ruleBase,String busManagementInboundPortUri) throws Exception{
		super(correlateurId, executor, emitters, ruleBase,busManagementInboundPortUri);
	}
	
	
	/*

	@Override
	public boolean estAvantDestination(Serializable destination) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void passerIntersectionN(Serializable priorite) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public boolean estApresDestination(Serializable destination) {
		// TODO Auto-generated method stub
		return false;
	}
	*/

	@Override
	public void changePriority(TypeOfTrafficLightPriority p,IntersectionPosition position) throws Exception {
		int index = 0;
		Iterator<IntersectionPosition> trafficLightsIterator =
				SmartCityDescriptor.createTrafficLightPositionIterator();
		
	
		/*
		while (trafficLightsIterator.hasNext()) {
			if(trafficLightsIterator.next().equals(position)) {
				break;
			}
			index++;
		}
		*/
		TrafficLightActions traffic=TrafficLightActions.changePriority;
		this.caeop.executeAction(traffic, new Serializable[] {p}); 
		
	}




	
	
	
	

}
