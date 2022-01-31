package gevite.evenement;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * @author	<a href="mailto:Yajie.Liu@etu.sorbonne-universite.fr">Yajie LIU</a>
 * @author <a href="mailto:Zimeng.Zhang@etu.sorbonne-universite.fr">Zimeng ZHANG</a>
 */

public interface EventI extends Serializable {
	public LocalTime getTimeStamp();
	public boolean  hasProperty(String name);
	Serializable getProperty(String name);
}

