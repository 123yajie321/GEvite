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
import gevite.evenement.atomique.circulation.DemandePriorite;
import gevite.evenement.atomique.circulation.PassageVehicule;
import gevite.evenement.atomique.pompier.AlarmFeu;
import gevite.evenement.atomique.pompier.AlarmeFeuCause;
import gevite.evenement.atomique.samu.AlarmeSante;
import gevite.evenement.complexe.circulation.AttentePassageComplexe;
import gevite.evenement.complexe.pompier.PremiereAlarmFeu;
import gevite.rule.circulation.C1;
import gevite.rule.pompier.F1;
import gevite.rule.pompier.F2;
import gevite.tests.BouchonCirculationCorrelateur;
import gevite.tests.BouchonHealthCorrelateur;
import gevite.tests.BouchonPompierCorrelateur;

public class C1Test {
	
	@Test
	public void test(){
		
		EventBase base = new EventBase();
		DemandePriorite dPriorite = new DemandePriorite();
		CorrelatorStateI bouchonCorrelateur = (CorrelatorStateI) new BouchonCirculationCorrelateur();

		dPriorite.putProperty("priorite", "p");
		dPriorite.putProperty("vehicule", "v");
		dPriorite.putProperty("destinationF", "df");
		
		base.addEvent(dPriorite);
		
		C1 c1 = new C1();
		ArrayList<EventI> result = c1.match(base);
		
		assertTrue(result.get(0) instanceof DemandePriorite);
		
		assertEquals("p", result.get(0).getPropertyValue("priorite"));
		assertEquals("v", result.get(0).getPropertyValue("vehicule"));
		assertEquals("df", result.get(0).getPropertyValue("destinationF"));


		assertTrue(c1.correlate(result));
		assertTrue(c1.filter(result, bouchonCorrelateur));
		
		
		PassageVehicule pVehicule = new PassageVehicule();
		result.add(pVehicule);
		AttentePassageComplexe attentePassageComplexe = new AttentePassageComplexe(result);
		base.addEvent(attentePassageComplexe);
		c1.update(result, base);
		assertFalse(base.appearsIn(dPriorite));
		assertTrue(base.appearsIn(attentePassageComplexe));
		
		
		
	}

}
