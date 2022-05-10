package gevite.cep;

import java.io.Serializable;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import gevite.actions.ActionI;

public interface ActionExecutionImplementationCI {
	
	public ResponseI executeAction(ActionI a,Serializable[] params) throws Exception;
	


}
