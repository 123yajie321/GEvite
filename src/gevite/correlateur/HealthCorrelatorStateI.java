package gevite.correlateur;

import java.io.Serializable;

public interface HealthCorrelatorStateI extends CorrelatorStateI
{
	public boolean isMedicAvailable(); 
	public void triggerMedicCall(Serializable personId);
	 
	
	public boolean inZone(String p);
	public boolean isAmbulanceAvailable();
	public void intervanetionAmbulance();
	
}
