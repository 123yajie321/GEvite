package gevite.interfaces;

import java.io.Serializable;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import gevite.actions.ActionI;
/**
 * The interface <code>ActionExecutionImplementationI</code> declares the signatures of the
 * implementation methods for  executeAction services.
 *
 * @author Yajie LIU, Zimeng ZHANG
 */
public interface ActionExecutionImplementationI {
	
	public ResponseI executeAction(ActionI a,Serializable[] params) throws Exception;

}
