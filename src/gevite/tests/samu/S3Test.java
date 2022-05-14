package gevite.tests.samu;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import gevite.correlateur.CorrelatorStateI;
import gevite.evenement.EventBase;
import gevite.evenement.EventI;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.rule.samu.S3;
import gevite.tests.BouchonHealthCorrelateur;

public class S3Test {
	
	@Test
	void test() throws Exception {
		EventBase base=new EventBase();
		LocalTime time = LocalTime.of(8, 0);

		AlarmeSante aSante = new AlarmeSante(time);
		CorrelatorStateI bouchonCorrelateur = (CorrelatorStateI) new BouchonHealthCorrelateur();

		
		aSante.putProperty("type", TypeOfHealthAlarm.MEDICAL);
		aSante.putProperty("position", new AbsolutePosition(1,2));
		base.addEvent(aSante);
		
		S3 s3 = new S3();
		ArrayList<EventI> result = s3.match(base);
		
		assertTrue(result.get(0) instanceof AlarmeSante);
		
		assertEquals(TypeOfHealthAlarm.MEDICAL, result.get(0).getPropertyValue("type"));
		assertEquals(new AbsolutePosition(1,2), result.get(0).getPropertyValue("position"));

		assertTrue(s3.correlate(result));
		assertTrue(s3.filter(result, bouchonCorrelateur));
		
		s3.update(result, base);
		assertFalse(base.appearsIn(aSante));
		
		
	}

}
