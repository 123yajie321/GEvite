package gevite.correlateur;

public interface PompierCorrelatorStateI extends CorrelatorStateI{
	
	/*f1*/
	public boolean inZone(String p);
	public boolean isEchelleDisponible();
	public void declancheAlarme();
	
	/*f2*/
	public boolean isCamionDisponible();
	
	/*f3*/
	public boolean isNotEchelleDisponible();



}
