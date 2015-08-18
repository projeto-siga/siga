package br.gov.jfrj.siga.sr.reports;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.sr.model.SrAtendimento;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;

public class SrRelAtendimento extends RelatorioTemplate {

	public SrRelAtendimento(Map parametros) throws DJBuilderException {
		super(parametros);
		if (parametros.get("idlotaAtendenteIni") == null) {
			throw new DJBuilderException("Parâmetro Lotação não informado!");
		}
		if (parametros.get("dtIni").equals("")) {
			throw new DJBuilderException(
					"Parâmetro data inicial não informado!");
		}
		if (parametros.get("dtFim").equals("")) {
			throw new DJBuilderException(
					"Parâmetro data final não informado!");
		}
}

	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException, ColumnBuilderException {
		
		this.setTitle("Relatorio de Atendimentos");
		this.addColuna("Solicitacao", 25, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Data de Abertura", 20, RelatorioRapido.CENTRO, false);
		this.addColuna("Equipe Atendente", 15, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Pessoa Atendente", 18, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Data de Inicio Atendimento", 20, RelatorioRapido.CENTRO, false);
		this.addColuna("Data de Fim Atendimento", 20, RelatorioRapido.CENTRO, false);
		this.addColuna("Tipo de Atendimento", 28, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Proximo Atendente", 15, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Item Configuracao", 50, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Acao", 50, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Solicitacao Fechada?", 15, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Tempo de Atendimento", 26, RelatorioRapido.DIREITA, false);
		this.addColuna("Faixa", 18, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Totalizador (%)", 14, RelatorioRapido.DIREITA, false, Number.class);
		
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection processarDados() throws Exception {
		List<Object> listaFinal = new LinkedList<Object>();
		Set<SrAtendimento> listaAtendimento = null;
		Set<SrAtendimento> listaAtendimentoTotal = new TreeSet<SrAtendimento>();
		DateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		int quantRegistros = 1; int tamanhoLista = 0;
		
		try {
			//movimentacao de inicio de atendimento (tipo = 1), fechamento (tipo = 7), escalonamento (tipo = 24)  			
			Query query = SrSolicitacao.AR.em().createQuery("select sol from SrSolicitacao sol inner join sol.meuMarcaSet marca "
					+ "where marca.cpMarcador.idMarcador <> 45 and sol.idSolicitacao in ("
						+ "select s.idSolicitacao from SrSolicitacao s inner join s.meuMovimentacaoSet mov "
						+ "where s.hisDtIni between :dataIni and :dataFim and (mov.tipoMov = 1 or mov.tipoMov = 7 or mov.tipoMov = 24) "
						+ "and mov.lotaAtendente.idLotacaoIni = :idLotaAtendenteIni group by s.idSolicitacao) "
					+ "order by sol.hisDtIni");

			Date dtIni = formatter.parse((String) parametros.get("dtIni") + " 00:00:00");
			query.setParameter("dataIni", dtIni, TemporalType.TIMESTAMP);
			Date dtFim = formatter.parse((String) parametros.get("dtFim") + " 23:59:59");
			query.setParameter("dataFim", dtFim, TemporalType.TIMESTAMP);
			Long  idlotaAtendenteIni = (Long) parametros.get("idlotaAtendenteIni");
			query.setParameter("idLotaAtendenteIni", idlotaAtendenteIni);
	
			List<SrSolicitacao> lista = query.getResultList();
			for (SrSolicitacao sol : lista) {
				if (sol.isPai())
					listaAtendimento = sol.getAtendimentosSetSolicitacaoPai();
				else 
					listaAtendimento = sol.getAtendimentosSet();	
				for (SrAtendimento a : listaAtendimento) {
					if (a.getLotacaoAtendente().getIdInicial().equals(idlotaAtendenteIni) &&
						a.getTempoAtendimento() != null) {
						listaAtendimentoTotal.add(a);
					}
				}
			}
			tamanhoLista = listaAtendimentoTotal.size();
			for (SrAtendimento atendimento : listaAtendimentoTotal) {
				listaFinal.add(atendimento.getSolicitacao().getCodigo());
				listaFinal.add(atendimento.getSolicitacao().getHisDtIniDDMMYYYYHHMM());
				listaFinal.add(atendimento.getLotacaoAtendente().getSiglaCompleta());
				listaFinal.add(atendimento.getPessoaAtendente() != null ? 
						atendimento.getPessoaAtendente().getPrimeiroNomeEIniciais() : "");
				listaFinal.add(atendimento.getDataInicioDDMMYYYYHHMMSS());
				listaFinal.add(atendimento.getDataFinalDDMMYYYYHHMMSS());
				listaFinal.add(atendimento.getTipoAtendimento());
				listaFinal.add(atendimento.getLotacaoAtendenteDestino() != null ? 
						atendimento.getLotacaoAtendenteDestino().getSiglaCompleta() : "");
				listaFinal.add(atendimento.getItemConfiguracao());
				listaFinal.add(atendimento.getAcao());
				listaFinal.add(atendimento.getSolicitacao().isFechado() ? "Sim" : "Nao" );
				listaFinal.add(atendimento.getTempoAtendimento().toString());
				listaFinal.add(atendimento.getFaixa().descricao);
				listaFinal.add(Float.parseFloat(String.format(Locale.US,"%.2f", 
						(quantRegistros * 100f)/tamanhoLista)));
				quantRegistros++;
			}
		}
		catch (Exception e) {
			throw new Exception(e.getMessage());
			//throw new AplicacaoException("Erro ao gerar o relatorio de atendimentos"); 
		}
		return listaFinal;
	}
		
}