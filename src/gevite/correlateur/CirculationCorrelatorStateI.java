package gevite.correlateur;

import java.io.Serializable;

import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;

public interface CirculationCorrelatorStateI extends CorrelatorStateI{
	
	/*c1*/
	public void changePriority(TypeOfTrafficLightPriority p) throws Exception;
	
	/*c2*/
	/*
	public boolean estAvantDestination(Serializable destination)throws Exception;
	public void passerIntersectionN(Serializable priorite)throws Exception;

	c3
	public boolean estApresDestination(Serializable destination)throws Exception;
*/


}
