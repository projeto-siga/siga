package br.gov.jfrj.siga.tp.model;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.feature.converter.entity.vraptor.ConvertableEntity;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.tp.validation.annotation.Data;

@SuppressWarnings("serial")
@Entity
@Audited
@Table(name = "parametro", schema = "sigatp")
public class Parametro extends TpModel implements ConvertableEntity {

    public static final ActiveRecord<Parametro> AR = new ActiveRecord<Parametro>(Parametro.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(Parametro.class);

    @Id
    @GeneratedValue(generator = "hibernate_sequence_generator")
    @SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "sigatp.hibernate_sequence")
    private Long id;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name = "ID_ORGAO_USU")
    private CpOrgaoUsuario cpOrgaoUsuario;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name = "ID_LOTACAO")
    private DpLotacao dpLotacao;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name = "ID_PESSOA")
    private DpPessoa dpPessoa;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name = "ID_COMPLEXO")
    private CpComplexo cpComplexo;

    @NotNull
    private String nomeParametro;

    @NotNull
    private String valorParametro;

    @Data(descricaoCampo = "Data In\u00EDcio")
    private Calendar dataInicio;

    @Data(descricaoCampo = "Data Fim")
    private Calendar dataFim;

    @NotNull
    private String descricao;

    @Transient
 	public NivelDeParametro nivel;
    
    public Parametro() {
        this.id = 0L;
    }

	@Override
    public Long getId() {
        return id;
    }

	public void setId(Long id) {
        this.id = id;
    }

    public CpOrgaoUsuario getCpOrgaoUsuario() {
        return cpOrgaoUsuario;
    }

    public void setCpOrgaoUsuario(CpOrgaoUsuario cpOrgaoUsuario) {
        this.cpOrgaoUsuario = cpOrgaoUsuario;
    }

    public DpLotacao getDpLotacao() {
        return dpLotacao;
    }

    public void setDpLotacao(DpLotacao dpLotacao) {
        this.dpLotacao = dpLotacao;
    }

    public DpPessoa getDpPessoa() {
        return dpPessoa;
    }

    public void setDpPessoa(DpPessoa dpPessoa) {
        this.dpPessoa = dpPessoa;
    }

    public CpComplexo getCpComplexo() {
        return cpComplexo;
    }

    public void setCpComplexo(CpComplexo cpComplexo) {
        this.cpComplexo = cpComplexo;
    }

    public String getNomeParametro() {
        return nomeParametro;
    }

    public void setNomeParametro(String nomeParametro) {
        this.nomeParametro = nomeParametro;
    }

    public String getValorParametro() {
        return valorParametro;
    }

    public void setValorParametro(String valorParametro) {
        this.valorParametro = valorParametro;
    }

    public Calendar getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Calendar dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Calendar getDataFim() {
        return dataFim;
    }

    public void setDataFim(Calendar dataFim) {
        this.dataFim = dataFim;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public NivelDeParametro getNivel() {
		return nivel;
	}

	public void setNivel(NivelDeParametro nivel) {
		this.nivel = nivel;
	}
	
	@SuppressWarnings("unchecked")
    public static List<Parametro> listarTodos() {
		List<Parametro> parametros = Parametro.AR.findAll();
        configurarNivel(parametros);
        return parametros;
    }

	private static void configurarNivel(Parametro parametro) {
		if(parametro.dpPessoa != null) {
			parametro.nivel = NivelDeParametro.USUARIO;
		} else if(parametro.dpLotacao != null) {
			parametro.nivel = NivelDeParametro.LOTACAO;
		} else if(parametro.cpComplexo != null) {
			parametro.nivel = NivelDeParametro.COMPLEXO;
		} else if(parametro.cpOrgaoUsuario != null) {
			parametro.nivel = NivelDeParametro.ORGAO;
		} else {
			parametro.nivel = NivelDeParametro.GERAL;
		}
	}
	
	private static void configurarNivel(List<Parametro> parametros) {
		for (Parametro parametro : parametros) {
			configurarNivel(parametro);
		}
	}
	
	public static Parametro buscar(Long idBuscar) {
        Parametro retorno = null;
        try {
    		Map<String, Object> parametros = new HashMap<String,Object>();
    		parametros.put("id",idBuscar);
            retorno = Parametro.AR.find("id = :id", parametros).first();
            configurarNivel(retorno);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
        return retorno;
    }

	public static String buscarValorEmVigor(String nome, DpPessoa usuario, CpComplexo complexoPadrao) {
		String retorno = null;
		Calendar hoje = Calendar.getInstance();
		String queryComData = "SELECT pa FROM Parametro pa Where  nomeParametro = :nomeParametro "
				+ "and pa.dataInicio < :dataHoje "
				+ "and (pa.dataFim is null or pa.dataFim > :dataHoje) "
				+ "and (pa.dpPessoa is null or pa.dpPessoa = :usuario) "
				+ "and (pa.dpLotacao is null or pa.dpLotacao = :lotacao) "
				+ "and (pa.cpComplexo is null or pa.cpComplexo = :complexoPadrao) "
				+ "and (pa.cpOrgaoUsuario is null or cpOrgaoUsuario = :orgaoUsuario)";
		
		Query qry = Parametro.AR.em().createQuery(queryComData);
    	qry.setParameter("nomeParametro",nome);
    	qry.setParameter("dataHoje", hoje);
    	qry.setParameter("usuario",usuario);
    	qry.setParameter("lotacao", usuario.getLotacao());
    	qry.setParameter("complexoPadrao",complexoPadrao);
    	qry.setParameter("orgaoUsuario", usuario.getOrgaoUsuario());
    	@SuppressWarnings("unchecked")
		List<Parametro> parametros  = (List<Parametro>) qry.getResultList();
		

		configurarNivel(parametros);
		
		if((parametros != null) && !(parametros.isEmpty())) {
			if(parametros.size() == 1) {
				retorno = parametros.get(0).valorParametro;
			} else {
				NivelDeParametro nivel = NivelDeParametro.GERAL;
				for (Parametro parametro : parametros) {
					boolean teste = (parametro.dpPessoa != null);
					if(teste) {
						if(parametro.dpPessoa.equivale(usuario)) {
							retorno = parametro.valorParametro;
							break;
						}
					} else {
						if(parametro.nivel.compareTo(nivel) >= 0) {
							retorno = parametro.valorParametro;
							nivel = parametro.nivel;
						}
					}
				}
			}
		}
		return retorno;
	}

    public static String buscarConfigSistemaEmVigor(String nome) {
        String retorno = null;
        Calendar hoje = Calendar.getInstance();
		Map<String, Object> parametros_find = new HashMap<String,Object>();
		parametros_find.put("nome", nome);
		parametros_find.put("hoje", hoje);
        String queryComData = "nomeParametro = :nome "
                + "and dataInicio <= :hoje "
                + "and (dataFim is null or dataFim > :hoje) "
                + "and dpPessoa is null "
                + "and dpLotacao is null "
                + "and cpComplexo is null "
                + "and cpOrgaoUsuario is null";
        
        Parametro parametro = Parametro.AR.find(queryComData, parametros_find).first();
        if(parametro != null) {
            retorno = parametro.valorParametro;
        }
        return retorno;
    }
}