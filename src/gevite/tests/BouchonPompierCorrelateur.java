package gevite.tests;

import java.io.Serializable;
import java.util.ArrayList;

import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFirefightingResource;
import gevite.evenement.EventI;
import gevite.interfaces.CorrelatorStateI;
import gevite.interfaces.PompierCorrelatorStateI;
import gevite.interfaces.SamuCorrelatorStateI;

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

	@Override
	public boolean caserneNonSolliciteExiste(ArrayList<EventI> mathedEvents) throws Exception {
		return true;
	}

	@Override
	public String getExecutorId() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void declancheGeneralAlarme(AbsolutePosition position) throws Exception {
		// TODO Auto-generated method stub
		
	}


	
}
