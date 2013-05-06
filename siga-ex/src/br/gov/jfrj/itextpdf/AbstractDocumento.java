/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.itextpdf;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.gov.jfrj.siga.acesso.ConheceUsuarioSupport;
import br.gov.jfrj.siga.acesso.UsuarioAutenticado;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;

/**
 * Hello World example as a Servlet.
 * 
 * @author blowagie
 */
public abstract class AbstractDocumento extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -600880073954336881L;

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(AbstractDocumento.class);

	private ExMobil getMobil(String requestURI) throws SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, Exception {
		ExMobil mob = null;
		String sigla = "";

		// final Pattern p = Pattern.compile("/([0-9A-Z\\-\\/]+)\\.pdf");
		final Pattern p = Pattern
				.compile("/([0-9A-Z\\-\\/\\.]+)(:?[0-9]*)\\.(pdf|html)");

		final Matcher m = p.matcher(requestURI);
		if (m.find()) {
			sigla = m.group(1);
			final ExMobilDaoFiltro flt = new ExMobilDaoFiltro();
			flt.setSigla(sigla);
			mob = (ExMobil) ExDao.getInstance().consultarPorSigla(flt);
		}
		// expDAO.consultarConteudoBlob(docvia.getExDocumento());
		return mob;
	}

	private ExMovimentacao getMov(ExMobil mob, String requestURI)
			throws AplicacaoException, SQLException {
		String sMovId = null;
		ExMovimentacao mov = null;

		final Pattern p = Pattern
				.compile("/([0-9A-Z\\-\\/\\.]+):?([0-9]*)\\.(pdf|html)");
		final Matcher m = p.matcher(requestURI);
		if (m.find()) {
			sMovId = m.group(2);
			if (sMovId.length() == 0)
				return null;
			for (ExMovimentacao movAux : mob.getExMovimentacaoSet()) {
				if (movAux.getIdMov() == Long.parseLong(sMovId))
					mov = movAux;
			}
		}
		if (mov == null)
			return null;
		mov = ExDao.getInstance().consultar(mov.getIdMov(),
				ExMovimentacao.class, false);
		return mov;
	}

	@Override
	public void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException,
			ServletException {
		try {
			
			//log.info("Iniciando servlet de documentos...");
			
			@SuppressWarnings("unused")
			ExDao dao = ExDao.getInstance();

			ExMobil mob = getMobil(request.getRequestURI());
			if (mob == null) {
				throw new AplicacaoException(
						"A sigla informada não corresponde a um documento da base de dados.");
			}

			ConheceUsuarioSupport cusr = new ConheceUsuarioSupport();
			try {
				UsuarioAutenticado.carregarUsuarioAutenticadoRequest(request,
						cusr);
			} catch (SQLException e) {
				throw new AplicacaoException(
						"Não foi possível carregar o perfil do usuário"
								+ e.toString());
			} catch (final NullPointerException e) {
				throw new AplicacaoException(
						"Não foi possível carregar usuário" + e.toString());
			}

			if (!Ex.getInstance()
					.getComp()
					.podeAcessarDocumento(cusr.getTitular(),
							cusr.getLotaTitular(), mob)) {
				throw new AplicacaoException("Documento " + mob.getSigla()
						+ " inacessível ao usuário "
						+ cusr.getTitular().getSigla() + "/"
						+ cusr.getLotaTitular().getSiglaCompleta() + ".");
			}

			ExMovimentacao mov = getMov(mob, request.getRequestURI());

			// String sHtml = getDocHTML(docvia, request.getSession().getId());
			// final byte pdf[] = generatePdf(sHtml);

			final byte ab[] = getDocumento(mob, mov, request);

			response.reset();
			// Se o resultado for muito pequeno, entao trata-se de um hash
			String filename;
			if (mov != null) {
				filename = mov.getReferencia();
			} else {
				filename = mob.getCodigoCompacto();
			}
			
			//log.info("Acessando documento " + filename);
			
			if (ab.length <= 64) {
				response.setHeader("Content-Disposition",
						"attachment; filename=" + filename + ".hash");
				response.setContentType("application/octet-stream");
			} else {
				response.setHeader("Content-Disposition", "filename="
						+ filename + ".pdf");
				response.setContentType(getContentType());
			}
			response.setContentLength(ab.length);
			if (getCharacterEncoding() != null)
				response.setCharacterEncoding(getCharacterEncoding());
			response.getOutputStream().write(ab);
			response.getOutputStream().flush();
		} catch (Exception e) {
			throw new ServletException("erro na geração do documento.", e);
		}

		// Paginas pg = new Paginas(request, response, new Integer(num));
		// pg.print();
		// document.close();
	}

	public abstract String getContentType();

	public String getCharacterEncoding() {
		return null;
	}

	protected abstract byte[] getDocumento(ExMobil mob, ExMovimentacao mov,
			HttpServletRequest request) throws Exception;

	@Override
	protected void doPost(final HttpServletRequest arg0,
			final HttpServletResponse arg1) throws ServletException,
			IOException {
		doGet(arg0, arg1);
	}

}
