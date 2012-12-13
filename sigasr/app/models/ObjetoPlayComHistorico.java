package models;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import controllers.SrConfiguracaoBL;

import play.db.jpa.JPA;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Historico;
import br.gov.jfrj.siga.model.Objeto;

@MappedSuperclass
public abstract class ObjetoPlayComHistorico extends Objeto implements
		Historico {

	@Column(name = "HIS_ID_INI")
	private Long hisIdIni;

	@Column(name = "HIS_DT_INI")
	private Date hisDtIni;

	@Column(name = "HIS_DT_FIM")
	private Date hisDtFim;

	@Column(name = "HIS_ATIVO")
	private int hisAtivo;

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Long getIdInicial() {
		return hisIdIni;
	}

	@Override
	public boolean equivale(Object other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Long getHisIdIni() {
		return hisIdIni;
	}

	@Override
	public void setHisIdIni(Long hisIdIni) {
		this.hisIdIni = hisIdIni;
	}

	@Override
	public Date getHisDtIni() {
		return hisDtIni;
	}

	@Override
	public void setHisDtIni(Date hisDtIni) {
		this.hisDtIni = hisDtIni;
	}

	@Override
	public Date getHisDtFim() {
		return hisDtFim;
	}

	@Override
	public void setHisDtFim(Date hisDtFim) {
		this.hisDtFim = hisDtFim;
	}

	@Override
	public int getHisAtivo() {
		return hisAtivo;
	}

	@Override
	public void setHisAtivo(int hisAtivo) {
		this.hisAtivo = hisAtivo;
	}

	public abstract Long getId();

	public abstract void setId(Long id);

	// Edson: Assim que for resolvido o problema no ObjetoBase, colocar esse
	// getConfiguracao() lá.
	public SrConfiguracao getConfiguracao(SrItemConfiguracao item,
			SrServico servico, long idTipo, SrSubTipoConfiguracao subTipo)
			throws Exception {

		SrConfiguracao conf = new SrConfiguracao(item, servico, JPA.em().find(
				CpTipoConfiguracao.class, idTipo), subTipo);

		return SrConfiguracaoBL.get().buscarConfiguracao(conf);
	}

	public List<SrConfiguracao> getConfiguracoes(SrItemConfiguracao item,
			SrServico servico, long idTipo, SrSubTipoConfiguracao subTipo) throws Exception{
		SrConfiguracao conf = new SrConfiguracao(item, servico, JPA.em().find(
				CpTipoConfiguracao.class, idTipo), subTipo);
		return SrConfiguracaoBL.get().listarConfiguracoesAtivasPorFiltro(conf);
	}

	public void copiarPara(Object o) {
		for (Method getter : this.getClass().getDeclaredMethods()) {
			try {
				String getterName = getter.getName();
				if (!getterName.startsWith("get"))
					continue;
				if (Collection.class.isAssignableFrom(getter.getReturnType()))
					continue;
				String setterName = getterName.replace("get", "set");
				Object origValue = getter.invoke(this);
				o.getClass().getMethod(setterName, getter.getReturnType())
						.invoke(o, origValue);
			} catch (NoSuchMethodException nSME) {
				int a = 0;
			} catch (IllegalAccessException iae) {
				int a = 0;
			} catch (IllegalArgumentException iae) {
				int a = 0;
			} catch (InvocationTargetException nfe) {
				int a = 0;
			}

		}
	}

}
