package gevite.emitteur;

import java.awt.Component;

import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;

public class SendEventPlugin extends AbstractPlugin {
	
	private static final long serialVersionUID=1L;
	
	protected EmitterSendOutboundPort emitterSendOutboundPort;
	
	public void installOn(ComponentI owner) throws Exception {
		
		super.installOn(owner);
		
		//this.addRequiredInterface(inter);
		
	}


}
