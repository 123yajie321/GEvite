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
import fr.sorbonne_u.cps.smartcity.utils.TimeManager;
import gevite.correlateur.CorrelateurSamu;
import gevite.correlateur.CorrelateurCepServicesOutboundPort;
import gevite.correlateur.CorrelatorStateI;
import gevite.correlateur.SamuCorrelatorStateI;
import gevite.evenement.EventBase;
import gevite.evenement.EventI;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.rule.samu.S1;
import gevite.tests.BouchonHealthCorrelateur;

public class S1Test {

	@Test
	void test() throws Exception {
		LocalTime time = LocalTime.of(8, 0);

		EventBase base=new EventBase();
		AlarmeSante aSante = new AlarmeSante(time);
		CorrelatorStateI bouchonCorrelateur = (CorrelatorStateI) new BouchonHealthCorrelateur();
		
		
		aSante.putProperty("type", TypeOfHealthAlarm.EMERGENCY);
		aSante.putProperty("position", new AbsolutePosition(1,2) );
		base.addEvent(aSante);
		
		S1 s1 = new S1();
		ArrayList<EventI> result = s1.match(base);
		
		assertTrue(result.get(0) instanceof AlarmeSante);
		
		assertEquals(TypeOfHealthAlarm.EMERGENCY, result.get(0).getPropertyValue("type"));
		assertEquals(new AbsolutePosition(1,2), result.get(0).getPropertyValue("position"));

		assertTrue(s1.correlate(result));
		assertTrue(s1.filter(result, bouchonCorrelateur));
		
		s1.update(result, base);
		assertFalse(base.appearsIn(aSante));
		
		
	}


}
