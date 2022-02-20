package gevite.tests.pompier;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import gevite.correlateur.CorrelatorStateI;
import gevite.evenement.EventBase;
import gevite.evenement.EventI;
import gevite.evenement.atomique.pompier.AlarmFeu;
import gevite.evenement.atomique.pompier.AlarmeFeuCause;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.complexe.pompier.PremiereAlarmFeu;
import gevite.rule.pompier.F1;
import gevite.rule.pompier.F2;
import gevite.tests.BouchonHealthCorrelateur;
import gevite.tests.BouchonPompierCorrelateur;

public class F2Test {
	
	@Test
	public void test(){
		
		EventBase base = new EventBase();
		AlarmFeu aFeu = new AlarmFeu();
		CorrelatorStateI bouchonCorrelateur = (CorrelatorStateI) new BouchonPompierCorrelateur();

		
		aFeu.putProperty("type", "maison");
		aFeu.putProperty("position", "p");
		base.addEvent(aFeu);
		
		F2 f2 = new F2();
		ArrayList<EventI> result = f2.match(base);
		
		assertTrue(result.get(0) instanceof AlarmFeu);
		
		assertEquals("maison", result.get(0).getPropertyValue("type"));
		assertEquals("p", result.get(0).getPropertyValue("position"));

		assertTrue(f2.correlate(result));
		assertTrue(f2.filter(result, bouchonCorrelateur));
		
		AlarmeFeuCause alarmeFeuCause = new AlarmeFeuCause();
		result.add(alarmeFeuCause);
		PremiereAlarmFeu premiereAlarmFeu = new PremiereAlarmFeu(result);
		base.addEvent(premiereAlarmFeu);
		f2.update(result, base);
		assertFalse(base.appearsIn(aFeu));
		assertTrue(base.appearsIn(premiereAlarmFeu));
		
		
		
	}

}
