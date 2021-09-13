
package br.gov.jfrj.siga.ex.api.v1;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.base.Data;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorGrupoEnum;
import br.gov.jfrj.siga.dp.CpMarca;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentoPesq;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosGet;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExBL;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilApiBuilder;

public class DocumentosGet implements IDocumentosGet {
	final private static String SIGA_DOC_PESQ_PESQDESCR = "SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;PESQ:Pesquisar;PESQDESCR:Pesquisar descrição";
	final private static String SIGA_DOC_PESQ_DTLIMITADA = "SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;PESQ:Pesquisar;DTLIMITADA:Pesquisar somente com data limitada";
	final static public Long MAXIMO_DIAS_PESQUISA = 30L;
	DpPessoa titular;
	DpLotacao lotaTitular;

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		final ExMobilApiBuilder builder = new ExMobilApiBuilder();
		Date dtIni = null;
		Date dtFim = null;
		Long qtdMaxima = req.qtdmax;

		if (req.qtdmax == null)
			qtdMaxima = 50L;
		if (qtdMaxima > 50)
			throw new RegraNegocioException("A quantidade máxima de itens a trazer é de 50 documentos.");

		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(titular, lotaTitular,
				SIGA_DOC_PESQ_DTLIMITADA)) {
			if (req.dtinicial != null && !"".equals(req.dtinicial))
				validarLimiteDeDatas(req.dtinicial, req.dtfinal);
			else
				throw new RegraNegocioException("Período para pesquisa não deve ser superior a "
						+ MAXIMO_DIAS_PESQUISA.toString() + " dias. Informe a Data Inicial e/ou Final.");
		}

		if (req.dtinicial != null && !"".equals(req.dtinicial))
			dtIni = toDate(req.dtinicial + " 00:00:00");

		if (req.dtfinal != null && !"".equals(req.dtfinal))
			dtFim = toDate(req.dtfinal + " 23:59:59");

		if (!(req.ordenacao != null && req.ordenacao > 0 && req.ordenacao < 9))
			throw new SwaggerException("Ordenação inválida: (" + req.ordenacao + "). Deve ser numérico de 1 a 6.", 400,
					null, req, resp, null);

		CpMarcador marcador = null;
		if (req.marcador != null) {
			List<CpMarcador> listMarcador = CpDao.getInstance().consultaCpMarcadorAtivoPorNome(req.marcador,
					lotaTitular);
			if (listMarcador.size() > 0) {
				marcador = listMarcador.get(0);
			} else {
				throw new SwaggerException("Não existe marcador com o nome especificado (" + req.marcador + ")", 400,
						null, req, resp, null);
			}
		}
		Long idLota = req.idlotacao;
		if (req.idlotacao == null)
			idLota = lotaTitular.getIdInicial();
		if (req.idlotacao != null && !req.idlotacao.equals(lotaTitular.getIdInicial()))
			throw new SwaggerException("Usuário não autorizado a pesquisar documentos de outra lotação.", 400, null,
					req, resp, null);

		if (req.grupomarcador != null && CpMarcadorGrupoEnum.getByNome(req.grupomarcador) == null)
			throw new SwaggerException("Não existe grupo de marcador com o nome especificado: " + req.grupomarcador,
					400, null, req, resp, null);

		builder.setOffset(req.offset).setQtdMax(qtdMaxima).setOrdenacao(req.ordenacao)
				.setMarcador(marcador != null ? Long.valueOf(marcador.getIdInicial()) : null)
				.setGrupoMarcador(CpMarcadorGrupoEnum.getByNome(req.grupomarcador)).setDtDocIni(dtIni)
				.setDtDocFim(dtFim).setIdCadastrante(req.idpessoa).setIdLotaCadastrante(idLota);

		List<Object[]> l = ExDao.getInstance().consultarPorFiltro(builder);
		if (l.isEmpty())
			throw new SwaggerException("Nenhum documento foi encontrado com os argumentos informados.", 404, null, req,
					resp, null);

		resp.list = l.stream().map(this::toResultadoPesquisa).collect(Collectors.toList());
	}

	private void validarLimiteDeDatas(final String dtDocString, final String dtDocFinalString) {
		final String formato = "yyyy-MM-dd";
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
		LocalDate dtIni = null;
		LocalDate dtFinal = null;
		LocalDate dataAtual = LocalDate.now();

		if (dtDocString != null && !"".equals(dtDocString)) {
			if (Data.valida(dtDocString, formato)) {
				dtIni = LocalDate.parse(dtDocString, formatter);
			} else {
				throw new RegraNegocioException("Data Inicial inválida.");
			}
		} else {
			throw new RegraNegocioException("Data Inicial não informada. Período para pesquisa não deve ser superior à "
					+ MAXIMO_DIAS_PESQUISA.toString() + " dias.");
		}

		if (dtDocFinalString != null && !"".equals(dtDocFinalString)) {
			if (Data.valida(dtDocFinalString, formato)) {
				dtFinal = LocalDate.parse(dtDocFinalString, formatter);
				if (ChronoUnit.DAYS.between(dtIni, dtFinal) > MAXIMO_DIAS_PESQUISA) {
					throw new RegraNegocioException("Período para pesquisa não deve ser superior a "
							+ MAXIMO_DIAS_PESQUISA.toString() + " dias.");
				}
			} else {
				throw new RegraNegocioException("Data Final inválida.");
			}
		} else {
			if (ChronoUnit.DAYS.between(dtIni, dataAtual) > MAXIMO_DIAS_PESQUISA) {
				throw new RegraNegocioException("Período para pesquisa não deve ser superior a "
						+ MAXIMO_DIAS_PESQUISA.toString() + " dias. Informe a Data Inicial e/ou Final.");
			}
		}
	}

	private Date toDate(String dt) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.parse(dt, formatter);
		return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	private DocumentoPesq toResultadoPesquisa(Object[] obj) {
		CpMarca mar = (CpMarca) obj[0];
		CpMarcador marcador = (CpMarcador) obj[1];
		ExMobil mob = (ExMobil) obj[2];
		ExDocumento doc = (ExDocumento) obj[3];
		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		DocumentoPesq d = new DocumentoPesq();
		d.sigla = mob.getSigla();
		if (mar.getDpPessoaIni() != null) {
			d.cadastrantesigla = mar.getDpPessoaIni().getSigla();
			d.cadastrantenome = mar.getDpPessoaIni().getNomePessoa();
		}
		d.dtinimarca = df.format(mar.getDtIniMarca());
		d.descricaomarcador = marcador.getDescrMarcador();
		d.nomegrupomarcador = marcador.getIdGrupo().getNome();
		Ex.getInstance().getBL();
		d.descricaodocumento = (Prop.isGovSP() ? doc.getDescrDocumento()
				: ExBL.descricaoSePuderAcessar(doc, this.titular, this.lotaTitular));
		if (doc.getDtDoc() != null)
			d.dtdoc = df.format(doc.getDtDoc());
		d.dtregdoc = df.format(doc.getDtRegDoc());
		d.lotacadastrantesigla = doc.getLotaCadastrante().getSigla();
		d.lotacadastrantenome = doc.getLotaCadastrante().getDescricao();
		if (doc.getLotaSubscritor() != null) {
			d.lotasubscritorsigla = doc.getLotaSubscritor().getSigla();
			d.lotasubscritornome = doc.getLotaSubscritor().getNomeLotacao();
		}
		d.modelo = doc.getExModelo().getDescricao();
		if (doc.getExMobilPai() != null)
			d.siglamobilpai = doc.getExMobilPai().getSigla();
		if (doc.getTitular() != null)
			d.titular = doc.getTitular().getSigla();
		return d;
	}

	@Override
	public String getContext() {
		return "obter documento";
	}

}
