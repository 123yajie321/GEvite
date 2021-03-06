package gevite.interfaces;

import java.util.ArrayList;

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
	public void declancheSecondAlarme(AbsolutePosition position)throws Exception;

	/*f3*/
	public boolean caserneNonSolliciteExiste(ArrayList<EventI>mathedEvents)throws Exception;
	public void propagerEvent(EventI event) throws Exception;
	
	
	/*f9*/
	public void declancheGeneralAlarme(AbsolutePosition position)throws Exception;

	
	/*f15*/
	public void setHighLadderTrucksBusy()throws Exception;
	/*f16*/
	public void setStandardTrucksBusy()throws Exception;
	/*f17*/
	public void setHighLadderTrucksAvailable()throws Exception;
	/*f18*/
	public void setStandardTRucksAvailable()throws Exception;

    public String getExecutorId() throws Exception;

	


}
