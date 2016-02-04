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

package br.gov.jfrj.siga.wf.tag;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.xpath.DefaultXPath;
import org.jbpm.JbpmContext;
import org.jbpm.file.def.FileDefinition;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.Token;
import org.jbpm.taskmgmt.exe.TaskInstance;

import br.gov.jfrj.siga.wf.util.WfContextBuilder;

/**
 * Classe que define a tag de exibição do gráfico do process definition através da tag wfImage.
 * @author kpf
 *
 */
public class ProcessImageTag extends TagSupport
{

	private static final long serialVersionUID = 1L;
	private long taskInstanceId = - 1;
	private long tokenInstanceId = - 1;

	private byte [] gpdBytes = null;
	private byte [] imageBytes = null;
	private Token currentToken = null;
	private ProcessDefinition processDefinition = null;

	static String currentTokenColor = "red";
	static String childTokenColor = "blue";
	static String tokenNameColor = "blue";
	static String actorIDColor = "green";

	/**
	 * Libera os recursos quando a tag passar para release state.
	 */
	public void release ()
	{
		taskInstanceId = - 1;
		gpdBytes = null;
		imageBytes = null;
		currentToken = null;
	}

	/**
	 * Processa a tag.
	 */
	public int doEndTag () throws JspException
	{
		try
		{
			initialize ();
			retrieveByteArrays ();
			if ( gpdBytes != null && imageBytes != null )
			{
				writeTable ();
			}
		}
		catch ( IOException e )
		{
			e.printStackTrace ();
			throw new JspException ( "table couldn't be displayed" , e );
		}
		catch ( DocumentException e )
		{
			e.printStackTrace ();
			throw new JspException ( "table couldn't be displayed" , e );
		}
		release ();
		return EVAL_PAGE;
	}

	/**
	 * Lê as informações referentes ao gráfico da definição do processo.
	 */
	private void retrieveByteArrays ()
	{
		try
		{
			FileDefinition fileDefinition = processDefinition.getFileDefinition ();
			gpdBytes = fileDefinition.getBytes ( "gpd.xml" );
			imageBytes = fileDefinition.getBytes ( "processimage.jpg" );
		}
		catch ( Exception e )
		{
			e.printStackTrace ();
		}
	}

	/**
	 * Desenha um quadro sobre a tarefa atual.
	 * @throws IOException
	 * @throws DocumentException
	 */
	private void writeTable () throws IOException , DocumentException
	{

		int borderWidth = 4;
		Element rootDiagramElement = DocumentHelper.parseText ( new String ( gpdBytes ) ).getRootElement ();
		int [] boxConstraint;
		int [] imageDimension = extractImageDimension ( rootDiagramElement );
		String imageLink = "loadPhoto?pdId=" + processDefinition.getId ();
		JspWriter jspOut = pageContext.getOut ();

		if ( tokenInstanceId > 0 )
		{

			List allTokens = new ArrayList ();
			walkTokens ( currentToken , allTokens );

			jspOut.println ( "<div style='position:relative; background-image:url(" + imageLink + "); width: " + imageDimension [ 0 ] + "px; height: " + imageDimension [ 1 ] + "px;'>" );

			for ( int i = 0 ; i < allTokens.size () ; i++ )
			{
				Token token = ( Token ) allTokens.get ( i );

				// check how many tokens are on teh same level (= having the
				// same parent)
				int offset = i;
				if ( i > 0 )
				{
					while ( offset > 0 && ( ( Token ) allTokens.get ( offset - 1 ) ).getParent ().equals ( token.getParent () ) )
					{
						offset--;
					}
				}
				boxConstraint = extractBoxConstraint ( rootDiagramElement , token );

				// Adjust for borders
				// boxConstraint[2]-=borderWidth*2;
				// boxConstraint[3]-=borderWidth*2;

				jspOut.println ( "<div style='position:absolute; left: " + boxConstraint [ 0 ] + "px; top: " + boxConstraint [ 1 ] + "px; " );

				if ( i == ( allTokens.size () - 1 ) )
				{
					jspOut.println ( "border: " + currentTokenColor );
				}
				else
				{
					jspOut.println ( "border: " + childTokenColor );
				}

				jspOut.println ( " " + borderWidth + "px groove; " + "width: " + boxConstraint [ 2 ] + "px; height: " + boxConstraint [ 3 ] + "px;'>" );

				if ( token.getName () != null )
				{
					jspOut.println ( "<span style='color:" + tokenNameColor + ";font-style:italic;position:absolute;left:" + ( boxConstraint [ 2 ] + 10 ) + "px;top:" + ( ( i - offset ) * 20 ) + ";'>&nbsp;" + token.getName () + "</span>" );
				}

				jspOut.println ( "<span style='color:" + actorIDColor + ";font-style:italic;position:absolute;left:" + ( boxConstraint [ 2 ] + 10 ) + "px;top:-30;'>&nbsp;" + WfContextBuilder.getJbpmContext().getJbpmContext().getTaskInstance(taskInstanceId).getActorId() + "</span>" );
				jspOut.println ( "</div>" );
			}
			jspOut.println ( "</div>" );
		}
		else
		{
			boxConstraint = extractBoxConstraint ( rootDiagramElement );

			jspOut.println ( "<table border=0 cellspacing=0 cellpadding=0 width=" + imageDimension [ 0 ] + " height=" + imageDimension [ 1 ] + ">" );
			jspOut.println ( "  <tr>" );
			jspOut.println ( "    <td width=" + imageDimension [ 0 ] + " height=" + imageDimension [ 1 ] + " style=\"background-image:url(" + imageLink + ")\" valign=top>" );
			jspOut.println ( "      <table border=0 cellspacing=0 cellpadding=0>" );
			jspOut.println ( "        <tr>" );
			jspOut.println ( "          <td width=" + ( boxConstraint [ 0 ] - borderWidth ) + " height=" + ( boxConstraint [ 1 ] - borderWidth ) + " style=\"background-color:transparent;\"></td>" );
			jspOut.println ( "        </tr>" );
			jspOut.println ( "        <tr>" );
			jspOut.println ( "          <td style=\"background-color:transparent;\"></td>" );
			jspOut.println ( "          <td style=\"border-color:" + currentTokenColor + "; border-width:" + borderWidth + "px; border-style:groove; background-color:transparent;\" width=" + boxConstraint [ 2 ] + " height=" + ( boxConstraint [ 3 ] + ( 2 * borderWidth ) ) + ">&nbsp;</td>" );
			jspOut.println ( "        </tr>" );
			jspOut.println ( "      </table>" );
			jspOut.println ( "    </td>" );
			jspOut.println ( "  </tr>" );
			jspOut.println ( "</table>" );
		}
	}

