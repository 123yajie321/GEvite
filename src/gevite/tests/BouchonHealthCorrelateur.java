package gevite.tests;

import java.io.Serializable;

import gevite.correlateur.HealthCorrelatorStateI;

public class BouchonHealthCorrelateur implements HealthCorrelatorStateI{

	
	@Override
	public boolean isMedicAvailable() {
		return true;
	}

	@Override
	public void triggerMedicCall(Serializable personId) {		
	}

	@Override
	public boolean inZone(String p) {
		return true;
	}

	@Override
	public boolean isAmbulanceAvailable() {
		return true;
	}

	@Override
	public void intervanetionAmbulance() {		
	}

	@Override
	public boolean isNotAmbulanceAvailable() {
		return true;
	}

	@Override
	public boolean procheSamuExiste() {
		return true;
	}

	@Override
	public boolean isNotMedicAvailable() {
		return true;
	}

	

}
