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
/*
 * JBoss, Home of Professional Open Source Copyright 2005, JBoss Inc., and individual contributors as indicated by the @authors tag. See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 * 
 * This is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this software; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package br.gov.jfrj.siga.wf.servlet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.xpath.DefaultXPath;
import org.jbpm.JbpmContext;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.Token;
import org.jbpm.taskmgmt.exe.TaskInstance;

import br.gov.jfrj.siga.wf.util.WfContextBuilder;

/**
 * Classe que gera uma imagem de exibição do process definition com marcações
 * nos tasks atuais.
 * 
 * @author kpf
 * 
 */
public class SingleImageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	static Color currentTokenColor = Color.red;
	static Color childTokenColor = Color.blue;
	static Color tokenNameColor = Color.blue;
	static Color actorIDColor = Color.green;

	/**
	 * Extrai os limites da caixa para que seja desenhada a marcação à sua
	 * volta.
	 * 
	 * @param root
	 * @return
	 */
	private int[] extractBoxConstraint(Token currentToken, Element root) {
		int[] result = new int[4];
		String nodeName = currentToken.getNode().getName();
		XPath xPath = new DefaultXPath("//node[@name='" + nodeName + "']");
		Element node = (Element) xPath.selectSingleNode(root);
		result[0] = Integer.valueOf(node.attribute("x").getValue()).intValue();
		result[1] = Integer.valueOf(node.attribute("y").getValue()).intValue();
		result[2] = Integer.valueOf(node.attribute("width").getValue())
				.intValue();
		result[3] = Integer.valueOf(node.attribute("height").getValue())
				.intValue();
		return result;
	}

	/**
	 * Extrai os limites da caixa para que seja desenhada a marcação à sua
	 * volta.
	 * 
	 * @param root
	 * @param token
	 * @return
	 */
	private int[] extractBoxConstraint(Element root, Token token) {

		int[] result = new int[4];
		String nodeName = "";
		try {
			nodeName = new String(token.getNode().getName()
					.getBytes("ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		XPath xPath = new DefaultXPath("//node[@name='" + nodeName + "']");
		Element node = (Element) xPath.selectSingleNode(root);
		result[0] = Integer.valueOf(node.attribute("x").getValue()).intValue();
		result[1] = Integer.valueOf(node.attribute("y").getValue()).intValue();
		result[2] = Integer.valueOf(node.attribute("width").getValue())
				.intValue();
		result[3] = Integer.valueOf(node.attribute("height").getValue())
				.intValue();
		return result;
	}

	/**
	 * Extrai as dimensões da imagem.
	 * 
	 * @param root
	 * @return
	 */
	private int[] extractImageDimension(Element root) {
		int[] result = new int[2];
		result[0] = Integer.valueOf(root.attribute("width").getValue())
				.intValue();
		result[1] = Integer.valueOf(root.attribute("height").getValue())
				.intValue();
		return result;
	}

	/**
	 * Pega todos os tokens.
	 * 
	 * @param parent
	 * @param allTokens
	 */
	private void walkTokens(Token parent, List allTokens) {
		Map children = parent.getChildren();
		if (children != null && children.size() > 0) {
			Collection childTokens = children.values();
			for (Iterator iterator = childTokens.iterator(); iterator.hasNext();) {
				Token child = (Token) iterator.next();
				walkTokens(child, allTokens);
			}
		}

		allTokens.add(parent);
	}

	/**
	 * Pega o desenho do process definition e coloca no output stream.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Token currentToken = null;

		Long pdId = null;
		ProcessDefinition pd = null;
		Long tiId = Long.parseLong(request.getParameter("tId"));
		TaskInstance ti = null;

		JbpmContext jbpmContext = WfContextBuilder.getJbpmContext()
				.getJbpmContext();

		if (tiId > 0) {
			ti = jbpmContext.getTaskMgmtSession().loadTaskInstance(tiId);
			currentToken = ti.getToken();
			pd = ti.getProcessInstance().getProcessDefinition();
			pdId = pd.getId();
		} else {
			pdId = Long.parseLong(request.getParameter("pdId"));
			pd = jbpmContext.getGraphSession().loadProcessDefinition(pdId);
		}

		byte[] gpdBytes = pd.getFileDefinition().getBytes("gpd.xml");
		byte[] imageBytes = pd.getFileDefinition().getBytes("processimage.jpg");
		if (gpdBytes == null && imageBytes == null) {
			throw new ServletException(
					"Impossível acessar a imagem ou as definições do processo.");
		}

/*		ByteArrayInputStream in = new ByteArrayInputStream(imageBytes);
		JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
		BufferedImage image = decoder.decodeAsBufferedImage();
		in.close();*/
		
		InputStream in = new ByteArrayInputStream(imageBytes);
		BufferedImage image = ImageIO.read(in);
		in.close();

		Graphics2D g2dImage = image.createGraphics();
		g2dImage.setPaint(Color.white);
		g2dImage.draw(new Rectangle2D.Double(0, 0, image.getWidth() - 1, image
				.getHeight() - 1));

		int borderWidth = 4;
		Element rootDiagramElement;
		try {
			rootDiagramElement = DocumentHelper.parseText(new String(gpdBytes))
					.getRootElement();
		} catch (DocumentException e) {
			throw new ServletException(e);
		}
		int[] boxConstraint;
		int[] imageDimension = extractImageDimension(rootDiagramElement);

		long tokenInstanceId = ti.getToken().getId();

		if (tokenInstanceId > 0) {

			List allTokens = new ArrayList();
			walkTokens(currentToken, allTokens);

			for (int i = 0; i < allTokens.size(); i++) {
				Token token = (Token) allTokens.get(i);

				// check how many tokens are on teh same level (= having the
				// same parent)
				int offset = i;
				if (i > 0) {
					while (offset > 0
							&& ((Token) allTokens.get(offset - 1)).getParent()
									.equals(token.getParent())) {
						offset--;
					}
				}
				boxConstraint = extractBoxConstraint(rootDiagramElement, token);
				if (i == (allTokens.size() - 1)) {
					g2dImage.setPaint(currentTokenColor);
				} else {
					g2dImage.setPaint(childTokenColor);
				}
				Stroke stroke = new BasicStroke(borderWidth);
				g2dImage.setStroke(stroke);
				g2dImage.draw(new Rectangle2D.Double(boxConstraint[0],
						boxConstraint[1], boxConstraint[2], boxConstraint[3]));

				// Adjust for borders
				// boxConstraint[2]-=borderWidth*2;
				// boxConstraint[3]-=borderWidth*2;

				// if (token.getName() != null) {
				// jspOut.println("<span style='color:" + tokenNameColor
				// + ";font-style:italic;position:absolute;left:"
				// + (boxConstraint[2] + 10) + "px;top:"
				// + ((i - offset) * 20) + ";'>&nbsp;"
				// + token.getName() + "</span>");
				// }

				// jspOut.println("<span style='color:"
				// + actorIDColor
				// + ";font-style:italic;position:absolute;left:"
				// + (boxConstraint[2] + 10)
				// + "px;top:-30;'>&nbsp;"
				// + WfContextBuilder.getJbpmContext().getJbpmContext()
				// .getTaskInstance(taskInstanceId).getActorId()
				// + "</span>");
			}
		}

/*		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JPEGImageEncoder jie = JPEGCodec.createJPEGEncoder(baos);
		jie.encode(image);
		byte[] bytes = baos.toByteArray();*/

		/*OutputStream out = response.getOutputStream();
		out.write(bytes);
		out.flush();*/
		
		OutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		out.flush();
	}

}
