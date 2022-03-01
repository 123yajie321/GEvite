package gevite.connector;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightActionCI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;

public class ConnectorTrafficAction extends AbstractConnector implements TrafficLightActionCI{

	/**
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true				// no more preconditions.
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightActionCI#changePriority(fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority)
	 */
	@Override
	public void			changePriority(TypeOfTrafficLightPriority priority)
	throws Exception
	{
		((TrafficLightActionCI)this.offering).changePriority(priority);
	}

	/**
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true				// no more preconditions.
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightActionCI#returnToNormalMode()
	 */
	@Override
	public void			returnToNormalMode() throws Exception
	{
		((TrafficLightActionCI)this.offering).returnToNormalMode();
	}

	
	

}
