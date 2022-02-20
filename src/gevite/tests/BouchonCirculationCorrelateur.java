package gevite.tests;

import java.io.Serializable;

import gevite.correlateur.CirculationCorrelatorStateI;


public class BouchonCirculationCorrelateur implements CirculationCorrelatorStateI{

	@Override
	public void passerIntersectionP(Serializable p) {
		// TODO Auto-generated method stub
	}


	@Override
	public boolean estAvantDestination(Serializable destination) {
		if(destination.equals("df")) {
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	public void passerIntersectionN(Serializable p) {
		// TODO Auto-generated method stub
	}


	@Override
	public boolean estApresDestination(Serializable destination) {
		if(destination.equals("df")) {
			return true;
		}else {
			return false;
		}
	}



}
