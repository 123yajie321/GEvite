package gevite.interfaces;
import gevite.evenement.EventI;

public interface EventEmissionImplementationI  {
	
	public void sendEvent(String emitterURI, EventI event)throws Exception;
	public void sendEvents(String emitterURI, EventI[] events)throws Exception;

}
