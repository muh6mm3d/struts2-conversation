/*******************************************************************************
 * 
 *  Struts2-Conversation-Plugin - An Open Source Conversation- and Flow-Scope Solution for Struts2-based Applications
 *  =================================================================================================================
 * 
 *  Copyright (C) 2012 by Rees Byars
 *  http://code.google.com/p/struts2-conversation/
 * 
 * **********************************************************************************************************************
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *  the License. You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *  an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *  specific language governing permissions and limitations under the License.
 * 
 * **********************************************************************************************************************
 * 
 *  $Id: ConversationsTag.java reesbyars $
 ******************************************************************************/
package com.google.code.rees.scope.struts2.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ui.AbstractUITag;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * A custom {@link AbstractUITag} that uses the {@link Conversations} bean to
 * add the conversation IDs to a page
 * 
 * @author rees.byars
 * 
 */
public class ConversationsTag extends AbstractUITag {

	private static final long serialVersionUID = -2510111224558001809L;

	@Override
	public Component getBean(ValueStack paramValueStack, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse) {
		return new Conversations(paramValueStack, paramHttpServletRequest, paramHttpServletResponse);
	}

}
