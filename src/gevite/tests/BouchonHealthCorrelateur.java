package gevite.tests;

import java.io.Serializable;

import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import gevite.correlateur.SamuCorrelatorStateI;
import gevite.evenement.EventI;

public class BouchonHealthCorrelateur implements SamuCorrelatorStateI{

	@Override
	public boolean inZone(AbsolutePosition p) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAmbulanceAvailable() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void intervanetionAmbulance(AbsolutePosition position, String personId, TypeOfSAMURessources type)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean procheSamuExiste() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void propagerEvent(EventI event) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isMedicAvailable() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void triggerMedicCall(AbsolutePosition position, String personId, TypeOfSAMURessources type)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAmbulancesNoAvailable() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMedcinNoAvailable() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAmbulancesAvailable() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMedcinAvailable() throws Exception {
		// TODO Auto-generated method stub
		
	}

	
	
	

}
