package gevite.evenement;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;

public abstract class AtomicEvent implements AtomicEventI {
	
	private HashMap<String, Serializable> listPoperty;
	private LocalTime createtime;
	
	
	public AtomicEvent() {
		listPoperty = new HashMap<String, Serializable>();
		createtime=LocalTime.now();
	}
	
	@Override
	public LocalTime getTimeStamp() {
		
		return createtime;
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
