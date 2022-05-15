package gevite.interfaces;

import java.io.Serializable;

import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;

public interface CirculationCorrelatorStateI extends CorrelatorStateI{
	
	/**c1*/
	public void changePriority(TypeOfTrafficLightPriority p,IntersectionPosition position) throws Exception;
	/*	
	public boolean estAvantDestination(Serializable destination)throws Exception;
	public void passerIntersectionN(Serializable priorite)throws Exception;
	public boolean estApresDestination(Serializable destination)throws Exception;
	*/


}
