package gevite.samu;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.SAMUActionCI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;

public class SAMUActionOutboundPort extends AbstractOutboundPort implements	SAMUActionCI{

	private static final long serialVersionUID = 1L;

	public SAMUActionOutboundPort(ComponentI owner) throws Exception {
		super(SAMUActionCI.class, owner);
		// TODO Auto-generated constructor stub
	}
	
	public SAMUActionOutboundPort(String uri,ComponentI owner) throws Exception {
		super(uri,SAMUActionCI.class, owner);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public void			triggerIntervention(
		AbsolutePosition position,
		String personId,
		TypeOfSAMURessources type
		) throws Exception
	{
		((SAMUActionCI)this.getConnector()).
							triggerIntervention(position, personId, type);
	}
	

}
