package gevite.traffic;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.cps.smartcity.interfaces.SAMUActionCI;
import fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightActionCI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;

public class TrafficActionOutboundPort extends AbstractOutboundPort implements	TrafficLightActionCI{

	private static final long serialVersionUID = 1L;

	public TrafficActionOutboundPort(ComponentI owner) throws Exception {
		super(SAMUActionCI.class, owner);
		// TODO Auto-generated constructor stub
	}
	
	public TrafficActionOutboundPort(String uri,ComponentI owner) throws Exception {
		super(uri,SAMUActionCI.class, owner);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightActionCI#changePriority(fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority)
	 */
	@Override
	public void			changePriority(TypeOfTrafficLightPriority priority)
	throws Exception
	{
		((TrafficLightActionCI)this.getConnector()).changePriority(priority);
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightActionCI#returnToNormalMode()
	 */
	@Override
	public void			returnToNormalMode() throws Exception
	{
		((TrafficLightActionCI)this.getConnector()).returnToNormalMode();
	}
	

	
	

}
