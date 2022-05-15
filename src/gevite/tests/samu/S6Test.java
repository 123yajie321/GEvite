package gevite.tests.samu;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import gevite.correlateur.CorrelateurSamu;
import gevite.evenement.EventBase;
import gevite.evenement.EventI;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.atomique.samu.AmbulancesAvailable;
import gevite.interfaces.CorrelatorStateI;
import gevite.interfaces.SamuCorrelatorStateI;
import gevite.rule.samu.S1;
import gevite.rule.samu.S18;
import gevite.rule.samu.S6;
import gevite.tests.BouchonHealthCorrelateur;

public class S6Test {

	@Test
	void test() throws Exception {
		EventBase base=new EventBase();
		LocalTime time = LocalTime.of(8, 0);

		AlarmeSante aSante = new AlarmeSante(time);
		CorrelatorStateI bouchonCorrelateur = (CorrelatorStateI) new BouchonHealthCorrelateur();

		
		aSante.putProperty("type",TypeOfHealthAlarm.TRACKING);
		aSante.putProperty("position", new AbsolutePosition(1,2));
		base.addEvent(aSante);
		
		S6 s6 = new S6();
		ArrayList<EventI> result = s6.match(base);
		
		assertTrue(result.get(0) instanceof AlarmeSante);
		
		assertEquals(TypeOfHealthAlarm.TRACKING, result.get(0).getPropertyValue("type"));
		assertEquals(new AbsolutePosition(1,2), result.get(0).getPropertyValue("position"));

		assertTrue(s6.correlate(result));
		//assertTrue(s4.filter(result, bouchonCorrelateur)); // medecine is not available
		
		s6.update(result, base);
		assertFalse(base.appearsIn(aSante));
		
	}
}
