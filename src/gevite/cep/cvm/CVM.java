package gevite.cep.cvm;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import gevite.cep.CEPBusManagementCI;
import gevite.cepbus.CEPBus;
import gevite.connector.ConnectorCorrelateurRegister;
import gevite.connector.ConnectorEmitterRegister;
import gevite.correlateur.Correlateur;
import gevite.emitteur.Emetteur;

public class CVM extends AbstractCVM {

	public CVM() throws Exception {
	}
	
	@Override
	public void deploy() throws Exception {
		
		AbstractComponent.createComponent(CEPBus.class.getCanonicalName(), new Object[] {});
		String desURI = AbstractComponent.createComponent(Emetteur.class.getCanonicalName(), new Object[] {});
		this.doPortConnection(desURI, Emetteur.EROP_URI, CEPBus.CRIP_URI, ConnectorEmitterRegister.class.getCanonicalName());

		String correlateurURI = AbstractComponent.createComponent(Correlateur.class.getCanonicalName(), new Object[] {});
		//System.out.println(correlateurURI+";"+Correlateur.CCROP_URI+";"+CEPBus.CCRIP_URI);
		this.doPortConnection(correlateurURI,Correlateur.CCROP_URI, CEPBus.CRIP_URI, ConnectorCorrelateurRegister.class.getCanonicalName());

		super.deploy();
	}

	public static void main(String[] args){
		try {
			CVM cvm = new CVM();
			cvm.startStandardLifeCycle(5000L);
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
