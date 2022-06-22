package br.gov.jfrj.siga.ex.util.notificador.especifico;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import br.gov.jfrj.siga.cp.CpTipoMarcadorEnum;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.model.enm.CpServicosNotificacaoPorEmail;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMarca;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.ex.util.notificador.geral.Notificador;

/**
 * Essa classe tem a responsabilidade de realizar todas as 
 * rotinas no tocante quem  escolheu receber uma notificação por e-email assim também 
 * como se o órgão desse usuário está habilitado para essa configuração.
 * 
 */

public class ExNotificar {

	private ExEmail email;
	
	public ExNotificar() { }
	
	//Esse método notifica o(s) usuário(s) que foi incluido como cossignatário de um documento.
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
	
	//Esse método envia uma notificação para todos usuários da unidade que o documento foi tramitado.
	//Existem dois tipos de notificações aqui. A primeira é quando o documento possui algum marcador Local ou Geral.
	//A segunda é quando o documento simplesmente foi tramitado indepente se tem ou não marcadores.
	public void usuarioDiretamenteOuPelaUnidade (ExMovimentacao mov) {
		email = new ExEmail();
		Set<ExMobil> exMobils = mov.getExDocumento().getExMobilSet();
		Set<DpPessoa> pessoasLota = mov.getLotaResp().getDpPessoaLotadosSet();
		Set<CpMarcador> marcas = new HashSet<>();
		StringJoiner marcasDoDoc = new StringJoiner(", "); 
		if (mov.getResp() == null) {
			for (DpPessoa pessoa: pessoasLota) {
				if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, 
						pessoa.getLotacao(), CpServicosNotificacaoPorEmail.DOCMARC.getChave())
						&& Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, 
								pessoa.getLotacao(), CpServicosNotificacaoPorEmail.SIGACEMAIL.getChave())) { 
					for (ExMobil exMobil: exMobils) {
						for (ExMarca exMarca: exMobil.getExMarcaSet()) {
							if (exMarca.getCpMarcador().getIdFinalidade().getIdTpMarcador() == CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO 
									|| exMarca.getCpMarcador().getIdFinalidade().getIdTpMarcador() == CpTipoMarcadorEnum.TIPO_MARCADOR_GERAL) {
								marcas.add(exMarca.getCpMarcador());
							} 
						}
					}
					if (marcasDoDoc.length() == 0) {
						marcas.forEach(marc -> {
							marcasDoDoc.add(marc.getDescrMarcador());
						}); 
					} 
					email.enviarAoTramitarDocMarcado(
							pessoa, mov.getTitular(), mov.getExDocumento().getSigla(), marcasDoDoc + "");		
				} 
				if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, 
						pessoa.getLotacao(), CpServicosNotificacaoPorEmail.DOCTUN.getChave())
						&& Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, 
								pessoa.getLotacao(), CpServicosNotificacaoPorEmail.SIGACEMAIL.getChave())) 
					email.enviarAoTramitarDocParaUsuariosDaUnidade(
							mov.getLotaResp(), pessoa, mov.getExDocumento().getSigla());
			}
		}
		if (mov.getResp() != null) { 
			if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(mov.getResp(), 
					mov.getResp().getLotacao(), CpServicosNotificacaoPorEmail.DOCMARC.getChave())
					&& Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(mov.getResp(), 
							mov.getResp().getLotacao(), CpServicosNotificacaoPorEmail.SIGACEMAIL.getChave())) {
				for (ExMobil exMobil: exMobils) {
					for (ExMarca exMarca: exMobil.getExMarcaSet()) {
						if (exMarca.getCpMarcador().getIdFinalidade().getIdTpMarcador() == CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO 
								|| exMarca.getCpMarcador().getIdFinalidade().getIdTpMarcador() == CpTipoMarcadorEnum.TIPO_MARCADOR_GERAL) {
							marcas.add(exMarca.getCpMarcador());
						}
					}
				}
				if (marcasDoDoc.length() == 0) {
					marcas.forEach(marc -> {
						marcasDoDoc.add(marc.getDescrMarcador());
					});
				}
				email.enviarAoTramitarDocMarcado(
						mov.getResp(), mov.getTitular(), mov.getExDocumento().getSigla(), marcasDoDoc + "");	
			}
			if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(mov.getResp(), 
						mov.getResp().getLotacao(), CpServicosNotificacaoPorEmail.DOCTUSU.getChave())
					&& Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(mov.getResp(), 
							mov.getResp().getLotacao(), CpServicosNotificacaoPorEmail.SIGACEMAIL.getChave()))  
				email.enviarAoTramitarDocParaUsuario(
						mov.getResp(), mov.getTitular(), mov.getExDocumento().getSigla());
		}
	}
	
	//Esse método envia uma notificação para o responsável pela assinatura do documento.
	public void responsavelPelaAssinatura (ExDocumento doc, DpPessoa cadastrante) {
		email = new ExEmail();
		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(doc.getSubscritor(), 
				doc.getSubscritor().getLotacao(), CpServicosNotificacaoPorEmail.RESPASS.getChave())
				&& Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(doc.getSubscritor(), 
						doc.getSubscritor().getLotacao(), CpServicosNotificacaoPorEmail.SIGACEMAIL.getChave())) 
			email.enviarAoResponsavelPelaAssinatura(
					doc.getSubscritor(), cadastrante, doc.getSigla());
		
	}
	
}
