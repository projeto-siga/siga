package br.gov.jfrj.siga.cd;


public class Cd {
	static private Cd instance;
	ICdAssinaturaDigital assinaturaDigital = null;

	public ICdAssinaturaDigital getAssinaturaDigital() {
		return assinaturaDigital;
	}

	public void setAssinaturaDigital(ICdAssinaturaDigital assinaturaDigital) {
		this.assinaturaDigital = assinaturaDigital;
	}

	@SuppressWarnings("unchecked")
	public static Cd getInstance() {
		if (instance == null) {
			synchronized (Cd.class) {
				if (instance == null) {
					instance = new Cd();
					try {
						instance.assinaturaDigital = (ICdAssinaturaDigital) Class
								.forName(
										SigaCdProperties.getAssinaturaDigital())
								.newInstance();
					} catch (Exception e) {
						instance.assinaturaDigital = new CdAssinaturaDigital();
					}
				}
			}
		}
		return instance;
	}
}
