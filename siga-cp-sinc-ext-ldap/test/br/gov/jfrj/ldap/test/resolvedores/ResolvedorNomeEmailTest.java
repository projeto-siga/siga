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
package br.gov.jfrj.ldap.test.resolvedores;

import junit.framework.TestCase;

import org.junit.Test;

import br.gov.jfrj.ldap.sinc.resolvedores.ResolvedorNomeEmail;


public class ResolvedorNomeEmailTest extends TestCase{
	
	ResolvedorNomeEmail resolvedor = new ResolvedorNomeEmail();
	
	@Test
	public void testResolverNomeSimples(){
		resolvedor.setNome("Marcio Paiva Martins");
		
		assertEquals("marcio.martins", resolvedor.getNomesResolvidos()[0]);
		assertEquals("marcio.paiva", resolvedor.getNomesResolvidos()[1]);
		assertEquals("marcio", resolvedor.getNomesResolvidos()[2]);
		assertEquals("martins", resolvedor.getNomesResolvidos()[3]);
		assertEquals("paiva", resolvedor.getNomesResolvidos()[4]);
	}

	@Test
	public void testResolverNomeComposto(){
		resolvedor.setNome("Marco Antonio Vargas Ribeiro");
		
		assertEquals("marco.ribeiro", resolvedor.getNomesResolvidos()[0]);
		assertEquals("marco.vargas", resolvedor.getNomesResolvidos()[1]);
		assertEquals("marco", resolvedor.getNomesResolvidos()[2]);
		assertEquals("ribeiro", resolvedor.getNomesResolvidos()[3]);
		assertEquals("vargas", resolvedor.getNomesResolvidos()[4]);
	}

	@Test
	public void testResolverNomePequeno(){
		resolvedor.setNome("João da Silva");
		
		assertEquals("joao.silva", resolvedor.getNomesResolvidos()[0]);
		assertEquals("joao.silva", resolvedor.getNomesResolvidos()[1]);
		assertEquals("joao", resolvedor.getNomesResolvidos()[2]);
		assertEquals("silva", resolvedor.getNomesResolvidos()[3]);
		assertEquals("silva", resolvedor.getNomesResolvidos()[4]);
	}
	
}
