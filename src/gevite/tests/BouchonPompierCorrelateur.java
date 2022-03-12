package gevite.tests;

import java.io.Serializable;

import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFirefightingResource;
import gevite.correlateur.CorrelatorStateI;
import gevite.correlateur.SamuCorrelatorStateI;
import gevite.evenement.EventI;
import gevite.correlateur.PompierCorrelatorStateI;

public class BouchonPompierCorrelateur implements PompierCorrelatorStateI{

	@Override
	public boolean inZone(AbsolutePosition p) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEchelleDisponible() throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void declancheFirstAlarme(AbsolutePosition position, TypeOfFirefightingResource type) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isCamionDisponible() throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void declancheSecondAlarme(AbsolutePosition position) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean procheCaserneExiste() throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void propagerEvent(EventI event) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHighLadderTrucksBusy() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStandardTrucksBusy() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHighLadderTrucksAvailable() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStandardTRucksAvailable() throws Exception {
		// TODO Auto-generated method stub
		
	}

	

	
}
