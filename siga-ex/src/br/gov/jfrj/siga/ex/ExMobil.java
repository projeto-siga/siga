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
package br.gov.jfrj.siga.ex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;

@Entity
@Table(name = "EX_MOBIL")
public class ExMobil extends AbstractExMobil implements Serializable,
		Selecionavel, Comparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(ExMobil.class);

	private class CronologiaComparator implements Comparator<ExMovimentacao> {

		public int compare(ExMovimentacao o1, ExMovimentacao o2) {
			try {
				int i = o2.getDtIniMov().compareTo(o1.getDtIniMov());
				if (i != 0)
					return i;
				i = o2.getIdMov().compareTo(o1.getIdMov());
				return i;
			} catch (final Exception ex) {
				return 0;
			}
		}
	}

	/**
	 * Retorna A última movimentação de um Mobil.
	 * 
	 * @return Última movimentação de um Mobil.
	 * 
	 */
	public ExMovimentacao getUltimaMovimentacao() {
		final Set<ExMovimentacao> movs = getExMovimentacaoSet();

		ExMovimentacao mov = null;
		if (movs != null)
			for (final Object element : movs) {
				final ExMovimentacao movIterate = (ExMovimentacao) element;
				mov = movIterate;
			}
		return mov;
	}

	/**
	 * Retorna A última movimentação de um Mobil de acordo com um tipo
	 * específico de movimentação.
	 * 
	 * @param tpMov
	 * 
	 * @return Última movimentação de um Mobil de acordo com um tipo específico
	 *         de movimentação.
	 * 
	 */
	public ExMovimentacao getUltimaMovimentacao(long tpMov) {
		final Set<ExMovimentacao> movs = getExMovimentacaoSet();

		ExMovimentacao mov = null;
		if (movs != null)
			for (final Object element : movs) {
				final ExMovimentacao movIterate = (ExMovimentacao) element;
				if (movIterate.getExTipoMovimentacao().getIdTpMov()
						.equals(tpMov))
					mov = movIterate;
			}
		return mov;
	}

	/**
	 * Retorna A última movimentação não cancelada de um mobil, com base nos
	 * parâmetros de uma outra movimentação.
	 * 
	 * @return Última movimentação não cancelada de um Mobil.
	 * 
	 */
	public ExMovimentacao getUltimaMovimentacaoNaoCancelada() {
		return getUltimaMovimentacaoNaoCancelada(null);
	}

	/**
	 * Retorna A última movimentação não cancelada de um Mobil.
	 * 
	 * @return Última movimentação não cancelada de um Mobil.
	 * 
	 */
	public ExMovimentacao getUltimaMovimentacaoNaoCancelada(
			ExMovimentacao movParam) {
		final Set<ExMovimentacao> movs = getExMovimentacaoSet();
		if (movs == null)
			return null;

		ExMovimentacao mov = null;
		if (movs == null)
			return null;
		for (final Object element : movs) {
			final ExMovimentacao movIterate = (ExMovimentacao) element;

			if (movIterate.getExTipoMovimentacao().getIdTpMov() != ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO
					&& movIterate.getExMovimentacaoCanceladora() == null
					&& ((movParam == null) || (movIterate.getDtMov().equals(
							movParam.getDtMov()) && movIterate
							.getExTipoMovimentacao()
							.getIdTpMov()
							.equals(movParam.getExTipoMovimentacao()
									.getIdTpMov())))) {
				mov = movIterate;
			}
		}
		return mov;
	}

	/**
	 * Retorna A penúltima movimentação não cancelada de um Mobil.
	 * 
	 * @return Penúltima movimentação não cancelada de um Mobil.
	 * 
	 */
	public ExMovimentacao getPenultimaMovimentacaoNaoCancelada() {
		final Set<ExMovimentacao> movs = getExMovimentacaoSet();

		ExMovimentacao mov = null;
		ExMovimentacao penMov = null;
		for (final Object element : movs) {
			final ExMovimentacao movIterate = (ExMovimentacao) element;

			if (movIterate.getExTipoMovimentacao().getIdTpMov() != ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO
					&& movIterate.getExMovimentacaoCanceladora() == null) {
				if (mov == null && penMov == null) {
					mov = movIterate;
				} else {
					penMov = mov;
					mov = movIterate;
				}
			}
		}
		return penMov;
	}

	/**
	 * Retorna as movimentações de um Mobil de acordo com um tipo específico de
	 * movimentação.
	 * 
	 * @param tpMov
	 * 
	 * @return Lista de movimentações de um Mobil de acordo com um tipo
	 *         específico de movimentação.
	 * 
	 */
	public List<ExMovimentacao> getMovimentacoesPorTipo(long tpMov) {

		final Set<ExMovimentacao> movs = getExMovimentacaoSet();
		List<ExMovimentacao> movsTp = new ArrayList<ExMovimentacao>();

		if (movs != null)
			for (final ExMovimentacao m : movs) {
				if (m.getExTipoMovimentacao().getIdTpMov().equals(tpMov))
					movsTp.add(m);
			}
		return movsTp;
	}

	/**
	 * Verifica se um Mobil é do tipo Geral.
	 * 
	 * @return Verdadeiro se um Mobil for do tipo Geral e Falso caso contrário.
	 * 
	 */
	public boolean isGeral() {
		/*
		 * bruno.lacerda@avantiprima.com.br - 30/07/2012 Verifica se
		 * getExTipoMobil() é diferente de nulo antes de chamar o método
		 * getIdTipoMobil() do objeto
		 */
		// return getExTipoMobil().getIdTipoMobil() ==
		// ExTipoMobil.TIPO_MOBIL_GERAL;
		return getExTipoMobil() != null
				&& getExTipoMobil().getIdTipoMobil() == ExTipoMobil.TIPO_MOBIL_GERAL;
	}

	/**
	 * Verifica se um Mobil é do tipo Via.
	 * 
	 * @return Verdadeiro se um Mobil for do tipo Via e Falso caso contrário.
	 * 
	 */
	public boolean isVia() {
		/*
		 * bruno.lacerda@avantiprima.com.br - 30/07/2012 Verifica se
		 * getExTipoMobil() é diferente de nulo antes de chamar o método
		 * getIdTipoMobil() do objeto
		 */
		// return getExTipoMobil().getIdTipoMobil() ==
		// ExTipoMobil.TIPO_MOBIL_VIA;
		return getExTipoMobil() != null
				&& getExTipoMobil().getIdTipoMobil() == ExTipoMobil.TIPO_MOBIL_VIA;
	}

	/**
	 * Verifica se um Mobil é do tipo Volume.
	 * 
	 * @return Verdadeiro se um Mobil for do tipo Volume e Falso caso contrário.
	 * 
	 */
	public boolean isVolume() {
		/*
		 * bruno.lacerda@avantiprima.com.br - 30/07/2012 Verifica se
		 * getExTipoMobil() é diferente de nulo antes de chamar o método
		 * getIdTipoMobil() do objeto
		 */
		// return getExTipoMobil().getIdTipoMobil() ==
		// ExTipoMobil.TIPO_MOBIL_VOLUME;
		return getExTipoMobil() != null
				&& getExTipoMobil().getIdTipoMobil() == ExTipoMobil.TIPO_MOBIL_VOLUME;
	}

	/**
	 * Verifica se um Mobil está Cancelado.
	 * 
	 * @return Verdadeiro se um Mobil está Cancelado e Falso caso contrário.
	 * 
	 */
	public boolean isCancelada() {
		/*
		 * bruno.lacerda@avantiprima.com.br - 30/07/2012 Verifica se
		 * getExTipoMobil() é diferente de nulo antes de chamar o método
		 * getIdTipoMobil() do objeto
		 */
		// return (getExTipoMobil().getIdTipoMobil() ==
		// ExTipoMobil.TIPO_MOBIL_VIA && getUltimaMovimentacaoNaoCancelada() ==
		// null);
		return getExTipoMobil() != null
				&& getExTipoMobil().getIdTipoMobil() == ExTipoMobil.TIPO_MOBIL_VIA
				&& getUltimaMovimentacaoNaoCancelada() == null;
	}

	/**
	 * Retorna a descrição do documento relacionado ao Mobil como um link em
	 * html.
	 * 
	 * @return A descrição do documento relacionado ao Mobil como um link em
	 *         html.
	 * 
	 */
	public String getDescricao() {
		final Integer popW = 700;
		final Integer popH = 500;
		final String winleft = "(screen.width - " + popW + ") / 2";
		final String winUp = "(screen.height - " + popH + ") / 2";
		final String winProp = "'width=" + popW + ",height=" + popH
				+ ",left='+" + winleft + "+',top='+" + winUp
				+ "+',scrollbars=yes,resizable'";
		String s = "<a href=\"javascript:void(0)\" onclick=\"window.open('/sigaex/expediente/doc/exibir.action?popup=true&idmob="
				+ getIdMobil();

		String descricaoCurta = null;
		ExDocumento exDocumento = getExDocumento();

		if (exDocumento != null) {
			try {
				descricaoCurta = exDocumento.getDescrCurta();
			} catch (Exception e) {
				log.warn(
						"[getDescricao] - Não foi possível recuperar a descrição curta do Documento. Retornando descrição em branco.",
						e);
				descricaoCurta = "";
			}
		} else {
			log.warn("[getDescricao] - O Documento informado é nulo.");
		}

		if (descricaoCurta == null) {
			descricaoCurta = "";
		}

		s = s + "', 'documento', " + winProp + ")\">" + descricaoCurta + "</a>";

		return s;
	}

	/**
	 * Retorna a sigla mais a descrição do documento relacionado ao Mobil.
	 * 
	 * @return A sigla mais a descrição do documento relacionado ao Mobil.
	 * 
	 */
	public String getSiglaEDescricaoCompleta() {
		return getSigla() + " - " + getDescricaoCompleta();

	}

	/**
	 * Retorna a descrição do tipo de documento mais o código do documento e a
	 * descrição do documento.
	 * 
	 * @return A descrição do tipo de documento mais o código do documento e a
	 *         descrição do documento.
	 * 
	 */
	public String getNomeCompleto() {
		String s = getExDocumento().getExFormaDocumento().getExTipoFormaDoc()
				.getDescricao()
				+ ": " + getExDocumento().getCodigoString() + ": "; // getNomeCompleto();
		s += getDescricaoCompleta();
		return s;
	}

	/**
	 * Retorna o número de sequência, a descrição de tipo de mobil mais a
	 * descrição do tipo de destinação.
	 * 
	 * @return O número de sequência, a descrição de tipo de mobil mais a
	 *         descrição do tipo de destinação.
	 */
	public String getDescricaoCompleta() {
		if (isGeral())
			return getExTipoMobil().getDescTipoMobil();

		String s = getNumSequencia() + (isVia() ? "&ordf; " : "&ordm; ")
				+ getExTipoMobil().getDescTipoMobil();

		if (isVia()) {
			ExVia via = getDoc().via(getNumSequencia().shortValue());
			if (via != null && via.getExTipoDestinacao() != null) {
				s += " (" + via.getExTipoDestinacao().getDescrTipoDestinacao()
						+ ")";
			}
		}

		return s;
	}

	/**
	 * Informa a sigla de um mobil.
	 * 
	 * @param sigla
	 */
	public void setSigla(String sigla) {
		sigla = sigla.trim().toUpperCase();

		Map<String, CpOrgaoUsuario> mapAcronimo = new TreeMap<String, CpOrgaoUsuario>();
		for (CpOrgaoUsuario ou : ExDao.getInstance().listarTodos(
				CpOrgaoUsuario.class)) {
			mapAcronimo.put(ou.getAcronimoOrgaoUsu(), ou);
		}
		String acronimos = "";
		for (String s : mapAcronimo.keySet()) {
			acronimos += "|" + s;
		}

		final Pattern p2 = Pattern.compile("^TMP-?([0-9]{1,7})");
		final Pattern p1 = Pattern
				.compile("^([A-Za-z0-9]{2}"
						+ acronimos
						+ ")?-?([A-Za-z]{3})?-?(?:([0-9]{4})/?)??([0-9]{1,5})(\\.?[0-9]{1,3})?(?:((?:-?[a-zA-Z]{1})|(?:-[0-9]{1,2}))|((?:-?V[0-9]{1,2})))?$");
		final Matcher m2 = p2.matcher(sigla);
		final Matcher m1 = p1.matcher(sigla);

		if (getExDocumento() == null) {
			final ExDocumento doc = new ExDocumento();
			setExDocumento(doc);
		}

		if (m2.find()) {
			if (m2.group(1) != null)
				getExDocumento().setIdDoc(new Long(m2.group(1)));
			return;
		}

		if (m1.find()) {

			if (m1.group(1) != null) {
				try {
					if (mapAcronimo.containsKey(m1.group(1))) {
						getExDocumento().setOrgaoUsuario(
								mapAcronimo.get(m1.group(1)));
					} else {
						CpOrgaoUsuario orgaoUsuario = new CpOrgaoUsuario();
						orgaoUsuario.setSiglaOrgaoUsu(m1.group(1));

						orgaoUsuario = ExDao.getInstance().consultarPorSigla(
								orgaoUsuario);

						getExDocumento().setOrgaoUsuario(orgaoUsuario);
					}
				} catch (final Exception ce) {

				}
			}

			if (m1.group(2) != null) {
				try {
					ExFormaDocumento formaDoc = new ExFormaDocumento();
					formaDoc.setSiglaFormaDoc(m1.group(2));
					formaDoc = ExDao.getInstance().consultarPorSigla(formaDoc);
					if (formaDoc != null)
						getExDocumento().setExFormaDocumento(formaDoc);
				} catch (final Exception ce) {
					throw new Error(ce);
				}
			}

			if (m1.group(3) != null)
				getExDocumento().setAnoEmissao(Long.parseLong(m1.group(3)));
			// else {
			// Date dt = new Date();
			// getExDocumento().setAnoEmissao((long) dt.getYear());
			// }
			if (m1.group(4) != null)
				getExDocumento().setNumExpediente(Long.parseLong(m1.group(4)));

			// Numero de sequencia do documento filho
			//
			if (m1.group(5) != null) {
				String vsNumSubdocumento = m1.group(5).toUpperCase();
				if (vsNumSubdocumento.contains("."))
					vsNumSubdocumento = vsNumSubdocumento
							.substring(vsNumSubdocumento.indexOf(".") + 1);
				Integer vshNumSubdocumento = new Integer(vsNumSubdocumento);
				if (vshNumSubdocumento != 0) {
					String siglaPai = (m1.group(1) == null ? (getExDocumento()
							.getOrgaoUsuario() != null ? getExDocumento()
							.getOrgaoUsuario().getAcronimoOrgaoUsu() : "") : m1
							.group(1))
							+ (m1.group(2) == null ? "" : m1.group(2))
							+ (m1.group(3) == null ? "" : m1.group(3))
							+ ((m1.group(3) != null && m1.group(4) != null) ? "/"
									: "")
							+ (m1.group(4) == null ? "" : m1.group(4));
					ExMobilDaoFiltro flt = new ExMobilDaoFiltro();
					flt.setSigla(siglaPai);
					ExMobil mobPai = null;
					try {
						mobPai = ExDao.getInstance().consultarPorSigla(flt);
					} catch (Exception e) {
						e.printStackTrace();
					}
					ExDocumento docFilho = mobPai.doc().getMobilGeral()
							.getSubdocumento(vshNumSubdocumento);
					setExDocumento(docFilho);
				}
			}

			// Numero da via
			//
			if (m1.group(6) != null) {
				String vsNumVia = m1.group(6).toUpperCase();
				if (vsNumVia.contains("-"))
					vsNumVia = vsNumVia.substring(vsNumVia.indexOf("-") + 1);
				Integer vshNumVia;
				final String alfabeto = "ABCDEFGHIJLMNOPQRSTUZ";
				final int vi = (alfabeto.indexOf(vsNumVia)) + 1;
				if (vi <= 0)
					vshNumVia = new Integer(vsNumVia);
				else {
					vshNumVia = (new Integer(vi).intValue());
					setExTipoMobil(ExDao.getInstance().consultar(
							ExTipoMobil.TIPO_MOBIL_VIA, ExTipoMobil.class,
							false));
				}
				setNumSequencia(vshNumVia);
			} else if (m1.group(7) != null) {
				String vsNumVolume = m1.group(7).toUpperCase();
				if (vsNumVolume.contains("-"))
					vsNumVolume = vsNumVolume.substring(vsNumVolume
							.indexOf("-") + 1);
				if (vsNumVolume.contains("V"))
					vsNumVolume = vsNumVolume.substring(vsNumVolume
							.indexOf("V") + 1);
				Integer vshNumVolume = new Integer(vsNumVolume);
				setExTipoMobil(ExDao.getInstance()
						.consultar(ExTipoMobil.TIPO_MOBIL_VOLUME,
								ExTipoMobil.class, false));
				setNumSequencia(vshNumVolume);
			} else {
				setExTipoMobil(ExDao.getInstance().consultar(
						ExTipoMobil.TIPO_MOBIL_GERAL, ExTipoMobil.class, false));
			}
		}
	}

	/**
	 * Retorna o código do documento mais o número da via ou do volume.
	 * 
	 * @return O código do documento mais o número da via ou do volume.
	 */
	public String getSigla() {
		if (getExDocumento() == null)
			return null;
		if (getExTipoMobil() == null)
			return null;
		if ((isVia() || isVolume())
				&& (getNumSequencia() == null || getNumSequencia() == 0))
			throw new Error(
					"Via e Volume devem possuir número válido de sequencia.");
		if (isVia()) {
			final String alfabeto = "ABCDEFGHIJLMNOPQRSTUZ";

			// as vias vão até a letra 'U', se passar disso, assume letra 'Z'
			if (getNumSequencia() <= 20) {
				final String vsNumVia = alfabeto.substring(
						getNumSequencia() - 1, getNumSequencia());
				return getExDocumento().getCodigo() + "-" + vsNumVia;
			} else {
				final String vsNumVia = "Z";
				return getExDocumento().getCodigo() + "-" + vsNumVia;
			}
		} else if (isVolume()) {
			final String vsNumVolume = "V"
					+ (getNumSequencia() < 10 ? "0" + getNumSequencia()
							: getNumSequencia());
			return getExDocumento().getSigla() + "-" + vsNumVolume;
		} else {
			return getExDocumento().getSigla();
		}
	}

	/**
	 * Retorna o código do documento mais o número da via ou do volume.
	 * 
	 * @return O código do documento mais o número da via ou do volume.
	 */
	public static String getSigla(String codigoDocumento, Integer numSequencia,
			Long idTipoMobil) {
		if (codigoDocumento == null)
			return null;
		if (idTipoMobil == null)
			return null;

		if ((idTipoMobil == ExTipoMobil.TIPO_MOBIL_VIA || idTipoMobil == ExTipoMobil.TIPO_MOBIL_VOLUME)
				&& (numSequencia == null || numSequencia == 0))
			throw new Error(
					"Via e Volume devem possuir número válido de sequencia.");
		if (idTipoMobil == ExTipoMobil.TIPO_MOBIL_VIA) {
			final String alfabeto = "ABCDEFGHIJLMNOPQRSTUZ";

			// as vias vão até a letra 'U', se passar disso, assume letra 'Z'
			if (numSequencia <= 20) {
				final String vsNumVia = alfabeto.substring(numSequencia - 1,
						numSequencia);
				return codigoDocumento + "-" + vsNumVia;
			} else {
				final String vsNumVia = "Z";
				return codigoDocumento + "-" + vsNumVia;
			}
		} else if (idTipoMobil == ExTipoMobil.TIPO_MOBIL_VOLUME) {
			final String vsNumVolume = "V"
					+ (numSequencia < 10 ? "0" + numSequencia : numSequencia);
			return codigoDocumento + "-" + vsNumVolume;
		} else {
			return codigoDocumento;
		}
	}

	/*
	 * public Long getId() { if (getExDocumento() == null) return null;
	 * ExMovimentacao mov = getExDocumento()
	 * .getUltimaMovimentacaoNaoCancelada(getNumVia()); if (mov == null) return
	 * null; return mov.getIdMov(); }
	 */

	/**
	 * Se esse documento estiver juntado, retorna o pai Senão, retorna null
	 * 
	 */
	public ExMobil getMobilPrincipal() {

		if (getExMobilPai() == null)
			return this;

		return getExMobilPai().getMobilPrincipal();
	}

	/**
	 * Retorna a sigla sem os sinais "-" e "/".
	 * 
	 * @return A sigla sem os sinais "-" e "/".
	 * 
	 */
	public String getCodigoCompacto() {
		String s = getSigla();
		if (s == null)
			return null;
		return s.replace("-", "").replace("/", "");
	}

	/**
	 * Retorna a sigla.
	 * 
	 * @return A sigla.
	 * 
	 */
	public Object getCodigo() {
		return getSigla();
	}

	/**
	 * Retorna o documento relacionado ao Mobil.
	 * 
	 * @return O documento relacionado ao Mobil.
	 * 
	 */
	public ExDocumento doc() {
		return getExDocumento();
	}

	/**
	 * Uma via está arquivada se: -existir uma movimentação não cancelada e o
	 * estado desta movimentação for "Arquivado Corrente", "Arquivado
	 * Intermediário" ou "Arquivado Permanente"
	 */
	public boolean isArquivado() {
		boolean b = false;
		for (ExMovimentacao mov : getExMovimentacaoSet()) {
			if (mov.isCancelada())
				continue;
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_CORRENTE
					|| mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_INTERMEDIARIO
					|| mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_PERMANENTE)
				b = true;
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESARQUIVAMENTO)
				b = false;
		}
		return b;
	}

	/**
	 * Verifica se uma mobil está sobrestado
	 */
	public boolean isSobrestado() {
		boolean b = false;
		for (ExMovimentacao mov : getExMovimentacaoSet()) {
			if (mov.isCancelada())
				continue;
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_SOBRESTAR)
				b = true;
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESOBRESTAR)
				b = false;
		}
		return b;
	}

	/**
	 * Verifica se um Mobil está em trânsito. Um Mobil está em trânsito quando
	 * ele possui movimentações não canceladas dos tipos: TRANSFERENCIA,
	 * DESPACHO_TRANSFERENCIA, DESPACHO_TRANSFERENCIA_EXTERNA ou
	 * TRANSFERENCIA_EXTERNA e não possuem movimentação de recebimento.
	 * 
	 * @return Verdadeiro se o Mobil está em trânsito e Falso caso contrário.
	 * 
	 */
	public boolean isEmTransito() {
		boolean b = false;
		for (ExMovimentacao mov : getExMovimentacaoSet()) {
			if (mov.isCancelada())
				continue;
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA
					|| mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA
					|| mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA
					|| mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA_EXTERNA)
				b = true;
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO)
				b = false;
		}
		return b;
	}

	/**
	 * Verifica se um Mobil está em trânsito interno. Um Mobil está em trânsito
	 * interno quando ele possui movimentações não canceladas dos tipos:
	 * TRANSFERENCIA ou DESPACHO_TRANSFERENCIA e não possuem movimentação de
	 * recebimento.
	 * 
	 * @return Verdadeiro se o Mobil está em trânsito interno e Falso caso
	 *         contrário.
	 * 
	 */
	public boolean isEmTransitoInterno() {
		boolean b = false;
		for (ExMovimentacao mov : getExMovimentacaoSet()) {
			if (mov.isCancelada())
				continue;
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA
					|| mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA)
				b = true;
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO)
				b = false;
		}
		return b;
	}

	/**
	 * Verifica se um Mobil está em trânsito externo. Um Mobil está em trânsito
	 * externo quando ele possui movimentações não canceladas dos tipos:
	 * DESPACHO_TRANSFERENCIA_EXTERNA ou TRANSFERENCIA_EXTERNA e não possuem
	 * movimentação de recebimento.
	 * 
	 * @return Verdadeiro se o Mobil está em trânsito externo e Falso caso
	 *         contrário.
	 * 
	 */
	public boolean isEmTransitoExterno() {
		boolean b = false;
		for (ExMovimentacao mov : getExMovimentacaoSet()) {
			if (mov.isCancelada())
				continue;
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA_EXTERNA)
				b = true;
			// Orlando: O IF abaixo foi incluído para não permitir que o
			// documento seja recebido após ter sido transferido para um órgão
			// externo,
			// inclusive no caso de despacho com transferência externa.
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA)
				b = true;
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO)
				b = false;

		}

		return b;
	}

	/**
	 * Verifica se um Mobil está juntado a outro. Um Mobil está em juntado a
	 * outro quando ele possui movimentações não canceladas dos tipos: JUNTADA
	 * ou JUNTADA_EXTERNO e não possuem movimentação de cancelamento de juntada.
	 * 
	 * @return Verdadeiro se o Mobil está juntado a outro e Falso caso
	 *         contrário.
	 * 
	 */
	public boolean isJuntado() {
		boolean b = false;
		for (ExMovimentacao mov : getExMovimentacaoSet()) {
			if (mov.isCancelada())
				continue;
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA
					|| mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA_EXTERNO)
				b = true;
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA)
				b = false;
		}
		return b;
	}

	/**
	 * Verifica se um Mobil está juntado a outro mobil do tipo interno. Um Mobil
	 * está em juntado a outro mobil do tipo interno quando ele possui
	 * movimentaçao não cancelada do tipo: JUNTADA e não possue movimentação de
	 * cancelamento de juntada.
	 * 
	 * @return Verdadeiro se o Mobil está juntado a outro mobil do tipo interno
	 *         e Falso caso contrário.
	 * 
	 */
	public boolean isJuntadoInterno() {
		boolean b = false;
		for (ExMovimentacao mov : getExMovimentacaoSet()) {
			if (mov.isCancelada())
				continue;
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA)
				b = true;
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA)
				b = false;
		}
		return b;
	}

	/**
	 * Verifica se um Mobil está juntado a outro mobil do tipo externo. Um Mobil
	 * está em juntado a outro mobil do tipo externo quando ele possui
	 * movimentaçao não cancelada do tipo: JUNTADA_EXTERNO e não possue
	 * movimentação de cancelamento de juntada.
	 * 
	 * @return Verdadeiro se o Mobil está juntado a outro mobil do tipo externo
	 *         e Falso caso contrário.
	 * 
	 */
	public boolean isJuntadoExterno() {
		boolean b = false;
		for (ExMovimentacao mov : getExMovimentacaoSet()) {
			if (mov.isCancelada())
				continue;
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA_EXTERNO)
				b = true;
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA)
				b = false;
		}
		return b;
	}

	/**
	 * Verifica se um Mobil está apensado a outro mobil. Um Mobil está apensado
	 * a outro mobil quando ele possui movimentaçao do tipo: APENSACAO e não
	 * possue movimentação de desapensação
	 * 
	 * @return Verdadeiro se o Mobil está apensado a outro mobil e Falso caso
	 *         contrário.
	 * 
	 */
	public boolean isApensado() {
		boolean b = false;
		for (ExMovimentacao mov : getExMovimentacaoSet()) {
			if (mov.isCancelada())
				continue;
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_APENSACAO)
				b = true;
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESAPENSACAO)
				b = false;
		}
		return b;
	}

	/**
	 * Retorna o Mobil pai do Mobil atual.
	 * 
	 * @return Mobil pai do Mobil atual.
	 * 
	 */
	public ExMobil getExMobilPai() {
		ExMobil m = null;
		for (ExMovimentacao mov : getExMovimentacaoSet()) {
			if (mov.isCancelada())
				continue;
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA)
				m = mov.getExMobilRef();
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA)
				m = null;
		}
		return m;
	}

	public Long getId() {
		return getIdMobil();
	}

	/**
	 * Retorna a descrição da última movimentação não cancelada.
	 * 
	 * @return Descrição da última movimentação não cancelada.
	 * 
	 */
	public String getDescricaoUltimaMovimentacaoNaoCancelada() {
		ExMovimentacao mov = getUltimaMovimentacaoNaoCancelada();
		if (mov == null)
			return null;
		return mov.getExTipoMovimentacao().getDescricao();
	}

	/**
	 * Retorna o documento relacionado ao Mobil atual.
	 * 
	 * @return Documento relacionado ao Mobil atual.
	 * 
	 */
	public ExDocumento getDoc() {
		return doc();
	}

	public java.util.Set<ExMovimentacao> getCronologiaSet() {
		final TreeSet<ExMovimentacao> set = new TreeSet<ExMovimentacao>(
				new CronologiaComparator());
		set.addAll(getExMovimentacaoSet());
		set.addAll(getExMovimentacaoReferenciaSet());
		return set;
	}

	public int compareTo(Object o) {
		ExMobil other = (ExMobil) o;
		int i;
		i = other.getExDocumento().getIdDoc()
				.compareTo(getExDocumento().getIdDoc());
		if (i != 0)
			return -i;
		i = other.getExTipoMobil().getIdTipoMobil()
				.compareTo(getExTipoMobil().getIdTipoMobil());
		if (i != 0)
			return i;
		return getNumSequencia().compareTo(other.getNumSequencia());
	}

	public List<ExArquivoNumerado> getArquivosNumerados() {
		return getExDocumento().getArquivosNumerados(this);
	}

	/**
	 * Retorna a descrição dos marcadores relacionado ao Mobil atual.
	 * 
	 * @return Descrição dos marcadores relacionado ao Mobil atual.
	 * 
	 */
	public String getMarcadores() {
		StringBuilder sb = new StringBuilder();
		for (ExMarca mar : getExMarcaSet()) {
			if (sb.length() > 0)
				sb.append(", ");
			sb.append(mar.getCpMarcador().getDescrMarcador());
		}
		if (sb.length() == 0)
			return null;
		return sb.toString();
	}

	// public String getMarcadoresEmHtml(DpPessoa pess, DpLotacao lota) {
	// StringBuilder sb = new StringBuilder();
	//
	// // Marcacoes para a propria lotacao e para a propria pessoa ou sem
	// // informacao de pessoa
	// //
	// for (ExMarca mar : getExMarcaSet()) {
	// if (sb.length() > 0)
	// sb.append(", ");
	// if ((mar.getDpLotacaoIni() != null && lota.getIdInicial().equals(
	// mar.getDpLotacaoIni().getIdInicial()))
	// && (mar.getDpPessoaIni() == null || pess.getIdInicial()
	// .equals(mar.getDpPessoaIni().getIdInicial())))
	// sb.append(mar.getCpMarcador().getDescrMarcador());
	// }
	//
	// // Marcacoes para a propria lotacao e para outra pessoa
	// //
	// if (sb.length() == 0) {
	// for (ExMarca mar : getExMarcaSet()) {
	// if (sb.length() > 0)
	// sb.append(", ");
	// if ((mar.getDpLotacaoIni() != null && lota.getIdInicial()
	// .equals(mar.getDpLotacaoIni().getIdInicial()))
	// && (mar.getDpPessoaIni() != null && !pess
	// .getIdInicial().equals(
	// mar.getDpPessoaIni().getIdInicial()))) {
	// sb.append(mar.getCpMarcador().getDescrMarcador());
	// sb.append(" [");
	// sb.append(mar.getDpPessoaIni().getSigla());
	// sb.append("]");
	// }
	// }
	// }
	//
	// // Marcacoes para qualquer outra pessoa ou lotacao
	// //
	// if (sb.length() == 0) {
	// for (ExMarca mar : getExMarcaSet()) {
	// if (sb.length() > 0)
	// sb.append(", ");
	// sb.append(mar.getCpMarcador().getDescrMarcador());
	// if (mar.getDpLotacaoIni() != null
	// || mar.getDpPessoaIni() != null) {
	// sb.append(" [");
	// if (mar.getDpLotacaoIni() != null) {
	// sb.append(mar.getDpLotacaoIni().getSigla());
	// }
	// if (mar.getDpPessoaIni() != null) {
	// if (mar.getDpLotacaoIni() != null) {
	// sb.append(", ");
	// }
	// sb.append(mar.getDpPessoaIni().getSigla());
	// }
	// sb.append("]");
	// }
	// }
	// }
	// if (sb.length() == 0)
	// return null;
	// return sb.toString();
	// }

	/**
	 * Retorna o Mobil Mestre relacionado ao Mobil atual.
	 * 
	 * @return Mobil Mestre relacionado ao Mobil atual.
	 * 
	 */
	public ExMobil getMestre() {
		ExMobil m = null;
		for (ExMovimentacao mov : getExMovimentacaoSet()) {
			if (mov.isCancelada())
				continue;
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_APENSACAO)
				m = mov.getExMobilRef();
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESAPENSACAO)
				m = null;
		}
		return m;
	}

	/**
	 * Se o mobil em questão for o grande mestre de uma cadeia de apensação, ele
	 * deve retornar "this".
	 * 
	 * @return Retorna o mestre de último nivel, ou seja, o mestre de todos os
	 *         mestres dessa apensacao.
	 */
	public ExMobil getGrandeMestre() {
		ExMobil m = getMestre();
		if (m == null) {
			if (getApensos().size() != 0)
				return this;
		}
		while (m != null) {
			ExMobil m2 = m.getMestre();
			if (m2 == null)
				return m;
			else
				m = m2;
		}
		return null;
	}

	/**
	 * 
	 * @return Retorna todos os apensos desse mobil para baixo. Não inclui o
	 *         próprio mobil que está sendo chamado.
	 */
	public SortedSet<ExMobil> getApensos() {
		return getApensos(false, false);
	}

	public SortedSet<ExMobil> getApensosDiretosExcetoVolumeApensadoAoProximo() {
		return getApensos(true, true);
	}

	public SortedSet<ExMobil> getApensos(boolean omitirApensosIndiretos,
			boolean omitirVolumesApensadosAosProximos) {
		SortedSet<ExMobil> set = new TreeSet<ExMobil>();
		return getApensos(set, omitirApensosIndiretos,
				omitirVolumesApensadosAosProximos);
	}

	public SortedSet<ExMobil> getApensos(SortedSet<ExMobil> set,
			boolean omitirApensosIndiretos,
			boolean omitirVolumesApensadosAosProximos) {
		for (ExMovimentacao mov : getExMovimentacaoReferenciaSet()) {
			if (!ExTipoMovimentacao.hasApensacao(mov.getIdTpMov()))
				continue;
			ExMobil mobMestre = mov.getExMobil().getMestre();
			if (this.equals(mobMestre)) {
				if (!set.contains(mov.getExMobil())) {
					if (!omitirVolumesApensadosAosProximos
							|| !mov.getExMobil().isVolumeApensadoAoProximo())
						set.add(mov.getExMobil());
					if (!omitirApensosIndiretos)
						mov.getExMobil().getApensos(set,
								omitirApensosIndiretos,
								omitirVolumesApensadosAosProximos);
				}
			}
		}
		return set;
	}

	@Override
	public String toString() {
		return getSigla();
	}

	@Override
	public boolean equals(Object obj) {
		if (this.getIdMobil() == null)
			return super.equals(obj);
		ExMobil other = (ExMobil) obj;
		if (other == null || other.getIdMobil() == null)
			return false;
		return this.getIdMobil().equals(other.getIdMobil());
	}

	public SortedSet<ExMobil> getMobilETodosOsApensos() {
		return getMobilETodosOsApensos(false);
	}

	public SortedSet<ExMobil> getMobilETodosOsApensos(
			boolean fOmitirVolumesApensadosAosProximos) {
		SortedSet<ExMobil> set = new TreeSet<ExMobil>();
		ExMobil mobGrandeMestre = getGrandeMestre();
		if (mobGrandeMestre != null) {
			set.add(mobGrandeMestre);
			mobGrandeMestre.getApensos(set, false,
					fOmitirVolumesApensadosAosProximos);
		} else {
			set.add(this);
		}
		return set;
	}

	/**
	 * Verifica se um Mobil está encerrado. Um mobil está encerrado se ele
	 * possui movimentação não cancelada do tipo ENCERRAMENTO.
	 * 
	 * @return Verdadeiro se o Mobil está encerrado e Falso caso contrário.
	 * 
	 */
	public boolean isEncerrado() {
		boolean b = false;
		for (ExMovimentacao mov : getExMovimentacaoSet()) {
			if (mov.isCancelada())
				continue;
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ENCERRAMENTO)
				b = true;
		}
		return b;
	}

	/**
	 * Verifica se um Mobil do tipo Volume está Apensado ao próximo Mobil. Para
	 * saber o próximo Mobil é utilizado o número de sequência do Mobil.
	 * 
	 * @return Verdadeiro se o Mobil estiver apensado ao próximo volume e Falso
	 *         caso contrário.
	 * 
	 */
	public boolean isVolumeApensadoAoProximo() {
		if (getMestre() == null)
			return false;
		if (!isVolume())
			return false;
		if (!getMestre().isVolume())
			return false;
		if (!getNumSequencia().equals(getMestre().getNumSequencia() - 1))
			return false;
		if (!getMestre().doc().getIdDoc().equals(doc().getIdDoc()))
			return false;
		return true;
	}

	/**
	 * Verifica se um Mobil possui Anexos Pendentes de Assinatura
	 * 
	 * @return Verdadeiro se o Mobil possui anexos não assinados e False caso
	 *         contrário.
	 * 
	 */
	public boolean temAnexosNaoAssinados() {
		boolean b = false;
		for (ExMovimentacao movAss : this.getExMovimentacaoSet()) {
			if (movAss.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO)
				if (movAss.isAssinada())
					continue;
				else {
					b = true;
					break;
				}
		}
		return b;

	}

	/**
	 * Verifica se um Mobil possui arquivos anexados
	 * 
	 * @return Verdadeiro se o Mobil possui arquivos anexados e False caso
	 *         contrário.
	 * 
	 */
	public boolean temAnexos() {
		boolean b = false;
		for (ExMovimentacao movAss : this.getExMovimentacaoSet()) {
			if (movAss.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO)
				b = true;
			break;
		}
		return b;

	}

	/**
	 * Verifica se um Mobil do tipo Volume está Apensado a outro Mobil do mesmo
	 * Processo.
	 * 
	 * @return Verdadeiro se o Mobil estiver apensado a outro volume do mesmo
	 *         processo e Falso caso contrário.
	 * 
	 */
	public boolean isApensadoAVolumeDoMesmoProcesso() {
		if (getMestre() == null)
			return false;
		if (!isVolume())
			return false;
		if (!getMestre().isVolume())
			return false;
		if (!getMestre().doc().getIdDoc().equals(doc().getIdDoc()))
			return false;
		return true;
	}

	/**
	 * Retorna o último documento que é filho do Mobil atual.
	 * 
	 * @return Último documento que é filho do Mobil atual.
	 * 
	 */
	public int getNumUltimoSubdocumento() {
		int maxNumSubdocumento = 0;
		for (ExDocumento d : getExDocumentoFilhoSet()) {
			if (d.getNumSequencia() == null)
				continue;
			if (d.getNumSequencia() > maxNumSubdocumento) {
				maxNumSubdocumento = d.getNumSequencia();
			}
		}
		return maxNumSubdocumento;
	}

	/**
	 * Retorna um documento filho do Mobil atual de acordo com o número de
	 * sequência informado.
	 * 
	 * @param numSubdocumento
	 * 
	 * @return Documento filho do Mobil atual de acordo com o número de
	 *         sequência informado.
	 * 
	 */
	public ExDocumento getSubdocumento(int numSubdocumento) {
		for (final ExDocumento d : getExDocumentoFilhoSet()) {
			if (d.getNumSequencia() == null)
				continue;
			if (d.getNumSequencia() == numSubdocumento) {
				return d;
			}
		}
		return null;
	}

	public boolean isNumeracaoUnicaAutomatica() {
		return doc().isNumeracaoUnicaAutomatica();
	}

	public List<ExArquivoNumerado> filtrarArquivosNumerados(ExMovimentacao mov,
			boolean bCompleto) {
		ExMobil mobPrincipal = getMobilPrincipal();

		List<ExArquivoNumerado> arquivosNumerados = null;

		arquivosNumerados = mobPrincipal.getExDocumento().getArquivosNumerados(
				mobPrincipal);

		List<ExArquivoNumerado> ans = new ArrayList<ExArquivoNumerado>();
		int i = 0;
		if (mov != null) {
			// Se for uma movimentacao, remover todos os arquivos alem da
			// movimentacao
			for (ExArquivoNumerado an : arquivosNumerados) {
				if (an.getArquivo() instanceof ExMovimentacao) {
					if (((ExMovimentacao) an.getArquivo()).getIdMov().equals(
							mov.getIdMov())) {
						ans.add(an);
						break;
					}
				}
			}
		} else if (mobPrincipal != this) {
			// Se for um documento juntado, remover todos os documentos que alem
			// dele e de seus anexos
			ExArquivoNumerado an;
			for (i = 0; i < arquivosNumerados.size(); i++) {
				an = arquivosNumerados.get(i);
				if (an.getArquivo() instanceof ExDocumento) {
					if (((ExDocumento) an.getArquivo()).getIdDoc().equals(
							getExDocumento().getIdDoc())
							&& an.getMobil().equals(this)) {
						ans.add(an);
						break;
					}
				}
			}
		} else {
			ans.add(arquivosNumerados.get(0));
		}
		if (bCompleto && i != -1) {
			for (int j = i + 1; j < arquivosNumerados.size(); j++) {
				ExArquivoNumerado anSub = arquivosNumerados.get(j);
				if (anSub.getNivel() <= arquivosNumerados.get(i).getNivel())
					break;
				ans.add(anSub);
			}
		}
		return ans;
	}

	public Long getByteCount() {
		if (getExMobilPai() != null)
			return null;
		long l = 0;

		List<ExArquivoNumerado> arquivosNumerados = getExDocumento()
				.getArquivosNumerados(this);

		for (int i = 0; i < arquivosNumerados.size(); i++)
			l += arquivosNumerados.get(i).getArquivo().getByteCount();

		return l;
	}

	/**
	 * Retorna as movimentações de um Mobil que estão canceladas.
	 * 
	 * @return Lista de movimentações de um Mobil que estão canceladas.
	 * 
	 */
	public List<ExMovimentacao> getMovimentacoesCanceladas() {

		final Set<ExMovimentacao> movs = getExMovimentacaoSet();
		List<ExMovimentacao> movsCanceladas = new ArrayList<ExMovimentacao>();

		if (movs != null)
			for (final ExMovimentacao mov : movs) {
				if (mov.isCancelada())
					movsCanceladas.add(mov);
			}
		return movsCanceladas;
	}

}
