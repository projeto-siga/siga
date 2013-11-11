package br.gov.jfrj.siga.libs.webwork;

/*
 * Copyright (c) 2002-2006 by OpenSymphony
 * All rights reserved.
 */

import com.opensymphony.webwork.dispatcher.WebWorkResultSupport;
import com.opensymphony.xwork.ActionInvocation;

public class DoNothingResult extends WebWorkResultSupport {

	protected void doExecute(String finalLocation, ActionInvocation invocation)
			throws Exception {
	}

}