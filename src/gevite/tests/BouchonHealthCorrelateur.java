package gevite.tests;

import java.io.Serializable;

import gevite.correlateur.HealthCorrelatorStateI;

public class BouchonHealthCorrelateur implements HealthCorrelatorStateI{

	
	@Override
	public boolean isMedicAvailable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void triggerMedicCall(Serializable personId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean inZone(String p) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAmbulanceAvailable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void intervanetionAmbulance() {
		// TODO Auto-generated method stub
		
	}

	

}
