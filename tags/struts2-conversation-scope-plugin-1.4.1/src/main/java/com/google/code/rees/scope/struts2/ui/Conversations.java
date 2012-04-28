package com.google.code.rees.scope.struts2.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.UIBean;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * A custom {@link UIBean} that places the conversation IDs as hidden fields
 * in a page
 * 
 * @see {@link ConversationTag}
 * 
 * @author rees.byars
 */
public class Conversations extends UIBean {

    public Conversations(ValueStack stack, HttpServletRequest request,
            HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "conversations";
    }

}