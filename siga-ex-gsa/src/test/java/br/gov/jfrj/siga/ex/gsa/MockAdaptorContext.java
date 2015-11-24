/*******************************************************************************
 * Copyright (c) 2006 - 2015 SJRJ.
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
package br.gov.jfrj.siga.ex.gsa;
import com.google.enterprise.adaptor.AdaptorContext;
import com.google.enterprise.adaptor.AsyncDocIdPusher;
import com.google.enterprise.adaptor.AuthnAuthority;
import com.google.enterprise.adaptor.AuthzAuthority;
import com.google.enterprise.adaptor.Config;
import com.google.enterprise.adaptor.DocIdEncoder;
import com.google.enterprise.adaptor.DocIdPusher;
import com.google.enterprise.adaptor.ExceptionHandler;
import com.google.enterprise.adaptor.PollingIncrementalLister;
import com.google.enterprise.adaptor.SensitiveValueDecoder;
import com.google.enterprise.adaptor.Session;
import com.google.enterprise.adaptor.StatusSource;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class MockAdaptorContext implements AdaptorContext {

	public void addStatusSource(StatusSource arg0) {
		// TODO Auto-generated method stub

	}

	public HttpContext createHttpContext(String arg0, HttpHandler arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public AsyncDocIdPusher getAsyncDocIdPusher() {
		// TODO Auto-generated method stub
		return null;
	}

	public Config getConfig() {
		Config config = new Config();
		config.addKey("servidor", "desenv");
		return config;
	}

	public DocIdEncoder getDocIdEncoder() {
		// TODO Auto-generated method stub
		return null;
	}

	public DocIdPusher getDocIdPusher() {
		// TODO Auto-generated method stub
		return null;
	}

	public ExceptionHandler getGetDocIdsFullErrorHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	public ExceptionHandler getGetDocIdsIncrementalErrorHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	public SensitiveValueDecoder getSensitiveValueDecoder() {
		// TODO Auto-generated method stub
		return null;
	}

	public Session getUserSession(HttpExchange arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setAuthnAuthority(AuthnAuthority arg0) {
		// TODO Auto-generated method stub

	}

	public void setAuthzAuthority(AuthzAuthority arg0) {
		// TODO Auto-generated method stub

	}

	public void setGetDocIdsFullErrorHandler(ExceptionHandler arg0) {
		// TODO Auto-generated method stub

	}

	public void setGetDocIdsIncrementalErrorHandler(ExceptionHandler arg0) {
		// TODO Auto-generated method stub

	}

	public void setPollingIncrementalLister(PollingIncrementalLister arg0) {
		// TODO Auto-generated method stub

	}

}
