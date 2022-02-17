package gevite.correlateur;

public interface HealthCorrelatorStateI extends CorrelatorStateI
{
	/*
	 * public boolean isMedicAvailable(); public void triggerMedicCall(String
	 * personId);
	 */
	
	public boolean inZone(String p);
	public boolean isAmbulanceAvailable();
	public void intervanetionAmbulance();
	
}
