/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/*
 * Criado em  21/12/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.dp;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.swing.text.MaskFormatter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Historico;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.parser.SiglaParser;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;

@SuppressWarnings("serial")
@Table(name = "corporativo.dp_pessoa")
@Entity
@SqlResultSetMapping(name = "scalar", columns = @ColumnResult(name = "dt"))
@Cache(region = CpDao.CACHE_CORPORATIVO, usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class DpPessoa extends AbstractDpPessoa implements Serializable,
		Selecionavel, Historico, Sincronizavel, Comparable, DpConvertableEntity {

	public static final ActiveRecord<DpPessoa> AR = new ActiveRecord<>(
			DpPessoa.class);
	
	@Transient
	private Long idSitConfiguracaoConfManual;

	@Transient
	private List <List<String>> listaLotacoes = new ArrayList <List<String>>();

	public DpPessoa() {

	}

	public Long getIdLotacao() {
		if (getLotacao() == null)
			return null;
		return getLotacao().getIdLotacao();
	}
	
	public Long getIdLotacaoIni() {
		if (getLotacao() == null)
			return null;
		return getLotacao().getIdLotacaoIni();
	}

	public boolean isFechada() {
		if (getDataFimPessoa() == null)
			return false;
		Set<DpPessoa> setPessoas = getPessoaInicial().getPessoasPosteriores();
		if (setPessoas != null)
			for (DpPessoa l : setPessoas)
				if (l.getDataFimPessoa() == null)
					return false;

		return true;
	}

	public Long getIdCargo() {
		if (getCargo() == null)
			return null;
		return getCargo().getIdCargo();
	}

	public Long getIdFuncaoConfianca() {
		if (getFuncaoConfianca() == null)
			return null;
		return getFuncaoConfianca().getIdFuncao();
	}

	public String iniciais(String s) {
		final StringBuilder sb = new StringBuilder(10);
		boolean f = true;

		s = s.replace(" E ", " ");
		s = s.replace(" DA ", " ");
		s = s.replace(" DE ", " ");
		s = s.replace(" DO ", " ");

		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (f) {
				sb.append(c);
				f = false;
			}
			if (c == ' ') {
				f = true;
			}
		}
		return sb.toString();
	}

	public Long getId() {
		return super.getIdPessoa();
	}

	public void setId(Long id) {
		setIdPessoa(id);
	}

	public String getSigla() {
		String sesbPessoa = getSesbPessoa();
		Long matricula = getMatricula();

		return sesbPessoa != null && matricula != null ? getSesbPessoa()
				+ getMatricula().toString() : "";
	}

	public String getIniciais() {
		// return iniciais(getNomePessoa());
		return getSigla();
	}

	public String getFuncaoString() {
		return getFuncaoConfianca() != null ? getFuncaoConfianca()
				.getNomeFuncao() : getCargo() != null ? getCargo()
				.getNomeCargo() : "";
	}

	public String getFuncaoStringIniciaisMaiusculas() {
		return getFuncaoConfianca() != null ? getFuncaoConfianca()
				.getDescricaoIniciaisMaiusculas()
				: getCargo() != null ? getCargo()
						.getDescricaoIniciaisMaiusculas() : "";
	}

	public String getDescricao() {
		return getNomePessoa();
	}

	public String getDescricaoIniciaisMaiusculas() {
		return Texto.maiusculasEMinusculas(getDescricao());
	}

	public String getDescricaoCompleta() {
		String funcaoStr = getFuncaoString();
		DpLotacao lotacao = getLotacao();
		return getNomePessoa() + ", " + (funcaoStr != null ? funcaoStr.toUpperCase() : "") + ", "
				+ (lotacao != null ? lotacao.getSiglaCompleta() : "");				
	}

	public String getDescricaoCompletaIniciaisMaiusculas() {
		return getDescricaoIniciaisMaiusculas() + ", "
				+ getFuncaoStringIniciaisMaiusculas() + ", "
				+ getLotacao().getSiglaCompleta();
	}

	static Pattern p1 = null;

	public void setSigla(String sigla) {	
		if (sigla == null) return;
		
		if (p1 == null) {
			Map<String, CpOrgaoUsuario> mapAcronimo = new TreeMap<String, CpOrgaoUsuario>();
			for (CpOrgaoUsuario ou : CpDao.getInstance().listarOrgaosUsuarios()) {
				mapAcronimo.put(ou.getAcronimoOrgaoUsu(), ou);
				mapAcronimo.put(ou.getSiglaOrgaoUsu(), ou);
			}
			String acronimos = "";
			for (String s : mapAcronimo.keySet()) {
				acronimos += "|" + s;
			}
			
			p1 = Pattern.compile("^(?<orgao>" + acronimos + ")?(?<numero>[0-9]+)$");
		}

		sigla = sigla.trim().toUpperCase();

		
		final Matcher m = p1.matcher(sigla);
		if (m.find()) {
			String orgao = m.group("orgao");
			String numero = m.group("numero");
			setSesbPessoa(orgao.toUpperCase());
			setMatricula(Long.parseLong(numero));
		}
	}

	public boolean equivale(Object other) {
		if (other == null || ((DpPessoa) other).getId() == null
				|| this.getId() == null)
			return false;
		if(this.getIdInicial() == null || ((DpPessoa)other).getIdInicial() == null)
			return false;
		return this.getIdInicial().longValue() == ((DpPessoa) other)
				.getIdInicial().longValue();
	}

	public Long getIdInicial() {
		return getIdPessoaIni();
	}

	public String getPadraoReferenciaInvertido() {
		if (getPadraoReferencia() != null && !getPadraoReferencia().equals("")) {
			// Caso o padrão de referência não esteja no formado utilizado pela
			// SJRJ.
			// Retorna o padrão cadastrado no BD sem inverter.
			try {
				String partes[] = getPadraoReferencia().split("-");
				String partesConcat = partes[1] + "-" + partes[0];
				if (partes.length > 2)
					partesConcat += "-" + partes[2];
				return partesConcat;
			} catch (Exception e) {
				return getPadraoReferencia();
			}
		} else
			return "";
	}

	@Override
	public String getSiglaCompleta() {
		return getSigla();
	}

	// Metodos necessarios para ser "Sincronizavel"
	//
	public Date getDataFim() {
		return getDataFimPessoa();
	}

	public Date getDataInicio() {
		return getDataInicioPessoa();
	}

	public String getDescricaoExterna() {
		return getDescricao();
	}

	public String getIdExterna() {
		return getIdePessoa();
	}

	public String getLoteDeImportacao() {
		return getOrgaoUsuario().getId().toString();
	}

	public int getNivelDeDependencia() {
		return SincronizavelSuporte.getNivelDeDependencia(this);
	}

	public boolean semelhante(Assemelhavel obj, int nivel) {
		return SincronizavelSuporte.semelhante(this, obj, nivel);
	}

	public void setDataFim(Date dataFim) {
		setDataFimPessoa(dataFim);
	}

	public void setDataInicio(Date dataInicio) {
		setDataInicioPessoa(dataInicio);
	}

	public void setIdExterna(String idExterna) {
		setIdePessoa(idExterna);
	}

	public void setIdInicial(Long idInicial) {
		setIdPessoaIni(idInicial);
	}

	public void setLoteDeImportacao(String loteDeImportacao) {
	}

	public boolean isBloqueada() throws AplicacaoException {
		return Cp.getInstance().getComp().isPessoaBloqueada(this);
	}

	//
	// Funcoes utilizadas nas formulas de inclusao em grupos de email.
	//

	public boolean tipoLotacaoSiglaIgual(String s) {
		if (this.getLotacao() == null)
			return false;
		if (this.getLotacao().getCpTipoLotacao() == null)
			return false;
		return this.getLotacao().getCpTipoLotacao().getSiglaTpLotacao()
				.toLowerCase().equals(s.toLowerCase());
	}

	public boolean tipoLotacaoSiglaContem(String s) {
		if (this.getLotacao() == null)
			return false;
		if (this.getLotacao().getCpTipoLotacao() == null)
			return false;
		return contem(this.getLotacao().getCpTipoLotacao().getSiglaTpLotacao(),
				"", s);
	}

	public boolean tipoLotacaoDescricaoContem(String s) {
		if (this.getLotacao() == null)
			return false;
		if (this.getLotacao().getCpTipoLotacao() == null)
			return false;
		return contem("", this.getLotacao().getCpTipoLotacao()
				.getDscTpLotacao(), s);
	}

	public boolean tipoLotacaoContem(String s) {
		if (this.getLotacao() == null)
			return false;
		if (this.getLotacao().getCpTipoLotacao() == null)
			return false;
		return contem(this.getLotacao().getCpTipoLotacao().getSiglaTpLotacao(),
				this.getLotacao().getCpTipoLotacao().getDscTpLotacao(), s);
	}

	public boolean lotacaoSiglaIgual(String s) {
		if (this.getLotacao() == null)
			return false;
		return this.getLotacao().getSigla().toLowerCase()
				.equals(s.toLowerCase());
	}

	public boolean lotacaoSiglaContem(String s) {
		if (this.getLotacao() == null)
			return false;
		return contem(this.getLotacao().getSigla(), "", s);
	}

	public boolean lotacaoDescricaoContem(String s) {
		if (this.getLotacao() == null)
			return false;
		return contem("", this.getLotacao().getDescricao(), s);
	}

	public boolean lotacaoContem(String s) {
		if (this.getLotacao() == null)
			return false;
		return contem(this.getLotacao().getSigla(), this.getLotacao()
				.getDescricao(), s);
	}

	public boolean cargoSiglaIgual(String s) {
		if (this.getCargo() == null)
			return false;
		return this.getCargo().getSigla().toLowerCase().equals(s.toLowerCase());
	}

	public boolean cargoSiglaContem(String s) {
		if (this.getCargo() == null)
			return false;
		return contem(this.getCargo().getSigla(), "", s);
	}

	public boolean cargoDescricaoContem(String s) {
		if (this.getCargo() == null)
			return false;
		return contem("", this.getCargo().getDescricao(), s);
	}

	public boolean cargoContem(String s) {
		if (this.getCargo() == null)
			return false;
		return contem(this.getCargo().getSigla(), this.getCargo()
				.getDescricao(), s);
	}

	public boolean funcaoSiglaIgual(String s) {
		if (this.getFuncaoConfianca() == null)
			return false;
		return this.getFuncaoConfianca().getSigla().toLowerCase()
				.equals(s.toLowerCase());
	}

	public boolean funcaoSiglaContem(String s) {
		if (this.getFuncaoConfianca() == null)
			return false;
		return contem(this.getFuncaoConfianca().getSigla(), "", s);
	}

	public boolean funcaoDescricaoContem(String s) {
		if (this.getFuncaoConfianca() == null)
			return false;
		return contem("", this.getFuncaoConfianca().getDescricao(), s);
	}

	public boolean funcaoContem(String s) {
		if (this.getFuncaoConfianca() == null)
			return false;
		return contem(this.getFuncaoConfianca().getSigla(), this
				.getFuncaoConfianca().getDescricao(), s);
	}

	private boolean contem(String sigla, String descricao, String s) {
		s = s.toLowerCase();
		return sigla.toLowerCase().contains(s)
				|| descricao.toLowerCase().contains(s);
	}

	public DpPessoa getPessoaAtual() {

		if (this.getDataFim() != null)
			return CpDao.getInstance().obterPessoaAtual(this);

		return this;
	}

	/**
	 * Devolve as lotacoes que o cadastrante pode acessar
	 * 
	 * @return Retorna as lotações que o cadastrante pode acessar
	 */
	public List <List<String>> getLotacoes() {
		
		if (listaLotacoes.size() == 0) {
			List<CpIdentidade> idsCpf = CpDao.getInstance().consultaIdentidadesCadastrante(getCpfPessoa().toString(), true);
			for (CpIdentidade identCpf : idsCpf) {
				List<String> listaUserLota = new ArrayList<String>();
				listaUserLota.add(identCpf.getNmLoginIdentidade());
				listaUserLota.add(identCpf.getDpPessoa().getPessoaAtual().getLotacao().getLotacaoAtual().getSiglaLotacao());
				if (identCpf.getDpPessoa().getPessoaAtual().getFuncaoConfianca() != null) {
					listaUserLota.add(identCpf.getDpPessoa().getPessoaAtual().getFuncaoConfianca().getNomeFuncao() + "/" +
							identCpf.getDpPessoa().getPessoaAtual().getCargo().getNomeCargo());
				} else {
					listaUserLota.add("");
				}
				listaLotacoes.add(listaUserLota);
			}
		}

		return listaLotacoes;
	}
	
	/**
	 * Seta as lotacoes que o cadastrante pode acessar
	 * 
	 * @param listaLota
	 *            * lista de lotacoes que o cadastrante pode acessar
	 */
	public void setLotacoes(List <List<String>> listaLota) {
		listaLotacoes = listaLota;
	}
	
	/**
	 * retorna se ativo na data
	 * 
	 * @param dt
	 *            * data quando ser saber se estava ativa
	 * @return true or false
	 */
	public boolean ativaNaData(Date dt) {
		if (dt == null)
			return this.getDataFim() == null;
		if (dt.before(this.getDataInicio()))
			return false;
		// dt >= DtIni
		if (this.getDataFim() == null)
			return true;
		if (dt.before(this.getDataFim()))
			return true;
		return false;
	}

	public String getNomeAbreviado() {
		if (getNomePessoa() == null)
			return "";
		String a[] = getNomePessoa().split(" ");

		String nomeAbreviado = "";
		for (String n : a) {
			if (nomeAbreviado.length() == 0)
				nomeAbreviado = n.substring(0, 1).toUpperCase()
						+ n.substring(1).toLowerCase();
			// else if (!"|DA|DE|DO|DAS|DOS|E|".contains("|" + n + "|"))
			// nomeAbreviado += " " + n.substring(0, 1) + ".";
		}
		return nomeAbreviado;
	}

	public String getPrimeiroNomeEIniciais() {
		if (getNomePessoa() == null)
			return "";
		String a[] = getNomePessoa().split(" ");

		String nomeAbreviado = "";
		for (String n : a) {
			if (n.trim().length() != 0) {
				if (nomeAbreviado.length() == 0)
					nomeAbreviado = n.substring(0, 1).toUpperCase()
							+ n.substring(1).toLowerCase();
				else if (!"|DA|DE|DO|DAS|DOS|E|".contains("|" + n + "|"))
					nomeAbreviado += " " + n.substring(0, 1) + ".";
			}
		}
		return nomeAbreviado;
	}

	public Long getHisIdIni() {
		return getIdPessoaIni();
	}

	public void setHisIdIni(Long hisIdIni) {
		setIdPessoaIni(hisIdIni);
	}

	public Date getHisDtIni() {
		return getDataInicioPessoa();
	}

	public void setHisDtIni(Date hisDtIni) {
		setDataInicioPessoa(hisDtIni);
	}

	public Date getHisDtFim() {
		return getDataFimPessoa();
	}

	public void setHisDtFim(Date hisDtFim) {
		setDataFimPessoa(hisDtFim);
	}

	public Integer getHisAtivo() {
		return getHisDtFim() != null ? 1 : 0;
	}

	public void setHisAtivo(Integer hisAtivo) {
		//
	}

	public String getEmailPessoaAtual() {
		try {
			return getPessoaAtual().getEmailPessoa();
		} catch (Exception e) {
			return getEmailPessoa();
		}

	}
	
	public String getEmailPessoaAtualParcialmenteOculto() {
		return getEmailPessoaAtual().substring(0,4) + "*********@***" + getEmailPessoaAtual().substring(getEmailPessoaAtual().length()-5,getEmailPessoaAtual().length());
	}

	/**
	 * Retorna a data de inicio da pessoa no formato dd/mm/aa HH:MI:SS, por
	 * exemplo, 01/02/10 14:10:00.
	 * 
	 * @return Data de inicio da pessoa no formato dd/mm/aa HH:MI:SS, por
	 *         exemplo, 01/02/10 14:10:00.
	 * 
	 */
	public String getDtInicioPessoaDDMMYYHHMMSS() {
		if (getDataInicioPessoa() != null) {
			final SimpleDateFormat df = new SimpleDateFormat(
					"dd/MM/yy HH:mm:ss");
			return df.format(getDataInicioPessoa());
		}
		return "";
	}

	/**
	 * Retorna a data de fim da pessoa no formato dd/mm/aa HH:MI:SS, por
	 * exemplo, 01/02/10 14:10:00.
	 * 
	 * @return Data de fim da pessoa no formato dd/mm/aa HH:MI:SS, por exemplo,
	 *         01/02/10 14:10:00.
	 * 
	 */
	public String getDtFimPessoaDDMMYYHHMMSS() {
		if (getDataFimPessoa() != null) {
			final SimpleDateFormat df = new SimpleDateFormat(
					"dd/MM/yy HH:mm:ss");
			return df.format(getDataFimPessoa());
		}
		return "";
	}
	
	public String getCpfFormatado() {
    	MaskFormatter mf;
		try {
			mf = new MaskFormatter("###.###.###-##");
	        mf.setValueContainsLiteralCharacters(false);
	        return mf.valueToString(String.format("%011d", getCpfPessoa()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getCpfPessoa().toString();
    }
    
    public String getDtNascimentoDDMMYYYY() {
        if (getDataNascimento() != null) {
                final SimpleDateFormat df = new SimpleDateFormat(
                                "dd/MM/yyyy");
                return df.format(getDataNascimento());
        }
        return "";
    }
    
    public String getDataExpedicaoIdentidadeDDMMYYYY() {
        if (getDataExpedicaoIdentidade() != null) {
                final SimpleDateFormat df = new SimpleDateFormat(
                                "dd/MM/yyyy");
                return df.format(getDataExpedicaoIdentidade());
        }
        return "";
    }


	public int compareTo(Object o) {
		DpPessoa other = (DpPessoa) o;

		return getNomePessoa().compareTo(other.getNomePessoa());
	}

	@Override
	public String toString() {
		return getSiglaCompleta();
	}

	/**
	 * Metodo que filtra o pessoaPosteriores para que apareca somente o
	 * historico com informacoes corporativas, comparando uma linha da lista com
	 * a proxima para verificar se ocorreu alguma alteracao de lotacao, funcao
	 * ou padrao.
	 * 
	 * @return lista com historico referentes as seguintes informacoes:
	 *         lotacoes, funcao e padrao.
	 */
	public List<DpPessoa> getHistoricoInfoCorporativas() {
		// transforma um treeSet (pessoaPosteriores) em um list para que se
		// possa percorrer a lista do fim para o comeco
		List<DpPessoa> listaPessoaPosterioresA = new ArrayList<DpPessoa>(
				getPessoaInicial().getPessoasPosteriores());
		List<DpPessoa> listaPessoaPosterioresB = listaPessoaPosterioresA;
		List<DpPessoa> listaHistoricoPessoa = new ArrayList<DpPessoa>();
		// define que o iterator comeca pelo fim da lista
		ListIterator<DpPessoa> itPessoaPosteriorA = listaPessoaPosterioresA
				.listIterator(listaPessoaPosterioresA.size());
		ListIterator<DpPessoa> itPessoaPosteriorB = listaPessoaPosterioresB
				.listIterator(listaPessoaPosterioresB.size());
		DpPessoa pessoaPost = null;
		DpPessoa pessoaHist = null;

		if (itPessoaPosteriorB.hasPrevious()) {
			pessoaHist = itPessoaPosteriorB.previous();
			listaHistoricoPessoa.add(pessoaHist);
		}
		while (itPessoaPosteriorB.hasPrevious()) {
			pessoaPost = itPessoaPosteriorA.previous();
			pessoaHist = itPessoaPosteriorB.previous();
			// verifica se a lotacao da lista listaPessoaPosterioresA e a mesma
			// que da lista listaPessoaPosterioresB,
			// que esta um registro a frente (linha seguinte)
			// somente adiciona na lista listaHistoricoPessoa,que sera retornada
			// pelo metodo, caso as lotacoes sejam diferentes
			if (!pessoaHist.getLotacao().getSigla()
					.equals(pessoaPost.getLotacao().getSigla()))
				listaHistoricoPessoa.add(pessoaHist);
			// verifica se o padrao de referencia da lista
			// listaPessoaPosterioresA e o mesmo que da lista
			// listaPessoaPosterioresB
			else if ((pessoaHist.getPadraoReferencia() == null ^ pessoaPost
					.getPadraoReferencia() == null)
					|| ((pessoaHist.getPadraoReferencia() != null && pessoaPost
							.getPadraoReferencia() != null) && !pessoaHist
							.getPadraoReferencia().equals(
									pessoaPost.getPadraoReferencia())))
				listaHistoricoPessoa.add(pessoaHist);
			// verifica se a funcao de confianca da lista
			// listaPessoaPosterioresA e a mesma que da lista
			// listaPessoaPosterioresB
			else if ((pessoaHist.getFuncaoConfianca() == null ^ pessoaPost
					.getFuncaoConfianca() == null)
					|| ((pessoaHist.getFuncaoConfianca() != null && pessoaPost
							.getFuncaoConfianca() != null) && !pessoaHist
							.getFuncaoConfianca()
							.getNomeFuncao()
							.equals(pessoaPost.getFuncaoConfianca()
									.getNomeFuncao())))
				listaHistoricoPessoa.add(pessoaHist);
		}
		Collections.reverse(listaHistoricoPessoa);
		return listaHistoricoPessoa;
	}

	public Long getIdSitConfiguracaoConfManual() {
		return idSitConfiguracaoConfManual;
	}

	public void setIdSitConfiguracaoConfManual(Long idSitConfiguracaoConfManual) {
		this.idSitConfiguracaoConfManual = idSitConfiguracaoConfManual;
	}
	
	public boolean isUsuarioExterno() {
		return ((this.getOrgaoUsuario().getIsExternoOrgaoUsu() != null
				&& this.getOrgaoUsuario().getIsExternoOrgaoUsu() == 1)
				|| (this.getLotacao() != null && this.getLotacao().getIsExternaLotacao() != null
						&& this.getLotacao().getIsExternaLotacao() == 1));
	}

	@Override
	public String getSiglaDePessoaEOuLotacao() {
		return SiglaParser.makeSigla(this, this.getLotacao());
	}
}
