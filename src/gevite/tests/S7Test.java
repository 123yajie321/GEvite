package gevite.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import gevite.correlateur.Correlateur;
import gevite.correlateur.HealthCorrelatorStateI;
import gevite.evenement.EventBase;
import gevite.evenement.EventI;
import gevite.evenement.atomique.AlarmeSante;
import gevite.evenement.atomique.SignaleManuel;
import gevite.rule.S1;
import gevite.rule.S7;

public class S7Test {
	
	@Test
	void test() {
		EventBase base=new EventBase();
		AlarmeSante aSante = new AlarmeSante();
		SignaleManuel sManuel = new SignaleManuel();
		//Correlateur correlateur = new Correlateur();
		HealthCorrelatorStateI samuStateI;
		
		aSante.putProperty("personId", "1");
		aSante.putProperty("type", "tracking");
		sManuel.putProperty("personId", "2");
		base.addEvent(aSante);
		base.addEvent(sManuel);
		
		S7 s7 = new S7();
		ArrayList<EventI> result = s7.match(base);
		
		assertTrue(result.get(0).hasProperty("personId"));
		assertTrue(result.get(1).hasProperty("personId"));

		assertEquals("tracking", result.get(0).getPropertyValue("type"));
		assertEquals(result.get(0).getPropertyValue("personId"), result.get(1).getPropertyValue("personId"));

		assertTrue(s7.correlate(result));
		assertTrue(s7.filter(result, samuStateI));
		
		
	}

}
