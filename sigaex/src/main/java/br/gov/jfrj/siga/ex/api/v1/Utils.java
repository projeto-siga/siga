package br.gov.jfrj.siga.ex.api.v1;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.ocpsoft.prettytime.PrettyTime;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.bl.Ex;

class Utils {

	static String getUsuariosRestritos() {
		try {
			return Prop.get("username.restriction");
		} catch (Exception e) {
			throw new RuntimeException("Erro de configuração", e);
		}

	}

	static String getJwtIssuer() {
		return "jwt.issuer";
	}

	static String getJwtSecret() {
		return Prop.get("/siga.jwt.secret");
	}

	/**
	 * Verifica se uma string é composta somente por caracteres numéricos.
	 * 
	 * @param str String a verificar se é numérico
	 * @return
	 *         <ul>
	 *         <li><b>true</b> se for totalmente numérico</li>
	 *         <li><b>false</b> se houver qualquer caracter não numérico.</li>
	 *         </ul>
	 */
	static boolean isNumerico(String str) {
		return str.matches("^\\d+$");
	}

	/**
	 * Converte string em UTF-8 para ISO-8859-1
	 * 
	 * @param str String a ser convertida.
	 * @return String convertida.
	 */
	static String UTF8toISO(String str) {
		Charset utf8charset = Charset.forName("UTF-8");
		Charset iso88591charset = Charset.forName("ISO-8859-1");
		ByteBuffer inputBuffer = ByteBuffer.wrap(str.getBytes());
		CharBuffer data = utf8charset.decode(inputBuffer);
		ByteBuffer outputBuffer = iso88591charset.encode(data);
		byte[] outputData = outputBuffer.array();
		return new String(outputData);
	}

	static String calcularTempoRelativo(Date anterior) {
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

	static void assertAcesso(final ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
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
