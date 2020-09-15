package br.gov.jfrj.siga.ex.api.v1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import com.crivano.swaggerservlet.SwaggerServlet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaGetRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaGetResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocSiglaGet;
import br.gov.jfrj.siga.ex.bl.CurrentRequest;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.RequestInfo;
import br.gov.jfrj.siga.ex.vo.ExDocumentoSigaLeVO;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class DocSiglaGet implements IDocSiglaGet {

	@Override
	public void run(DocSiglaGetRequest req, DocSiglaGetResponse resp) throws Exception {
		CurrentRequest.set(new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));

		SwaggerHelper.buscarEValidarUsuarioLogado();
		
		ApiContext apiContext = new ApiContext(true);
		SigaObjects so = SwaggerHelper.getSigaObjects();
		so.assertAcesso("DOC:Módulo de Documentos;" + "");

		try {
			DpPessoa cadastrante = so.getCadastrante();
			DpPessoa titular = cadastrante;
			DpLotacao lotaTitular = cadastrante.getLotacao();

			ExMobilDaoFiltro flt = new ExMobilDaoFiltro();
			flt.setSigla(req.sigla);
			ExMobil mob = ExDao.getInstance().consultarPorSigla(flt);
			ExDocumento doc = mob.doc();

			Utils.assertAcesso(mob, titular, lotaTitular);

			// Recebimento automático
			if (Ex.getInstance().getComp().podeReceberEletronico(titular, lotaTitular, mob)) {
				try {
					Ex.getInstance().getBL().receber(cadastrante, lotaTitular, mob, new Date());
					apiContext.close();
				} catch (Exception e) {
					e.printStackTrace(System.out);
					apiContext.rollback(e);
					throw e;
				}
			}

			// Mostra o último volume de um processo ou a primeira via de um
			// expediente
			if (mob == null || mob.isGeral()) {
				if (mob.getDoc().isFinalizado()) {
					if (doc.isProcesso())
						mob = doc.getUltimoVolume();
					else
						mob = doc.getPrimeiraVia();
				}
			}

			final ExDocumentoSigaLeVO docVO = new ExDocumentoSigaLeVO(doc, mob, cadastrante, titular, lotaTitular, true, false);
//TODO: Resolver o problema declares multiple JSON fields named serialVersionUID
			//Usado o Expose temporariamente
			Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
					.excludeFieldsWithModifiers(Modifier.TRANSIENT)
					.excludeFieldsWithoutExposeAnnotation()
					.create();
			String json = gson.toJson(docVO);

			resp.inputstream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
			resp.contenttype = "application/json";
		} catch (Exception e) {
			e.printStackTrace(System.out);
			apiContext.rollback(e);
			throw e;
		}

	}

	@Override
	public String getContext() {
		return "obter documento";
	}

	/**
	 * This TypeAdapter unproxies Hibernate proxied objects, and serializes them
	 * through the registered (or default) TypeAdapter of the base class.
	 */
	private static class HibernateProxyTypeAdapter extends TypeAdapter<HibernateProxy> {

		public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
			@Override
			@SuppressWarnings("unchecked")
			public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
				return (HibernateProxy.class.isAssignableFrom(type.getRawType())
						? (TypeAdapter<T>) new HibernateProxyTypeAdapter(gson) : null);
			}
		};
		private final Gson context;

		private HibernateProxyTypeAdapter(Gson context) {
			this.context = context;
		}

		@Override
		public HibernateProxy read(JsonReader in) throws IOException {
			throw new UnsupportedOperationException("Not supported");
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public void write(JsonWriter out, HibernateProxy value) throws IOException {
			if (value == null) {
				out.nullValue();
				return;
			}
			// Retrieve the original (not proxy) class
			Class<?> baseType = Hibernate.getClass(value);
			// Get the TypeAdapter of the original class, to delegate the
			// serialization
			TypeAdapter delegate = context.getAdapter(TypeToken.get(baseType));
			// Get a filled instance of the original class
			((HibernateProxy) value).getHibernateLazyInitializer().getImplementation();
			// Serialize the value
			// delegate.write(out, unproxiedValue);
			delegate.write(out, "__OMITIDO__");
		}
	}

}
