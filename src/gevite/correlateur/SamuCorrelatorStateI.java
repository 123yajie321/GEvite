package gevite.correlateur;

import java.io.Serializable;
import java.util.ArrayList;

import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import gevite.evenement.EventI;

public interface SamuCorrelatorStateI extends CorrelatorStateI
{
	/*s1*/
	public boolean inZone(AbsolutePosition p)throws Exception;
	public boolean isAmbulanceAvailable()throws Exception;
	
	public void intervanetionAmbulance(	AbsolutePosition position,
			String personId,
			TypeOfSAMURessources type)throws Exception;
	
	/*s2*/
	public boolean samuNonSolliciteExiste(ArrayList<EventI>mathedEvents)throws Exception;
	public void propagerEvent(EventI event) throws Exception;


	
	/*s3*/
	public boolean isMedicAvailable()throws Exception; 
	public void triggerMedicCall(AbsolutePosition position,
			String personId,
			TypeOfSAMURessources type)throws Exception;
	
	

	/*S16*/
    public void setAmbulancesNoAvailable() throws Exception;
	
	
	/*S17*/
    public void setMedcinNoAvailable() throws Exception;
	
	
	/*S18*/
    public void setAmbulancesAvailable() throws Exception;
	
	
	/*S19*/
    public void setMedcinAvailable() throws Exception;
    
    
    public String getExecutorId() throws Exception;
	
}
