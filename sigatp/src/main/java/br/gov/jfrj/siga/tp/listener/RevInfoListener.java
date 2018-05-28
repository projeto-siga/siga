package br.gov.jfrj.siga.tp.listener;
import java.net.InetAddress;

import org.hibernate.envers.RevisionListener;

import br.com.caelum.vraptor.ioc.spring.VRaptorRequestHolder;
import br.gov.jfrj.siga.tp.auth.annotation.DadosAuditoria;
import br.gov.jfrj.siga.tp.model.RevInfo;
import br.gov.jfrj.siga.tp.util.Verificador;

public class RevInfoListener  implements RevisionListener  {
	@Override
	public void newRevision(Object revisionEntity) {
		RevInfo entity = (RevInfo) revisionEntity;
		
		if(Verificador.estamosExecutandoNoCron()) {
			entity.setMatricula(null);
			entity.setMotivoLog(null);
		} else {
			DadosAuditoria da = (DadosAuditoria) VRaptorRequestHolder.currentRequest().getRequest().getSession().getAttribute("dadosAuditoria");
			entity.setMatricula(da.getMatricula());
			entity.setMotivoLog(da.getMotivoLog());
		}

		try {
			entity.setEnderecoIp(InetAddress.getLocalHost().getHostAddress());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
