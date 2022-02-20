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
import gevite.rule.pompier.F3;
import gevite.tests.BouchonHealthCorrelateur;
import gevite.tests.BouchonPompierCorrelateur;

public class F3Test {
	
	@Test
	public void test(){
		
		EventBase base = new EventBase();
		AlarmFeu aFeu = new AlarmFeu();
		CorrelatorStateI bouchonCorrelateur = (CorrelatorStateI) new BouchonPompierCorrelateur();

		
		aFeu.putProperty("type", "immeuble");
		aFeu.putProperty("position", "p");
		base.addEvent(aFeu);
		
		F3 f3 = new F3();
		ArrayList<EventI> result = f3.match(base);
		
		assertTrue(result.get(0) instanceof AlarmFeu);
		
		assertEquals("immeuble", result.get(0).getPropertyValue("type"));
		assertEquals("p", result.get(0).getPropertyValue("position"));

		assertTrue(f3.correlate(result));
		assertFalse(f3.filter(result, bouchonCorrelateur));
		

		f3.update(result, base);
		assertFalse(base.appearsIn(aFeu));
		//assertTrue(base.appearsIn(premiereAlarmFeu));
		
		
		
	}

}
