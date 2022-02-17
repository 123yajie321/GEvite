package gevite.tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import gevite.correlateur.Correlateur;
import gevite.correlateur.CorrelateurRegisterCepOutboundPort;
import gevite.correlateur.HealthCorrelatorStateI;
import gevite.evenement.EventBase;
import gevite.evenement.EventI;
import gevite.evenement.atomique.AlarmeSante;
import gevite.rule.S1;

public class S1Test {

	@Test
	void test() {
		EventBase base=new EventBase();
		AlarmeSante aSante = new AlarmeSante();
		//Correlateur correlateur = new Correlateur();
		HealthCorrelatorStateI samuStateI;
		
		aSante.putProperty("type", "urgence");
		aSante.putProperty("position", "p");
		base.addEvent(aSante);
		
		S1 s1 = new S1();
		ArrayList<EventI> result = s1.match(base);
		
		assertEquals("urgence", result.get(0).getPropertyValue("type"));
		assertEquals("p", result.get(0).getPropertyValue("position"));

		assertTrue(s1.correlate(result));
		assertTrue(s1.filter(result, samuStateI));
		
		
	}


}
