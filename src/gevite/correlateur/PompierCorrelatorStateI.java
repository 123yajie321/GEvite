package gevite.correlateur;

import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFirefightingResource;
import gevite.evenement.EventI;

public interface PompierCorrelatorStateI extends CorrelatorStateI{
	
	/*f1*/
	public boolean inZone(AbsolutePosition p)throws Exception;
	public boolean isEchelleDisponible()throws Exception;
	public void declancheFirstAlarme(AbsolutePosition position,
									 TypeOfFirefightingResource type)throws Exception;
	
	/*f2*/
	public boolean isCamionDisponible()throws Exception;
	public void declancheSecondAlarme(AbsolutePosition position,
									 TypeOfFirefightingResource type)throws Exception;
	/*f3*/
	public boolean procheCaserneExiste()throws Exception;
	public void propagerEvent(EventI event) throws Exception;

}
