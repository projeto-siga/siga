package br.gov.jfrj.siga.api.v1;

import org.apache.commons.lang3.StringUtils;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.Cargo;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.EntidadeItem;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.FuncaoConfianca;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.ISelecionarSiglaGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Localidade;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Lotacao;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Orgao;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Pessoa;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Uf;
import br.gov.jfrj.siga.base.Contexto;
import br.gov.jfrj.siga.cp.model.enm.EntidadeEnum;
import br.gov.jfrj.siga.cp.util.SiglaDeEntidadeParser;
import br.gov.jfrj.siga.dp.CpLocalidade;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.GenericoSelecao;
import br.gov.jfrj.siga.vraptor.PrincipalController;

public class SelecionarSiglaGet implements ISelecionarSiglaGet {
    @Override
    public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
        if (StringUtils.isEmpty(req.sigla))
            throw new SwaggerException("O parâmetro sigla é obrigatório.", 400, null, req, resp, null);

        DpPessoa pes = ctx.getTitular();
        DpLotacao lot = ctx.getLotaTitular();
        String incluirMatricula = "";
        if (req.matricula != null) {
            pes = ctx.getSigaObjects().daoPes(req.matricula);
            lot = pes.getLotacao();
            incluirMatricula = "&matricula=" + req.matricula;
        } else {
            incluirMatricula = "&matricula=" + ctx.getTitular().getSiglaCompleta();
        }

        String urlBase = Contexto.urlBase(ctx.getCtx().getRequest());
        final GenericoSelecao sel = PrincipalController.buscarGenericoPorSigla(req.sigla, pes, lot, incluirMatricula,
                urlBase);

        if (sel.getId() == null)
            return;
        
        EntidadeItem entidade = new EntidadeItem();
        entidade.refId = sel.getId().toString();
        entidade.sigla = sel.getSigla();
        if (entidade.sigla != null)
            entidade.codigo = sel.getSigla().replace("-", "").replace("/", "");

        EntidadeEnum tipo = null;
        SiglaDeEntidadeParser sp = new SiglaDeEntidadeParser(entidade.sigla);
        if (sp.isValida()) {
            if (sp.getEspecie() != null) {
                tipo = EntidadeEnum.EX_DOCUMENTO;
            } else if (sp.getModulo() != null) {
                switch (sp.getModulo()) {
                    case "SR": // Solicitacoes
                    case "TMPSR":
                        tipo = EntidadeEnum.SR_SOLICITACAO;
                        break;
                    case "GC": // Conhecimentos
                    case "TMPGC":
                        tipo = EntidadeEnum.GC_INFORMACAO;
                        break;
                    case "DP": // Diagramas
                        tipo = EntidadeEnum.WF_DIAGRAMA;
                        break;
                    case "WF": // Procedimentos
                        tipo = EntidadeEnum.WF_PROCEDIMENTO;
                        break;
                    case "TP": // Transportes
                        break;
                }
            }
        }
        if (tipo != null) {
            entidade.modulo = tipo.getModulo().name();
            entidade.tipo = tipo.name();
        }
        resp.entidade = entidade;
    }

    private Pessoa pessoaToResultadoPesquisa(DpPessoa p) {
        Pessoa pessoa = new Pessoa();
        Orgao orgao = new Orgao();
        Lotacao lotacao = new Lotacao();
        Localidade localidade = new Localidade();
        Cargo cargo = new Cargo();
        FuncaoConfianca funcao = new FuncaoConfianca();
        // Pessoa
        pessoa.idPessoaIni = p.getIdInicial().toString();
        pessoa.sigla = p.getSiglaCompleta();
        pessoa.nome = p.getNomePessoa();
        pessoa.isExternaPessoa = p.isUsuarioExterno();

        // Orgao Pessoa
        CpOrgaoUsuario o = p.getOrgaoUsuario();
        orgao.idOrgao = o.getId().toString();
        orgao.nome = o.getNmOrgaoAI();
        orgao.sigla = o.getSigla();

        // Lotacao Pessoa
        DpLotacao l = p.getLotacao();
        lotacao.idLotacao = l.getId().toString();
        lotacao.nome = l.getNomeLotacao();
        lotacao.sigla = l.getSigla();
        lotacao.orgao = orgao;

        // Localidade
        CpLocalidade loc = l.getLocalidade();
        if (loc != null) {
            localidade.idLocalidade = loc.getId().toString();
            localidade.nome = loc.getNmLocalidade();
            Uf uf = new Uf();
            uf.idUf = loc.getUF().getIdUF().toString();
            uf.nomeUf = loc.getUF().getNmUF();
            localidade.uf = uf;
            lotacao.localidade = localidade;
        }

        // Cargo Pessoa
        DpCargo c = p.getCargo();
        if (c != null) {
            cargo.idCargo = c.getId().toString();
            cargo.nome = c.getNomeCargo();
        }
        // Função Pessoa
        DpFuncaoConfianca f = p.getFuncaoConfianca();
        if (f != null) {
            funcao.idFuncaoConfianca = f.getId().toString();
            funcao.nome = f.getNomeFuncao();
        }

        pessoa.lotacao = lotacao;
        pessoa.cargo = cargo;
        pessoa.funcaoConfianca = funcao;
        return pessoa;
    }

    @Override
    public String getContext() {
        return "selecionar pessoas";
    }
}
