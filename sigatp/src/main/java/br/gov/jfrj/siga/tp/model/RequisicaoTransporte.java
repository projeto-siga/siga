package br.gov.jfrj.siga.tp.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NoResultException;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.validator.constraints.NotEmpty;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.feature.converter.entity.vraptor.ConvertableEntity;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.tp.util.Reflexao;
import br.gov.jfrj.siga.tp.util.SigaTpException;
import br.gov.jfrj.siga.tp.validation.annotation.Data;
import br.gov.jfrj.siga.tp.validation.annotation.Sequence;
import br.gov.jfrj.siga.tp.vraptor.ServicoVeiculoController;
import br.gov.jfrj.siga.tp.vraptor.i18n.MessagesBundle;
import br.gov.jfrj.siga.uteis.SiglaDocumentoType;
import br.gov.jfrj.siga.vraptor.handler.Resources;

@SuppressWarnings({ "serial", "deprecation" })
@Entity
@Audited
@Table(name = "REQUISICAOTRANSPORTE", schema = "SIGATP")
public class RequisicaoTransporte extends TpModel implements Comparable<RequisicaoTransporte>, ConvertableEntity {
    private static final String IMG_LINKNOVAJANELAICON = "/sigatp/public/images/linknovajanelaicon.png";
    private static final String END_23_59_59 = "23:59:59";
    private static final String START_00_00_00 = "00:00:00";
    public static final ActiveRecord<RequisicaoTransporte> AR = new ActiveRecord<>(RequisicaoTransporte.class);

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence_generator")
    @SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "SIGATP.hibernate_sequence")
    private Long id;

	@Sequence(propertieOrgao = "cpOrgaoUsuario", siglaDocumento = SiglaDocumentoType.RTP)
    @Column(updatable = false)
    private Long numero;

    @Data(descricaoCampo = "dataHora")
    private Calendar dataHora;

    @NotNull
    @Data(descricaoCampo = "dataHoraSa\u00EDdaPrevista")
    private Calendar dataHoraSaidaPrevista;

    @Data(descricaoCampo = "dataHoraRetornoPrevisto")
    private Calendar dataHoraRetornoPrevisto;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoRequisicao tipoRequisicao;

    @ElementCollection(targetClass = TipoDePassageiro.class)
    @JoinTable(name = "requisicao_tipopassageiro", joinColumns = @JoinColumn(name = "requisicaoTransporte_Id"))
    @Column(name = "tipoPassageiro", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<TipoDePassageiro> tiposDePassageiro;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ID_FINALIDADE")
    private FinalidadeRequisicao tipoFinalidade;

    //Campo detalheFinalidade no form Requisicoes
    private String finalidade;

    private String passageiros;

    @NotNull
    @NotEmpty
    private String itinerarios;

    @OneToMany(orphanRemoval = true, mappedBy = "requisicaoTransporte")
    private List<Andamento> andamentos;

    @Transient
    private Andamento ultimoAndamento;

    @Transient
    private EstadoRequisicao ultimoEstado;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "missao_requisTransporte", joinColumns = @JoinColumn(name = "requisicaoTransporte_Id"), inverseJoinColumns = @JoinColumn(name = "missao_Id"))
    private List<Missao> missoes;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name = "ID_ORGAO_USU")
    private CpOrgaoUsuario cpOrgaoUsuario;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name = "ID_SOLICITANTE")
    private DpPessoa solicitante;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name = "ID_COMPLEXO")
    private CpComplexo cpComplexo;

    @OneToOne(fetch = FetchType.LAZY, optional = true, mappedBy = "requisicaoTransporte")
    private ServicoVeiculo servicoVeiculo;

    private Integer numeroDePassageiros;

    @NotNull
    private boolean origemExterna;

    @Transient
    private Long idSolicitante;

    public RequisicaoTransporte() {
        id = Long.valueOf(0);
        tipoRequisicao = TipoRequisicao.NORMAL;
        ultimoAndamento = new Andamento();
        this.origemExterna = false;
        this.andamentos = new ArrayList<Andamento>();
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public Calendar getDataHora() {
        return dataHora;
    }

    public void setDataHora(Calendar dataHora) {
        this.dataHora = dataHora;
    }

    public Calendar getDataHoraSaidaPrevista() {
        return dataHoraSaidaPrevista;
    }

    public void setDataHoraSaidaPrevista(Calendar dataHoraSaidaPrevista) {
        this.dataHoraSaidaPrevista = dataHoraSaidaPrevista;
    }

    public Calendar getDataHoraRetornoPrevisto() {
        return dataHoraRetornoPrevisto;
    }

    public void setDataHoraRetornoPrevisto(Calendar dataHoraRetornoPrevisto) {
        this.dataHoraRetornoPrevisto = dataHoraRetornoPrevisto;
    }

    public TipoRequisicao getTipoRequisicao() {
        return tipoRequisicao;
    }

    public void setTipoRequisicao(TipoRequisicao tipoRequisicao) {
        this.tipoRequisicao = tipoRequisicao;
    }

    public List<TipoDePassageiro> getTiposDePassageiro() {
        return tiposDePassageiro;
    }

    public void setTiposDePassageiro(List<TipoDePassageiro> tiposDePassageiro) {
        this.tiposDePassageiro = tiposDePassageiro;
    }

    public FinalidadeRequisicao getTipoFinalidade() {
        return tipoFinalidade;
    }

    public void setTipoFinalidade(FinalidadeRequisicao tipoFinalidade) {
        this.tipoFinalidade = tipoFinalidade;
    }

    public String getFinalidade() {
        return finalidade;
    }

    public void setFinalidade(String finalidade) {
        this.finalidade = finalidade;
    }

    public String getPassageiros() {
        return passageiros;
    }

    public void setPassageiros(String passageiros) {
        this.passageiros = passageiros;
    }

    public String getItinerarios() {
        return itinerarios;
    }

    public void setItinerarios(String itinerarios) {
        this.itinerarios = itinerarios;
    }

    public List<Andamento> getAndamentos() {
        return andamentos;
    }

    public void setAndamentos(List<Andamento> andamentos) {
        this.andamentos = andamentos;
    }

    public void addAndamento(Andamento andamento) {
        this.andamentos.add(andamento);
    }

    public List<Missao> getMissoes() {
        return missoes;
    }

    public void setMissoes(List<Missao> missoes) {
        this.missoes = missoes;
    }

    public CpOrgaoUsuario getCpOrgaoUsuario() {
        return cpOrgaoUsuario;
    }

    public void setCpOrgaoUsuario(CpOrgaoUsuario cpOrgaoUsuario) {
        this.cpOrgaoUsuario = cpOrgaoUsuario;
    }

    public DpPessoa getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(DpPessoa solicitante) {
        this.solicitante = solicitante;
    }

    public CpComplexo getCpComplexo() {
        return cpComplexo;
    }

    public void setCpComplexo(CpComplexo cpComplexo) {
        this.cpComplexo = cpComplexo;
    }

    public ServicoVeiculo getServicoVeiculo() {
        return servicoVeiculo;
    }

    public void setServicoVeiculo(ServicoVeiculo servicoVeiculo) {
        this.servicoVeiculo = servicoVeiculo;
    }

    public Integer getNumeroDePassageiros() {
        return numeroDePassageiros;
    }

    public void setNumeroDePassageiros(Integer numeroDePassageiros) {
        this.numeroDePassageiros = numeroDePassageiros;
    }

    public boolean isOrigemExterna() {
        return origemExterna;
    }

    public void setOrigemExterna(boolean origemExterna) {
        this.origemExterna = origemExterna;
    }

    public Long getIdSolicitante() {
        return idSolicitante;
    }

    public void setIdSolicitante(Long idSolicitante) {
        this.idSolicitante = idSolicitante;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUltimoEstado(EstadoRequisicao ultimoEstado) {
        this.ultimoEstado = ultimoEstado;
    }

    public String getDadosParaExibicao() {
        return this.numero.toString();
    }

    @Override
    public int compareTo(RequisicaoTransporte o) {
        return (this.dataHoraSaidaPrevista).compareTo(o.dataHoraSaidaPrevista);
    }

    public String getDescricaoCompleta() {
        StringBuilder saida = new StringBuilder();
        saida.append(buscarSequence().toString());

        boolean temTipoPassageiro = false;
        for (Iterator<TipoDePassageiro> iterator = tiposDePassageiro.iterator(); iterator.hasNext();) {
            TipoDePassageiro tipo = iterator.next();
            if (!temTipoPassageiro) {
                saida.append(" - ");
                temTipoPassageiro = true;
            } else
                saida.append("; ");

            saida.append(tipo.getDescricao());
        }

        if (servicoVeiculo != null) {
            String caminhoUrl = Resources.getInstance().urlFor(ServicoVeiculoController.class, "buscarServico", true, servicoVeiculo.getSequence());

            saida.append(" - ");
            saida.append("Servi&ccedil;o: " + servicoVeiculo.getSequence() + " <a href=\"#\" onclick=\"javascript:window.open('/sigatp" + caminhoUrl + "');\">");
            saida.append("<img src=\"" + IMG_LINKNOVAJANELAICON + "\" alt=\"Abrir em uma nova janela\" title=\"Abrir em uma nova janela\"></a>");
            saida.append(" (");
            saida.append(servicoVeiculo.getVeiculo().getDadosParaExibicao() + ")");
        } else {
            saida.append(" - ");
            saida.append(itinerarios.toString());
        }

        return saida.toString();
    }

    public String buscarSequence() {
        if (this.numero != null && this.numero != 0) {
            return cpOrgaoUsuario.getAcronimoOrgaoUsu().replace("-", "").toString() + "-" + Reflexao.recuperaAnotacaoField(this).substring(1) + "-"
                    + String.format("%04d", this.dataHora.get(Calendar.YEAR)) + "/" + String.format("%05d", numero) + "-" + Reflexao.recuperaAnotacaoField(this).substring(0, 1);
        } else {
            return "";
        }
    }
    
    public void setSequence(Object cpOrgaoUsuarioObject) {
        CpOrgaoUsuario orgaoUsuario = (CpOrgaoUsuario) cpOrgaoUsuarioObject;
        int year = Calendar.getInstance().get(Calendar.YEAR);

        StringBuilder query = new StringBuilder();
        query.append("SELECT req FROM RequisicaoTransporte req WHERE req.id = ");
        query.append("(SELECT MAX(r.id) FROM RequisicaoTransporte r WHERE r.numero = ");
        query.append("(SELECT MAX(rt.numero) FROM RequisicaoTransporte rt ");
        query.append("WHERE cpOrgaoUsuario.id = " + orgaoUsuario.getId());
        query.append(" AND YEAR(dataHora) = " + year);
        query.append(" AND r.cpOrgaoUsuario.id = rt.cpOrgaoUsuario.id");
        query.append(" AND YEAR(r.dataHora) = YEAR(rt.dataHora)))");

        Query qry = AR.em().createQuery(query.toString());
        try {
            Object obj = qry.getSingleResult();
            this.numero = ((RequisicaoTransporte) obj).numero + 1;
        } catch (NoResultException ex) {
            this.numero = Long.valueOf(1);
        }
    }

	public List<Missao> getMissoesOrdenadas() {
    	TreeMap<Integer,  List<Missao>> hMissoes = new TreeMap<Integer,  List<Missao>>();

    	for (Missao item : missoes) {
    		Integer ordem =  this.getUltimoEstadoNestaMissao(item.getId()).getOrdem();
			List<Missao> current = hMissoes.get(ordem) != null ? hMissoes.get(ordem) : new ArrayList<Missao>();
			current.add(item);
			hMissoes.put(ordem, current);
    	}
    	
    	missoes.removeAll(missoes);
    	
    	for (Integer ordem : hMissoes.keySet()) {
        	List<Missao> listaOrdenada = new ArrayList<Missao>(hMissoes.get(ordem));
			Collections.sort(listaOrdenada);
            Collections.reverse(listaOrdenada);
    		missoes.addAll(listaOrdenada);
    	}
    	
        return missoes;
    }

    public static RequisicaoTransporte buscar(String codigoRequisicao) {
        String[] partesDoCodigo = null;
        RequisicaoTransporte requisicaoTransporte = new RequisicaoTransporte();
        try {
            partesDoCodigo = codigoRequisicao.split("[-/]");
        } catch (Exception e) {
            throw new AplicacaoException(MessagesBundle.getMessage("requisicaoTransporte.codigoRequisicao.exception", codigoRequisicao));
        }

        CpOrgaoUsuario cpOrgaoUsuario = CpOrgaoUsuario.AR.find("acronimoOrgaoUsu", partesDoCodigo[0]).first();
        Integer ano = new Integer(Integer.parseInt(partesDoCodigo[2]));
        Long numero = new Long(Integer.parseInt(partesDoCodigo[3]));
        String siglaDocumento = partesDoCodigo[4] + partesDoCodigo[1];
        if (!Reflexao.recuperaAnotacaoField(requisicaoTransporte).equals(siglaDocumento)) {
            throw new AplicacaoException(MessagesBundle.getMessage("requisicaoTransporte.siglaDocumento.exception", codigoRequisicao));
        }
        
        List<RequisicaoTransporte> requisicoesTransporte = RequisicaoTransporte.AR.find("cpOrgaoUsuario = ? and numero = ? and YEAR(dataHora) = ?", cpOrgaoUsuario, numero, ano).fetch();

        if (requisicoesTransporte.size() > 1) {
            throw new AplicacaoException(MessagesBundle.getMessage("requisicaoTransporte.codigoDuplicado.exception"));
        }
        if (requisicoesTransporte.size() == 0) {
            throw new AplicacaoException(MessagesBundle.getMessage("requisicaoTransporte.codigoInvalido.exception"));
        }
        return requisicoesTransporte.get(0);
    }

    public Andamento getUltimoAndamento() {
        if (andamentos != null && !andamentos.isEmpty()) {
            ordenarAndamentosESetarUltimo();
        } else if (!this.recarregarAndamentos()) {
            setUltimoAndamento(new Andamento());
        } else {
            ordenarAndamentosESetarUltimo();
        }
        return this.ultimoAndamento;
    }

    private boolean recarregarAndamentos() {
        this.andamentos = Andamento.AR.find("requisicaoTransporte.id", this.id).fetch();
        if (this.andamentos == null || this.andamentos.isEmpty()) {
            return false;
        }
        return true;
    }

    private void ordenarAndamentosESetarUltimo() {
        Collections.sort(andamentos);
        setUltimoAndamento(andamentos.get(andamentos.size() - 1));
    }

    public EstadoRequisicao getUltimoEstadoNestaMissao(Long idMissao) {
        Missao missao = Missao.AR.findById(idMissao);
        Andamento andamento = (Andamento) Andamento.AR.find("requisicaoTransporte = ? and missao = ? order by dataAndamento desc", this, missao).first();
        return andamento.getEstadoRequisicao();
    }

    public EstadoRequisicao getUltimoEstado() {
        Andamento andamento = getUltimoAndamento();
        if (andamento.getId() == null) {
            return null;
        }
        return andamento.getEstadoRequisicao();
    }

    private void setUltimoAndamento(Andamento andamento) {
        this.ultimoAndamento = andamento;
    }

    @SuppressWarnings("unchecked")
    public static List<RequisicaoTransporte> listar(EstadoRequisicao... estadoRequisicao) {
        List<RequisicaoTransporte> requisicoes;

        StringBuilder qrl = new StringBuilder("SELECT req from RequisicaoTransporte req," + "Andamento an1 " + "WHERE req.id = an1.requisicaoTransporte.id "
                + "AND (an1.requisicaoTransporte.id, an1.dataAndamento) IN (" + "SELECT an1.requisicaoTransporte.id, max(an1.dataAndamento) " + "FROM Andamento an1 "
                + "GROUP BY an1.requisicaoTransporte.id) " + "AND (");

        for (EstadoRequisicao estado : estadoRequisicao) {
            qrl.append("an1.estadoRequisicao = '" + estado.getDescricao() + "' OR ");
        }
        StringBuilder qrlFinal = new StringBuilder(qrl.substring(0, qrl.length() - 3) + ")");
        qrlFinal.append("ORDER BY req.id");

        try {
            Query qry = AR.em().createQuery(qrlFinal.toString());
            requisicoes = (List<RequisicaoTransporte>) qry.getResultList();
        } catch (NoResultException ex) {
            requisicoes = null;
        }

        return requisicoes;
    }

    @SuppressWarnings("unchecked")
    public static List<RequisicaoTransporte> listarParaAgendamento(DpPessoa dpPessoa) {
        List<RequisicaoTransporte> requisicoes;
        long orgaoUsuarioId = dpPessoa.getOrgaoUsuario().getIdOrgaoUsu();
        long usuarioId = dpPessoa.getIdPessoa();

        StringBuilder qrl = new StringBuilder("SELECT req from RequisicaoTransporte req," + "Andamento an1 " + 
        		"WHERE req.id = an1.requisicaoTransporte.id " + "AND req.cpOrgaoUsuario.idOrgaoUsu = "
                + orgaoUsuarioId + " " + "AND req.solicitante.idPessoa = " + usuarioId + " " +
				"AND (an1.requisicaoTransporte.id, an1.dataAndamento) IN (" 
				+ "SELECT an1.requisicaoTransporte.id, max(an1.dataAndamento) "
                + "FROM Andamento an1 " + "GROUP BY an1.requisicaoTransporte.id) " + "AND (");

        for (EstadoRequisicao estado : Arrays.asList(EstadoRequisicao.values())) {
            if (estado.podeAgendar()) {
                qrl.append("an1.estadoRequisicao = '" + estado.getDescricao() + "' OR ");
            }
        }
        StringBuilder qrlFinal = new StringBuilder(qrl.substring(0, qrl.length() - 3) + ")");
        qrlFinal.append("ORDER BY req.id");

        try {
            Query qry = AR.em().createQuery(qrlFinal.toString());
            requisicoes = (List<RequisicaoTransporte>) qry.getResultList();
        } catch (NoResultException ex) {
            requisicoes = null;
        }

        return requisicoes;
    }

    public boolean getPodeAgendar() {
        Andamento ultAnd = getUltimoAndamento();
        return ultAnd.getEstadoRequisicao().podeAgendar();
    }
    
	public Boolean cancelarSemMissao(DpPessoa responsavel, String descricao)  {
		String mensagemCancelamento = MessagesBundle.getMessage("requisicaoTransporte.cancelamento.mensagem", descricao);  
		return alterarUltimoAndamentoRequisicao(responsavel, mensagemCancelamento, false);
	}
    
	public Boolean cancelar(DpPessoa responsavel, String descricao)  {
		return alterarUltimoAndamentoRequisicao(responsavel, descricao, true);  
	}
	
	private Boolean alterarUltimoAndamentoRequisicao(DpPessoa responsavel, String descricao, Boolean alteraMissao)  {
        EstadoRequisicao ultimoAndamentoRequisicao = this.getUltimoAndamento().getEstadoRequisicao();
        if (alteraMissao & ultimoAndamentoRequisicao == EstadoRequisicao.PROGRAMADA) {
        	alterarMissoesDaRequisicao(missoes);
        }

        if (ultimoAndamentoRequisicao == EstadoRequisicao.ABERTA || ultimoAndamentoRequisicao == EstadoRequisicao.AUTORIZADA || ultimoAndamentoRequisicao == EstadoRequisicao.REJEITADA
                || ultimoAndamentoRequisicao == EstadoRequisicao.PROGRAMADA) {
            Andamento andamento = new Andamento();
            andamento.setResponsavel(responsavel);
            andamento.setDataAndamento(Calendar.getInstance());
            andamento.setEstadoRequisicao(EstadoRequisicao.CANCELADA);
            andamento.setDescricao(descricao);
            andamento.setRequisicaoTransporte(this);
            andamento.save();
            setUltimoAndamento(andamento);
            return true;
        }

        return false;
    }

    public void alterarMissoesDaRequisicao(List<Missao> missoes) {
    	boolean missaoAlterada = false;
    	for (Missao missao : missoes) {
            for (Iterator<RequisicaoTransporte> iterator = missao.getRequisicoesTransporte().iterator(); iterator.hasNext();) {
                RequisicaoTransporte requisicaoTransporte = iterator.next();
                if (requisicaoTransporte.id == this.id) {
                    iterator.remove();
                    missaoAlterada = true;
                }
            }

            if (missao.getRequisicoesTransporte().isEmpty()) {
                missao.setEstadoMissao(EstadoMissao.CANCELADA);
            }
            if (missaoAlterada){
                missao.save();
            }
        }
    }

    public boolean ordemDeDatasCorreta() {
        return this.dataHoraSaidaPrevista.before(this.dataHoraRetornoPrevisto);
    }

    public void excluir(Boolean ehRequisicaoServico) throws SigaTpException {
        EstadoRequisicao ultimoAndamentoRequisicao = this.getUltimoAndamento().getEstadoRequisicao();

        if (ultimoAndamentoRequisicao == EstadoRequisicao.ABERTA || ultimoAndamentoRequisicao == EstadoRequisicao.REJEITADA || ultimoAndamentoRequisicao == EstadoRequisicao.AUTORIZADA) {

            if (ehRequisicaoServico) {
                if (servicoVeiculo != null) {
                    servicoVeiculo.delete();
                } else {
                    throw new SigaTpException(MessagesBundle.getMessage("requisicaoTransporte.naoEhRequisicaoServico.exception"));
                }
            } else if (servicoVeiculo != null) {
                throw new SigaTpException(MessagesBundle.getMessage("requisicaoTransporte.ehRequisicaoServico.exception"));
            }

            this.refresh();
            this.delete();
            return;
        }

        if (!ehRequisicaoServico) {
            throw new SigaTpException("requisicaoTransporte.naoPodeSerExcluida.exception");
        } else {
            throw new SigaTpException("requisicaoTransporte.favorCancelarServico.exception");
        }
    }

    public boolean contemTipoDePassageiro(TipoDePassageiro tipo) {
        if (tiposDePassageiro == null || tiposDePassageiro.isEmpty()) {
            return false;
        }
        if (tiposDePassageiro.contains(tipo)) {
            return true;
        }
        return false;
    }

    public boolean getPodeAlterar() {
        Andamento ultAnd = getUltimoAndamento();
        return ultAnd.getEstadoRequisicao() == EstadoRequisicao.ABERTA && this.origemExterna == false;
    }

	public boolean getPodeExcluir() {
		Andamento ultAnd = getUltimoAndamento();
		return ultAnd.getEstadoRequisicao() != EstadoRequisicao.CANCELADA;
	}

    @Override
    public Long getId() {
        return id;
    }

	public static List<RequisicaoTransporte> buscarRequisicoesAvancado(Calendar dataInicio, Calendar dataFim, CpOrgaoUsuario orgaoUsuario, EstadoRequisicao estadoRequisicao) throws Exception {
		if (dataInicio == null && dataFim == null && estadoRequisicao == null) {
			return new ArrayList<RequisicaoTransporte>();
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String dataInicioFormatada = dataInicio != null ? "to_date('" + sdf.format(dataInicio.getTime()) + " " + START_00_00_00 + "', 'DD/MM/YYYY HH24:MI:SS')" : "";
		String dataFimFormatada = dataFim != null ? "to_date('" + sdf.format(dataFim.getTime())  + " " + END_23_59_59 + "', 'DD/MM/YYYY HH24:MI:SS')" : "";

        String qrl = "SELECT req from RequisicaoTransporte req, Andamento an1 ";
        qrl += "WHERE req.id = an1.requisicaoTransporte.id ";
        qrl += "AND (an1.requisicaoTransporte.id, an1.dataAndamento) IN ("; 
        qrl += "SELECT an1.requisicaoTransporte.id, max(an1.dataAndamento) " + "FROM Andamento an1 ";
        qrl += "GROUP BY an1.requisicaoTransporte.id) ";

		if (dataInicio != null && dataFim != null) {
        	qrl+= " AND ((req.dataHoraRetornoPrevisto is null and req.dataHoraSaidaPrevista between " + dataInicioFormatada + " and " + dataFimFormatada + ")" + 
        		  " or (req.dataHoraRetornoPrevisto between " + dataInicioFormatada + " and " + dataFimFormatada + "))";
        }
        else if (dataInicio != null && dataFim == null) {
        	qrl+= " AND ((req.dataHoraRetornoPrevisto is null and req.dataHoraSaidaPrevista >= " + dataInicioFormatada + ") or (req.dataHoraRetornoPrevisto >= " + 
        			dataInicioFormatada + "))";
        }
        else if (dataInicio == null && dataFim != null) {
        	qrl+= " AND ((req.dataHoraRetornoPrevisto is null and req.dataHoraSaidaPrevista <= " + dataFimFormatada + ") or (req.dataHoraRetornoPrevisto <= " + 
        			dataFimFormatada + "))";
        }
        
        qrl += estadoRequisicao!= null ? " AND an1.estadoRequisicao = '" + estadoRequisicao.getDescricao() + "'" : "";
        
        qrl += " AND req.cpOrgaoUsuario.id = " + orgaoUsuario.getId() + " order by dataHoraSaidaPrevista, dataHoraRetornoPrevisto desc";
        return RequisicaoTransporte.AR.find(qrl).fetch();
	}
}