package br.gov.jfrj.siga.ex.api.v1;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.ocpsoft.prettytime.PrettyTime;

import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.bl.Ex;

public class Utils {

	public static String getUsuariosRestritos() {
		try {
			return Prop.get("username.restriction");
		} catch (Exception e) {
			throw new RuntimeException("Erro de configuração", e);
		}

	}

	public static String getJwtIssuer() {
		return "jwt.issuer";
	}

	public static String getJwtSecret() {
		return Prop.get("/siga.jwt.secret");
	}

	/**
	 * Remove os acentos da string
	 * 
	 * @param acentuado - String acentuada
	 * @return String sem acentos
	 */
	public static String removeAcento(String acentuado) {
		if (acentuado == null)
			return null;
		String temp = new String(acentuado);
		temp = temp.replaceAll("[ÃÂÁÀ]", "A");
		temp = temp.replaceAll("[ÉÈÊ]", "E");
		temp = temp.replaceAll("[ÍÌÎ]", "I");
		temp = temp.replaceAll("[ÕÔÓÒ]", "O");
		temp = temp.replaceAll("[ÛÚÙÜ]", "U");
		temp = temp.replaceAll("[Ç]", "C");
		temp = temp.replaceAll("[ãâáà]", "a");
		temp = temp.replaceAll("[éèê]", "e");
		temp = temp.replaceAll("[íìî]", "i");
		temp = temp.replaceAll("[õôóò]", "o");
		temp = temp.replaceAll("[ûúùü]", "u");
		temp = temp.replaceAll("[ç]", "c");
		return temp;
	}

	public static String removePontuacao(String s) {
		if (s == null)
			return null;
		return s.replace("-", "").replace(".", "").replace("/", "");
	}

	private static final DateTimeFormatter dtfMNI = DateTimeFormat.forPattern("yyyyMMddHHmmss");

	public static String formatarApoloDataHoraMinuto(Date d) {
		DateTime dt = new DateTime(d.getTime());
		return dt.toString(dtfMNI);
	}

	public static Date parsearApoloDataHoraMinuto(String s) {
		if (s == null)
			return null;
		DateTime dt = DateTime.parse(s, dtfMNI);
		return dt.toDate();
	}

	private static final DateTimeFormatter dtfBRHHMM = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");

	public static String formatarDataHoraMinuto(Date d) {
		DateTime dt = new DateTime(d.getTime());
		return dt.toString(dtfBRHHMM);
	}

	public static Date parsearDataHoraMinuto(String s) {
		if (s == null)
			return null;
		DateTime dt = DateTime.parse(s, dtfBRHHMM);
		return dt.toDate();
	}

	private static final DateTimeFormatter dtfBRHHMMSS = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");

	public static String formatarDataHoraMinutoSegundo(Date d) {
		DateTime dt = new DateTime(d.getTime());
		return dt.toString(dtfBRHHMMSS);
	}

	public static Date parsearDataHoraMinutoSegundo(String s) {
		if (s == null)
			return null;
		DateTime dt = DateTime.parse(s, dtfBRHHMMSS);
		return dt.toDate();
	}

	private static final DateTimeFormatter dtfBR = DateTimeFormat.forPattern("dd/MM/yyyy");

	public static String formatarData(Date d) {
		DateTime dt = new DateTime(d.getTime());
		return dt.toString(dtfBR);

	}

	public static Date parsearData(String s) {
		if (s == null)
			return null;
		DateTime dt = DateTime.parse(s, dtfBR);
		return dt.toDate();
	}

	private static final DateTimeFormatter dtfJPHHMMSS = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

	public static Date parsearDataHoraFormatoJapones(String s) {
		if (s == null)
			return null;
		DateTime dt = DateTime.parse(s, dtfJPHHMMSS);
		return dt.toDate();
	}

	public static String formatarNumeroProcesso(String numProc) {
		String numProcFormated = numProc;
		try {
			numProcFormated = numProc.replaceAll("^(\\d{7})-?(\\d{2})\\.?(\\d{4})\\.?(4)\\.?(02)\\.?(\\d{4})(\\d{2})?",
					"$1-$2.$3.$4.$5.$6$7");
		} catch (Exception ex) {
		}
		return numProcFormated;
	}

	public static Date parsearDataHoraFormatoJS(String s) {
		if (s == null)
			return null;
		s = s.replace("T", " ");
		DateTime dt = DateTime.parse(s, dtfJPHHMMSS);
		return dt.toDate();
	}

	public static byte[] calcSha1(byte[] content) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		md.reset();
		md.update(content);
		byte[] output = md.digest();
		return output;
	}

	final private static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {

		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static String makeSecret(String s) {
		if (s == null || s.length() == 0)
			return null;
		byte[] bytes = s.getBytes();
		return bytesToHex(calcSha1(bytes));
	}

	public static double parsearValor(String s) {
		if (s == null)
			return 0;
		s = s.trim();
		if (s.length() == 0)
			return 0;
		s = s.replace(".", "").replace(",", ".");
		return Double.parseDouble(s);
	}

	public static String calcularTempoRelativo(Date anterior) {
		PrettyTime p = new PrettyTime(new Date(), new Locale("pt"));

		String tempo = p.format(anterior);
		tempo = tempo.replace(" atrás", "");
		tempo = tempo.replace(" dias", " dias");
		tempo = tempo.replace(" horas", "h");
		tempo = tempo.replace(" minutos", "min");
		tempo = tempo.replace(" segundos", "s");
		tempo = tempo.replace("agora há pouco", "agora");
		return tempo;
	}

	public static void assertAcesso(final ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
		if (!Ex.getInstance().getComp().podeAcessarDocumento(titular, lotaTitular, mob)) {
			String s = "";
			s += mob.doc().getListaDeAcessosString();
			s = "(" + s + ")";
			s = " " + mob.doc().getExNivelAcessoAtual().getNmNivelAcesso() + " " + s;

			Map<ExPapel, List<Object>> mapa = mob.doc().getPerfis();
			boolean isInteressado = false;

			for (ExPapel exPapel : mapa.keySet()) {
				Iterator<Object> it = mapa.get(exPapel).iterator();

				if ((exPapel != null) && (exPapel.getIdPapel() == ExPapel.PAPEL_INTERESSADO)) {
					while (it.hasNext() && !isInteressado) {
						Object item = it.next();
						isInteressado = item.toString().equals(titular.getSigla()) ? true : false;
					}
				}

			}

			if (mob.doc().isSemEfeito()) {
				if (!mob.doc().getCadastrante().equals(titular) && !mob.doc().getSubscritor().equals(titular)
						&& !isInteressado) {
					throw new AplicacaoException("Documento " + mob.getSigla() + " cancelado ");
				}
			} else {
				throw new AplicacaoException("Documento " + mob.getSigla() + " inacessível ao usuário "
						+ titular.getSigla() + "/" + lotaTitular.getSiglaCompleta() + "." + s);
			}
		}
	}
	
	

}
