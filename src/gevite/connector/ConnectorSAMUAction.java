package gevite.connector;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.SAMUActionCI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;

public class ConnectorSAMUAction extends AbstractConnector implements SAMUActionCI{

	@Override
	public void			triggerIntervention(
		AbsolutePosition position,
		String personId,
		TypeOfSAMURessources type
		) throws Exception
	{
		((SAMUActionCI)this.offering).
							triggerIntervention(position, personId, type);
	}
	

}
