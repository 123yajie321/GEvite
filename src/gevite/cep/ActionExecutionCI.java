package gevite.cep;

import java.io.Serializable;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface ActionExecutionCI extends RequiredCI, OfferedCI {
	
	public ResponseI execute(ActionI a,Serializable[] params);
	


}
