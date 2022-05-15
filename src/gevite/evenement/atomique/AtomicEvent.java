package gevite.evenement.atomique;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;

import fr.sorbonne_u.cps.smartcity.utils.TimeManager;

public abstract class AtomicEvent implements AtomicEventI {


	private static final long serialVersionUID = 1L;
	protected HashMap<String, Serializable> listPoperty;
	protected LocalTime createtime;
	
	
	
	
	public AtomicEvent(LocalTime createTime) {
		listPoperty = new HashMap<String, Serializable>();
		this.createtime=createTime;
	}
	
	public AtomicEvent() {
		listPoperty = new HashMap<String, Serializable>();
		createtime=TimeManager.get().getCurrentLocalTime();
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
	public Serializable getPropertyValue(String name) {
		
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
