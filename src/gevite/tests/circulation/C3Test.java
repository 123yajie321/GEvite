package gevite.tests.circulation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import gevite.correlateur.CorrelatorStateI;
import gevite.evenement.EventBase;
import gevite.evenement.EventI;
import gevite.evenement.atomique.circulation.AttentePassage;
import gevite.evenement.atomique.circulation.PassageVehicule;
import gevite.evenement.atomique.pompier.AlarmFeu;
import gevite.evenement.atomique.pompier.AlarmeFeuCause;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.complexe.pompier.PremiereAlarmFeu;
import gevite.rule.circulation.C2;
import gevite.rule.circulation.C3;
import gevite.rule.pompier.F1;
import gevite.rule.pompier.F2;
import gevite.tests.BouchonCirculationCorrelateur;
import gevite.tests.BouchonHealthCorrelateur;
import gevite.tests.BouchonPompierCorrelateur;

public class C3Test {
	
	@Test
	public void test(){
		
		EventBase base = new EventBase();
		AttentePassage aPassage = new AttentePassage();
		PassageVehicule pVehicule = new PassageVehicule();
		CorrelatorStateI bouchonCorrelateur = (CorrelatorStateI) new BouchonCirculationCorrelateur();

		
		aPassage.putProperty("vehicule", "v");
		aPassage.putProperty("direction", "d");
		aPassage.putProperty("destinationF", "df");
		
		pVehicule.putProperty("vehicule", "v");
		pVehicule.putProperty("direction", "d");


		base.addEvent(aPassage);
		base.addEvent(pVehicule);

		
		C3 c3 = new C3();
		ArrayList<EventI> result = c3.match(base);
		
		assertTrue(result.get(0) instanceof AttentePassage);
		assertTrue(result.get(1) instanceof PassageVehicule);

		
		assertEquals("v", result.get(0).getPropertyValue("vehicule"));

		assertEquals("v", result.get(1).getPropertyValue("vehicule"));
		assertEquals("d", result.get(1).getPropertyValue("direction"));

		assertTrue(c3.correlate(result));
		assertTrue(c3.filter(result, bouchonCorrelateur));
		
		c3.update(result, base);
		assertFalse(base.appearsIn(aPassage));
		assertFalse(base.appearsIn(pVehicule));

		
		
		
	}

}
