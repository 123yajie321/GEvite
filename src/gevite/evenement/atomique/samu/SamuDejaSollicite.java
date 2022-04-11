package gevite.evenement.atomique.samu;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;

import gevite.evenement.atomique.AtomicEvent;

public class SamuDejaSollicite extends AtomicEvent{
	private static final long serialVersionUID = 1L;

	public SamuDejaSollicite() {
		super();
	}

	public SamuDejaSollicite(LocalTime occurrence) {
		listPoperty = new HashMap<String, Serializable>();
		createtime = occurrence;	
	}

}
