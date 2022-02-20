package gevite.tests.samu;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import gevite.correlateur.CorrelatorStateI;
import gevite.evenement.EventBase;
import gevite.evenement.EventI;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.rule.samu.S3;
import gevite.tests.BouchonHealthCorrelateur;

public class S3Test {
	
	@Test
	void test() {
		EventBase base=new EventBase();
		AlarmeSante aSante = new AlarmeSante();
		CorrelatorStateI bouchonCorrelateur = (CorrelatorStateI) new BouchonHealthCorrelateur();

		
		aSante.putProperty("type", "medicale");
		aSante.putProperty("position", "p");
		base.addEvent(aSante);
		
		S3 s3 = new S3();
		ArrayList<EventI> result = s3.match(base);
		
		assertTrue(result.get(0) instanceof AlarmeSante);
		
		assertEquals("medicale", result.get(0).getPropertyValue("type"));
		assertEquals("p", result.get(0).getPropertyValue("position"));

		assertTrue(s3.correlate(result));
		assertTrue(s3.filter(result, bouchonCorrelateur));
		
		s3.update(result, base);
		assertFalse(base.appearsIn(aSante));
		
		
	}

}
