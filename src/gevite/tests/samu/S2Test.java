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
import gevite.correlateur.CorrelateurCepServicesOutboundPort;
import gevite.correlateur.CorrelatorStateI;
import gevite.correlateur.SamuCorrelatorStateI;
import gevite.evenement.EventBase;
import gevite.evenement.EventI;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.rule.samu.S1;
import gevite.rule.samu.S2;
import gevite.tests.BouchonHealthCorrelateur;

public class S2Test {

	@Test
	void test() throws Exception {
		EventBase base=new EventBase();
		LocalTime time = LocalTime.of(8, 0);

		AlarmeSante aSante = new AlarmeSante(time);
		CorrelatorStateI bouchonCorrelateur = (CorrelatorStateI) new BouchonHealthCorrelateur();

		
		aSante.putProperty("type", TypeOfHealthAlarm.EMERGENCY);
		aSante.putProperty("position", new AbsolutePosition(1,2));
		base.addEvent(aSante);
		
		S2 s2 = new S2();
		ArrayList<EventI> result = s2.match(base);
		
		assertTrue(result.get(0) instanceof AlarmeSante);
		
		assertEquals(TypeOfHealthAlarm.EMERGENCY, result.get(0).getPropertyValue("type"));
		assertEquals(new AbsolutePosition(1,2), result.get(0).getPropertyValue("position"));

		assertTrue(s2.correlate(result));
		assertFalse(s2.filter(result, bouchonCorrelateur)); // ambulance is not available
		
		s2.update(result, base);
		assertFalse(base.appearsIn(aSante));
		
		
	}


}
