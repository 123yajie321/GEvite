package gevite.fire;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.FireStationActionCI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFirefightingResource;

public class FIREActionOutboundPort extends AbstractOutboundPort implements	FireStationActionCI{

	private static final long serialVersionUID = 1L;

	public FIREActionOutboundPort(ComponentI owner) throws Exception {
		super(FireStationActionCI.class, owner);
		// TODO Auto-generated constructor stub
	}
	
	public FIREActionOutboundPort(String uri,ComponentI owner) throws Exception {
		super(uri,FireStationActionCI.class, owner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void			triggerFirstAlarm(AbsolutePosition p, TypeOfFirefightingResource r)
	throws Exception
	{
		((FireStationActionCI)this.getConnector()).triggerFirstAlarm(p, r);
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationActionCI#triggerSecondAlarm(fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition)
	 */
	@Override
	public void			triggerSecondAlarm(AbsolutePosition p) throws Exception
	{
		((FireStationActionCI)this.getConnector()).triggerSecondAlarm(p);
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationActionCI#triggerGeneralAlarm(fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition)
	 */
	@Override
	public void			triggerGeneralAlarm(AbsolutePosition p) throws Exception
	{
		((FireStationActionCI)this.getConnector()).triggerGeneralAlarm(p);
	}

	
	

}
