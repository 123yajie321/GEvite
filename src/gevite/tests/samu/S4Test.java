package gevite.tests.samu;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import gevite.evenement.EventBase;
import gevite.evenement.EventI;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.interfaces.CorrelatorStateI;
import gevite.rule.samu.S3;
import gevite.rule.samu.S4;
import gevite.tests.BouchonHealthCorrelateur;

public class S4Test {
	
	@Test
	void test() throws Exception {
		EventBase base=new EventBase();
		LocalTime time = LocalTime.of(8, 0);

		AlarmeSante aSante = new AlarmeSante(time);
		CorrelatorStateI bouchonCorrelateur = (CorrelatorStateI) new BouchonHealthCorrelateur();

		
		aSante.putProperty("type",TypeOfHealthAlarm.MEDICAL);
		aSante.putProperty("position", new AbsolutePosition(1,2));
		base.addEvent(aSante);
		
		S4 s4 = new S4();
		ArrayList<EventI> result = s4.match(base);
		
		assertTrue(result.get(0) instanceof AlarmeSante);
		
		assertEquals(TypeOfHealthAlarm.MEDICAL, result.get(0).getPropertyValue("type"));
		assertEquals(new AbsolutePosition(1,2), result.get(0).getPropertyValue("position"));

		assertTrue(s4.correlate(result));
		//assertTrue(s4.filter(result, bouchonCorrelateur)); // medecine is not available
		
		s4.update(result, base);
		assertFalse(base.appearsIn(aSante));
		
		
	}


}
