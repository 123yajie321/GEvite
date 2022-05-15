package gevite.tests.samu;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import gevite.correlateur.CorrelateurSamu;
import gevite.evenement.EventBase;
import gevite.evenement.EventI;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.atomique.samu.AmbulancesAvailable;
import gevite.interfaces.CorrelatorStateI;
import gevite.interfaces.SamuCorrelatorStateI;
import gevite.rule.samu.S1;
import gevite.rule.samu.S18;
import gevite.tests.BouchonHealthCorrelateur;

public class S18Test {

	@Test
	void test() throws Exception {
		EventBase base=new EventBase();
		LocalTime time = LocalTime.of(8, 0);

		AmbulancesAvailable abAvailable = new AmbulancesAvailable(time);
		CorrelatorStateI bouchonCorrelateur = (CorrelatorStateI) new BouchonHealthCorrelateur();

		base.addEvent(abAvailable);
		
		S18 s18 = new S18();
		ArrayList<EventI> result = s18.match(base);
		
		assertTrue(result.get(0) instanceof AmbulancesAvailable);
		
		assertTrue(s18.correlate(result));
		assertTrue(s18.filter(result, bouchonCorrelateur));
		
		s18.update(result, base);
		assertFalse(base.appearsIn(abAvailable));
		
		
	}

}
