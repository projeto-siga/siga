package br.gov.jfrj.siga.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;

import models.SrConfiguracao;
import models.SrItemConfiguracao;
import models.SrServico;
import models.SrSubTipoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import controllers.SrConfiguracaoBL;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;

/*
 * Edson: Não é ideal que esta classe (tal como ocorre com qualquer 
 * superclasse) use instanceof ou catch(ClassCastException) em si mesma, 
 * ou seja, que diferencie subclasses que implementem Historico das 
 * que não implementem. Isso foi necessário porque o ObjetoBase do 
 * projeto siga-base serve de superclasse para todas as classes do 
 * Corporativo, sejam elas Historico ou não. Para adicionar funcionalidades 
 * Play a essas classes, da qual algumas do sigasr estendem (SrConfiguracao, 
 * por exemplo), e tb manter um local único para manipulação de Histórico no 
 * Play tanto para classes como a SrConfiguracao quanto para as só do Play 
 * (como SrSolicitacao), foi usado esse esquema de substituir a ObjetoBase 
 * no projeto Play e deixá-la como mãe de tudo, ficando aquelas funcionalidades 
 * concentradas aqui.  
 */
public class ObjetoBase extends GenericModel {

	public void salvar() throws Exception {
		try {
			Historico thisHistorico = (Historico) this;
			thisHistorico.setHisDtIni(new Date());
			thisHistorico.setHisDtFim(null);
			if (thisHistorico.getId() == null) {
				super.save();
				thisHistorico.setHisIdIni(thisHistorico.getId());
			} else {
				JPA.em().detach(this);
				// Edson: Abaixo, não funcionou findById, por algum motivo relacionado
				// a esse esquema de
				// sobrescrever o ObjetoBase dentro da aplicação Play
				ObjetoBase oAntigo = (ObjetoBase) JPA.em().find(
						this.getClass(), thisHistorico.getId());
				Historico oAntigoHistorico = (Historico) oAntigo;
				oAntigo.finalizar();
				thisHistorico.setHisIdIni(oAntigoHistorico.getHisIdIni());
				thisHistorico.setId(null);
			}

			super.save();

		} catch (IllegalAccessException iae) {
			// Erro no copyPoperties ou newInstance
			int a = 0;
		} catch (InstantiationException ie) {
			// Erro no newInstance
			int a = 0;
		} catch (ClassCastException cce) {
			// Erro ao converter pra Historico
			super.save();
		}

		//return this;

	}

	public void finalizar() throws Exception {
		try {
			Historico thisHistorico = (Historico) this;
			thisHistorico.setHisDtFim(new Date());
			super.save();
		} catch (ClassCastException cce) {
			super.delete();
		}
	}

	@Override
	public final ObjetoBase save() {
		try {
			salvar();
		} catch (Exception e) {
			//
		}
		return this;
	}
}
