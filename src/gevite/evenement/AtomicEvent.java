package gevite.evenement;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;

public abstract class AtomicEvent implements AtomicEventI {
	
	private HashMap<String, Serializable> listPoperty;
	
	
	public AtomicEvent() {
		listPoperty = new HashMap<String, Serializable>();
	}
	
	@Override
	public LocalTime getTimeStamp() {
		
		return LocalTime.now();
	}

	@Override
	public boolean hasProperty(String name) {
		
		return listPoperty.containsKey(name);
	}

	@Override
	public Serializable getProperty(String name) {
		
		return listPoperty.get(name);
	}

	@Override
	public Serializable putProperty(String name, Serializable value) {
		
		return listPoperty.put(name, value);
	}

	@Override
	public void removeProperty(String name) {
		listPoperty.remove(name);

	}
}
