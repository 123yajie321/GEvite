package gevite.tests.samu;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import gevite.correlateur.CorrelateurSamu;
import gevite.correlateur.CorrelateurSendCepOutboundPort;
import gevite.correlateur.CorrelatorStateI;
import gevite.correlateur.SamuCorrelatorStateI;
import gevite.evenement.EventBase;
import gevite.evenement.EventI;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.atomique.samu.SignaleManuel;
import gevite.rule.samu.S1;
import gevite.rule.samu.S7;
import gevite.tests.BouchonHealthCorrelateur;

public class S7Test {
	
	@Test
	void test() throws Exception {
		EventBase base=new EventBase();
		AlarmeSante aSante = new AlarmeSante();
		SignaleManuel sManuel = new SignaleManuel();
		CorrelatorStateI bouchonCorrelateur = (CorrelatorStateI) new BouchonHealthCorrelateur();
		
		aSante.putProperty("personId", "1");
		aSante.putProperty("type", TypeOfHealthAlarm.TRACKING);
		sManuel.putProperty("personId", "1");
		base.addEvent(aSante);
		base.addEvent(sManuel);
		
		S7 s7 = new S7();
		ArrayList<EventI> result = s7.match(base);
		assertTrue(result.get(0) instanceof AlarmeSante);
		assertTrue(result.get(1) instanceof SignaleManuel);

		
		assertTrue(result.get(0).hasProperty("personId"));
		assertTrue(result.get(1).hasProperty("personId"));

		assertEquals(TypeOfHealthAlarm.TRACKING, result.get(0).getPropertyValue("type"));
		assertEquals(result.get(0).getPropertyValue("personId"), result.get(1).getPropertyValue("personId"));

		assertTrue(s7.correlate(result));
		assertTrue(s7.filter(result, bouchonCorrelateur));
		
		s7.update(result, base);
		
		assertFalse(base.appearsIn(aSante) && base.appearsIn(sManuel));
		
	}

}
