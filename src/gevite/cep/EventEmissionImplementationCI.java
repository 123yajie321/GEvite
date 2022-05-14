package gevite.cep;
import gevite.evenement.EventI;

public interface EventEmissionImplementationCI  {
	
	public void sendEvent(String emitterURI, EventI event)throws Exception;
	public void sendEvents(String emitterURI, EventI[] events)throws Exception;

}
