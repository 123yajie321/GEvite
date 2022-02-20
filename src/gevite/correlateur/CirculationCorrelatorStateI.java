package gevite.correlateur;

import java.io.Serializable;

public interface CirculationCorrelatorStateI extends CorrelatorStateI{
	
	/*c1*/
	public void passerIntersectionP(Serializable p);
	
	/*c2*/
	public boolean estAvantDestination(Serializable destination);
	public void passerIntersectionN(Serializable priorite);

	/*c3*/
	public boolean estApresDestination(Serializable destination);



}