	/**
	 * Extrai os limites da caixa para que seja desenhada a marcação à sua volta.
	 * @param root
	 * @return
	 */
	private int [] extractBoxConstraint ( Element root )
	{
		int [] result = new int [ 4 ];
		String nodeName = currentToken.getNode ().getName ();
		XPath xPath = new DefaultXPath ( "//node[@name='" + nodeName + "']" );
		Element node = ( Element ) xPath.selectSingleNode ( root );
		result [ 0 ] = Integer.valueOf ( node.attribute ( "x" ).getValue () ).intValue ();
		result [ 1 ] = Integer.valueOf ( node.attribute ( "y" ).getValue () ).intValue ();
		result [ 2 ] = Integer.valueOf ( node.attribute ( "width" ).getValue () ).intValue ();
		result [ 3 ] = Integer.valueOf ( node.attribute ( "height" ).getValue () ).intValue ();
		return result;
	}

	/**
	 * Extrai os limites da caixa para que seja desenhada a marcação à sua volta.
	 * @param root
	 * @param token
	 * @return
	 */
	private int [] extractBoxConstraint ( Element root , Token token )
	{
		
		int [] result = new int [ 4 ];
		String nodeName = "";
		try {
			nodeName = new String(token.getNode ().getName ().getBytes("ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		XPath xPath = new DefaultXPath ( "//node[@name='" + nodeName + "']" );
		Element node = ( Element ) xPath.selectSingleNode ( root );
		result [ 0 ] = Integer.valueOf ( node.attribute ( "x" ).getValue () ).intValue ();
		result [ 1 ] = Integer.valueOf ( node.attribute ( "y" ).getValue () ).intValue ();
		result [ 2 ] = Integer.valueOf ( node.attribute ( "width" ).getValue () ).intValue ();
		result [ 3 ] = Integer.valueOf ( node.attribute ( "height" ).getValue () ).intValue ();
		return result;
	}

	/**
	 * Extrai as dimensões da imagem.
	 * @param root
	 * @return
	 */
	private int [] extractImageDimension ( Element root )
	{
		int [] result = new int [ 2 ];
		result [ 0 ] = Integer.valueOf ( root.attribute ( "width" ).getValue () ).intValue ();
		result [ 1 ] = Integer.valueOf ( root.attribute ( "height" ).getValue () ).intValue ();
		return result;
	}
	
	/**
	 * Inicializa a tag
	 */
	private void initialize ()
	{
		JbpmContext jbpmContext = WfContextBuilder.getJbpmContext ().getJbpmContext ();
		if ( this.taskInstanceId > 0 )
		{
			TaskInstance taskInstance = jbpmContext.getTaskMgmtSession ().loadTaskInstance ( taskInstanceId );
			currentToken = taskInstance.getToken ();
		}
		else
		{
			if ( this.tokenInstanceId > 0 )
				currentToken = jbpmContext.getGraphSession ().loadToken ( this.tokenInstanceId );
		}
		processDefinition = currentToken.getProcessInstance ().getProcessDefinition ();
	}

	/**
	 * Pega todos os tokens.
	 * @param parent
	 * @param allTokens
	 */
	private void walkTokens ( Token parent , List allTokens )
	{
		Map children = parent.getChildren ();
		if ( children != null && children.size () > 0 )
		{
			Collection childTokens = children.values ();
			for ( Iterator iterator = childTokens.iterator () ; iterator.hasNext () ; )
			{
				Token child = ( Token ) iterator.next ();
				walkTokens ( child , allTokens );
			}
		}

		allTokens.add ( parent );
	}

	/**
	 * Define a task.
	 * @param id
	 */
	public void setTask ( long id )
	{
		this.taskInstanceId = id;
	}

	/**
	 * Define o token.
	 * @param id
	 */
	public void setToken ( long id )
	{
		this.tokenInstanceId = id;
	}

}
