package gevite.correlateur;

import java.io.Serializable;

public interface HealthCorrelatorStateI extends CorrelatorStateI
{
	/*s1*/
	public boolean inZone(String p);
	public boolean isAmbulanceAvailable();
	public void intervanetionAmbulance();
	
	/*s2*/
	public boolean isNotAmbulanceAvailable();

	
	/*s3*/
	public boolean isMedicAvailable(); 
	public void triggerMedicCall(Serializable personId);
	
}
