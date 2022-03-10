package gevite.correlateur;

import java.io.Serializable;

import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;

public interface SamuCorrelatorStateI extends CorrelatorStateI
{
	/*s1*/
	public boolean inZone(AbsolutePosition p)throws Exception;
	public boolean isAmbulanceAvailable()throws Exception;
	
	public void intervanetionAmbulance(	AbsolutePosition position,
			String personId,
			TypeOfSAMURessources type)throws Exception;
	
	/*s2*/
	public boolean isNotAmbulanceAvailable()throws Exception;
	public boolean procheSamuExiste()throws Exception;


	
	/*s3*/
	public boolean isMedicAvailable()throws Exception; 
	public void triggerMedicCall(Serializable personId)throws Exception;
	
	/*s4*/
	public boolean isNotMedicAvailable()throws Exception; 
	

	/*S16*/
    public void setAmbulancesNoAvailable() throws Exception;
	
	
	/*S17*/
    public void setMedcinNoAvailable() throws Exception;
	
	
	/*S18*/
    public void setAmbulancesAvailable() throws Exception;
	
	
	/*S19*/
    public void setMedcinAvailable() throws Exception;
	
}
