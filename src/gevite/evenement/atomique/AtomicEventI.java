package gevite.evenement.atomique;

import java.io.Serializable;

import gevite.evenement.EventI;

public interface AtomicEventI extends EventI {
	
	public Serializable putProperty(String name, Serializable value);
	public void removeProperty(String name);
	

}
