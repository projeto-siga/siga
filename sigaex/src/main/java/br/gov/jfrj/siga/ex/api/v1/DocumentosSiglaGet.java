package br.gov.jfrj.siga.ex.api.v1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaGet;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.vo.ExDocumentoVO;

public class DocumentosSiglaGet implements IDocumentosSiglaGet {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		DpPessoa cadastrante = ctx.getCadastrante();
		DpPessoa titular = cadastrante;
		DpLotacao lotaTitular = cadastrante.getLotacao();

		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp);

		ctx.assertAcesso(mob, titular, lotaTitular);

		ExDocumento doc = mob.doc();
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

		// Recebimento automático
		if (Prop.getBool("recebimento.automatico") 
				&& Ex.getInstance().getComp().deveReceberEletronico(titular, lotaTitular, mob)) {
			try {
				Ex.getInstance().getBL().receber(cadastrante, titular, lotaTitular, mob, new Date());
			} catch (Exception e) {
				e.printStackTrace(System.out);
				throw e;
			}
		}

		final ExDocumentoVO docVO = new ExDocumentoVO(doc, mob, cadastrante, titular, lotaTitular, true, false, true);
		// TODO: Resolver o problema declares multiple JSON fields named
		// serialVersionUID
		// Usado o Expose temporariamente
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
				.excludeFieldsWithModifiers(Modifier.TRANSIENT).create();

		String json = gson.toJson(docVO);

		resp.inputstream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
		resp.contenttype = "application/json";
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
						? (TypeAdapter<T>) new HibernateProxyTypeAdapter(gson)
						: null);
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
