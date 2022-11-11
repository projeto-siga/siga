package br.gov.jfrj.siga.ex.util.notificador.especifico;

import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import br.gov.jfrj.siga.cp.CpTipoMarcadorEnum;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.model.enm.CpServicosNotificacaoPorEmail;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeMovimentacao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.ExTipoFormaDoc;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExNotificar {

	private ExEmail email;
	
	public ExNotificar() { }
	
	public void cossignatario (ExDocumento doc, DpPessoa cadastrante) {
		email = new ExEmail();
		List<DpPessoa> cossignatarios = doc.getCosignatarios();  
		for (DpPessoa cossignatario : cossignatarios) {
			if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(cossignatario, cossignatario.getLotacao(), 
					CpServicosNotificacaoPorEmail.COSSIG.getChave()) 
					&& Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(cossignatario, 
							cossignatario.getLotacao(), CpServicosNotificacaoPorEmail.SIGACEMAIL.getChave())) {
				email.enviarAoCossignatario(cossignatario, cadastrante, doc.getSigla());
			}
		}
	}
	
	public void usuarioDiretamenteOuPelaUnidade (ExMovimentacao mov) {
		if (mov.getResp() == null) 
			tramitarParaUnidade(mov); 
		else if (mov.getResp() != null) 
			tramitarParaPessoa(mov);
	}

	private void tramitarParaPessoa(ExMovimentacao mov) {
		email = new ExEmail();
		String marcas = marcasDoDocumento(mov.getExDocumento().getExMobilSet());
		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(mov.getResp(), 
				mov.getResp().getLotacao(), CpServicosNotificacaoPorEmail.DOCMARC.getChave())
				&& Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(mov.getResp(), 
						mov.getResp().getLotacao(), CpServicosNotificacaoPorEmail.SIGACEMAIL.getChave())) 
			
			email.enviarAoTramitarDocMarcado(
					mov.getResp(), mov.getCadastrante(), mov.getExDocumento().getSigla(), marcas + "");	
		
		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(mov.getResp(), 
					mov.getResp().getLotacao(), CpServicosNotificacaoPorEmail.DOCTUSU.getChave())
				&& Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(mov.getResp(), 
						mov.getResp().getLotacao(), CpServicosNotificacaoPorEmail.SIGACEMAIL.getChave()))  
			email.enviarAoTramitarDocParaUsuario(
					mov.getResp(), mov.getCadastrante(), mov.getExDocumento().getSigla());
	}

	private void tramitarParaUnidade(ExMovimentacao mov) {
		email = new ExEmail();
		String marcas = marcasDoDocumento(mov.getExDocumento().getExMobilSet());
		for (DpPessoa pessoa: mov.getLotaResp().getDpPessoaLotadosSet()) {
			if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, 
					pessoa.getLotacao(), CpServicosNotificacaoPorEmail.DOCMARC.getChave())
					&& Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, 
							pessoa.getLotacao(), CpServicosNotificacaoPorEmail.SIGACEMAIL.getChave())) 
				email.enviarAoTramitarDocMarcado(
						pessoa, mov.getCadastrante(), mov.getExDocumento().getSigla(), marcas + "");		
			 
			if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, 
					pessoa.getLotacao(), CpServicosNotificacaoPorEmail.DOCTUN.getChave())
					&& Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, 
							pessoa.getLotacao(), CpServicosNotificacaoPorEmail.SIGACEMAIL.getChave())) 
				email.enviarAoTramitarDocParaUsuariosDaUnidade(
						mov.getLotaResp(), pessoa, mov.getExDocumento().getSigla());
		}
	}
	
	public void responsavelPelaAssinatura (ExDocumento doc, DpPessoa cadastrante) {
		email = new ExEmail();
		if (doc.getSubscritor() != null && Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(doc.getSubscritor(), 
				doc.getSubscritor().getLotacao(), CpServicosNotificacaoPorEmail.RESPASS.getChave())
				&& Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(doc.getSubscritor(), 
						doc.getSubscritor().getLotacao(), CpServicosNotificacaoPorEmail.SIGACEMAIL.getChave())) 
			email.enviarAoResponsavelPelaAssinatura(
					doc.getSubscritor(), cadastrante, doc.getSigla());
		
	}
	
	public static boolean verificaPermissaoParaNotificadorGeral(ExTipoFormaDoc tipoFormaDoc,
			ExPapel papel, DpPessoa pessoa, ITipoDeMovimentacao tipoMovimentacao) throws Exception {

		if(Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, 
				pessoa.getLotacao(), CpServicosNotificacaoPorEmail.SIGACEMAIL.getChave())) 
			return false;

		else if(!Ex.getInstance()
				.getConf()
				.podePorConfiguracao(
				tipoFormaDoc,
				papel,
				pessoa,
				tipoMovimentacao,
				ExTipoDeConfiguracao.NOTIFICAR_POR_EMAIL)) 
			return false;
		else 
			return true;
	}

	public static boolean verificaPermissaoParaNotificadorGeral(DpPessoa pessoa, DpLotacao lotacao, 
			ExModelo modelo, ITipoDeMovimentacao idTpMov) {

		if(Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, 
				pessoa.getLotacao(), CpServicosNotificacaoPorEmail.SIGACEMAIL.getChave())) 
			return false;

		else if(!Ex.getInstance().getConf().podePorConfiguracao(pessoa,
						lotacao, modelo,idTpMov, 
						ExTipoDeConfiguracao.NOTIFICAR_POR_EMAIL)) 
			return false;
		else 
			return true;

	}
	
	public String marcasDoDocumento(Set<ExMobil> exMobils) {  
		StringJoiner marcasConc = new StringJoiner(", "); 
		exMobils.forEach(exMobil -> exMobil.getExMarcaSet().stream()
			.filter(exMarca -> exMarca.getCpMarcador().getIdFinalidade().getIdTpMarcador() == CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO
					|| exMarca.getCpMarcador().getIdFinalidade().getIdTpMarcador() == CpTipoMarcadorEnum.TIPO_MARCADOR_GERAL)
			.forEach(exMarca -> marcasConc.add(exMarca.getCpMarcador().getDescrMarcador())));
			
		return marcasConc.toString();
	}
	
}
