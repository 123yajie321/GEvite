
package gevite.correlateur;

/**
 * The class <code>CorrelateurtTraffic</code> implements a component that can
 * send and receive events, demand l'executor traffic Light connected to execute actions
 * 
 *  <p>
 * The component implements the {@code CirculationCorrelatorStateI} interface
 * to verify the correlate condition,trigger demand of execution action
 * </p>
 *    
 * @author Yajie LIU, Zimeng ZHANG
 */
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
import gevite.CEPBus;
import gevite.actions.TrafficLightActions;
import gevite.evenement.EventBase;
import gevite.evenement.EventI;
import gevite.interfaces.ActionExecutionCI;
import gevite.interfaces.CEPBusManagementCI;
import gevite.interfaces.CirculationCorrelatorStateI;
import gevite.interfaces.EventEmissionCI;
import gevite.interfaces.EventReceptionCI;
import gevite.plugin.PluginActionExecuteOut;
import gevite.plugin.PluginEmissionOut;
import gevite.rule.RuleBase;

@OfferedInterfaces(offered = {EventReceptionCI.class})
@RequiredInterfaces(required = {CEPBusManagementCI.class})

public class CorrelateurtTraffic extends AbstractCorrelateur implements CirculationCorrelatorStateI{
	
	
	
	protected CorrelateurtTraffic(String correlateurId,String executor,ArrayList<String>emitters,RuleBase ruleBase,String busManagementInboundPortUri) throws Exception{
		super(correlateurId, executor, emitters, ruleBase,busManagementInboundPortUri);
	}
	
	


	@Override
	public void changePriority(TypeOfTrafficLightPriority p,IntersectionPosition position) throws Exception {
		TrafficLightActions traffic=TrafficLightActions.changePriority;
		this.caeop.executeAction(traffic, new Serializable[] {p}); 
		
	}



	/* pas encore implemente

	@Override
	public boolean estAvantDestination(Serializable destination) {
		
		return false;
	}

	@Override
	public void passerIntersectionN(Serializable priorite) {
		
		
	}


	@Override
	public boolean estApresDestination(Serializable destination) {

		return false;
	}
	*/
	
	
	
	

}
