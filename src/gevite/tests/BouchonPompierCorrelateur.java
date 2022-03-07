package gevite.tests;

import java.io.Serializable;

import gevite.correlateur.CorrelatorStateI;
import gevite.correlateur.SamuCorrelatorStateI;
import gevite.correlateur.PompierCorrelatorStateI;

public class BouchonPompierCorrelateur implements PompierCorrelatorStateI{

	@Override
	public boolean inZone(String p) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEchelleDisponible() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void declancheAlarme() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isCamionDisponible() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isNotEchelleDisponible() {
		// TODO Auto-generated method stub
		return true;
	}

	
}
