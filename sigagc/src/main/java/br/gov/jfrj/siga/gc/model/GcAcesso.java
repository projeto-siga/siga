package br.gov.jfrj.siga.gc.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpGrupo;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.grupo.ConfiguracaoGrupo;
import br.gov.jfrj.siga.cp.grupo.TipoConfiguracaoGrupoEnum;
import br.gov.jfrj.siga.cp.util.XjusUtils;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.Objeto;

@Entity
@Table(name = "sigagc.gc_acesso")
public class GcAcesso extends Objeto {
	private static final long serialVersionUID = -6824464659652929435L;
	public static final long ACESSO_EXTERNO_PUBLICO = 0;
	public static final long ACESSO_PUBLICO = 1;
	public static final long ACESSO_ORGAO_USU = 2;
	public static final long ACESSO_LOTACAO_E_SUPERIORES = 3;
	public static final long ACESSO_LOTACAO_E_INFERIORES = 4;
	public static final long ACESSO_LOTACAO = 5;
	public static final long ACESSO_PESSOAL = 6;
	public static final long ACESSO_LOTACAO_E_GRUPO = 7;

	public static ActiveRecord<GcAcesso> AR = new ActiveRecord<>(GcAcesso.class);

	@Id
	@GeneratedValue
	@Column(name = "ID_ACESSO", unique = true, nullable = false)
	private Long id;

	@Column(name = "NOME_ACESSO", nullable = false)
	private String nome;

	public void setId(Long id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public GcAcesso() {
		super();
	}

	public GcAcesso(long id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public Set<Object> calcularAcessos(GcInformacao inf) {
		Set<Object> acessos = new HashSet<Object>();
		switch ((int) (long) inf.getVisualizacao().getId()) {
		case (int) GcAcesso.ACESSO_EXTERNO_PUBLICO:
		case (int) GcAcesso.ACESSO_PUBLICO:
			acessos.add(XjusUtils.ACESSO_PUBLICO);
			break;
		case (int) GcAcesso.ACESSO_ORGAO_USU:
			acessos.add(inf.getOu());
			break;
		case (int) GcAcesso.ACESSO_LOTACAO_E_SUPERIORES:
			for (DpLotacao lot = inf.getLotacao(); lot != null; lot = lot.getLotacaoPai())
				acessos.add(lot);
			break;
		case (int) GcAcesso.ACESSO_LOTACAO_E_INFERIORES:
			acessos.add(inf.getLotacao());
			HashSet<DpLotacao> set = new HashSet<DpLotacao>();
			inf.getLotacao().addTodasLotacoesSubordinadas(set);
			acessos.addAll(set);
			break;
		case (int) GcAcesso.ACESSO_LOTACAO:
			acessos.add(inf.getLotacao());
			break;
		case (int) GcAcesso.ACESSO_PESSOAL:
			acessos.add(inf.getAutor());
			break;
		case (int) GcAcesso.ACESSO_LOTACAO_E_GRUPO:
			acessos.add(inf.getLotacao());
			if (inf.getGrupo() != null) {
				ArrayList<ConfiguracaoGrupo> configuracoesGrupo = Cp.getInstance().getConf()
						.obterCfgGrupo(ContextoPersistencia.em().find(CpGrupo.class, inf.getGrupo().getHisIdIni()));
				for (ConfiguracaoGrupo cg : configuracoesGrupo) {
					CpConfiguracao cfg = cg.getCpConfiguracao();
					TipoConfiguracaoGrupoEnum tipo = cg.getTipo();
					switch (tipo) {
					case LOTACAO:
						acessos.add(cfg.getLotacao());
						break;
					case PESSOA:
						acessos.add(cfg.getDpPessoa());
						break;
					}
				}
			}
		}
		return acessos;
	}

}
