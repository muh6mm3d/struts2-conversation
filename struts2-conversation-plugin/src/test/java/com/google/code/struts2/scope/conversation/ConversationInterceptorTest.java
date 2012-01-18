package com.google.code.struts2.scope.conversation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.google.code.struts2.scope.mocks.actions.conversation.MockConversationController;
import com.google.code.struts2.scope.mocks.beans.TestBean;
import com.google.code.struts2.scope.sessionfield.SessionField;
import com.google.code.struts2.scope.struts2.StrutsScopeConstants;
import com.google.code.struts2.scope.test.ScopeTestUtil;
import com.google.code.struts2.test.junit.StrutsConfiguration;
import com.google.code.struts2.test.junit.StrutsSpringTest;
import com.opensymphony.xwork2.inject.Inject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:*applicationContext.xml")
@StrutsConfiguration(locations = "struts-conversation.xml")
public class ConversationInterceptorTest extends
		StrutsSpringTest<MockConversationController> {

	static final String CONVERSATION_NAME = "oopy-conversation";
	static final String CONVERSATION_FIELD = "conversationString";

	@Inject(StrutsScopeConstants.MANAGER_KEY)
	ConversationManager manager;
	
	@ConversationField(conversations = "oopy")
	String conversationString;
	
	@SessionField
	String chubby;
	
	@Autowired
	TestBean bean;

	@Test
	public void testContinueRegistration() throws Exception {

		conversationString = "shit";
		
		this.getActionProxy("/conversation/begin").execute();
		System.out.println(this.getAction().getBean().getEcho());
		
		ScopeTestUtil.setConversationIdsOnRequest(request, MockConversationController.class);
		this.getActionProxy("/conversation/do1").execute();
		System.out.println(this.getAction().getBean().getEcho());
		
		ScopeTestUtil.setConversationIdsOnRequest(request, MockConversationController.class);
		this.getActionProxy("/conversation/do2").execute();
		System.out.println(this.getAction().getBean().getEcho());
		System.out.println(ConversationAdapter.getAdapter().getActionId());
		
		String id = ConversationUtil.getConversationId("oopy");
		
		
		this.getActionProxy("begin").execute();
		
		this.getActionProxy("/conversation/begin").execute();
		System.out.println(this.getAction().getBean().getEcho());
		
		request.addParameter("oopy-conversation", id);
		this.getActionProxy("/conversation/do2").execute();
		System.out.println(this.getAction().getBean().getEcho());
		
		request.addParameter("oopy-conversation", id);
		this.getActionProxy("/conversation/do2").execute();
		System.out.println(this.getAction().getBean().getEcho());
	}

	@Test
	public void testBeforeInvocation() throws Exception {

		this.getActionProxy("/conversation/begin").execute();
		String id1 = ConversationUtil.getConversationId(CONVERSATION_NAME);

		this.getActionProxy("/conversation/begin").execute();
		String id2 = ConversationUtil.getConversationId(CONVERSATION_NAME);

		request.addParameter(CONVERSATION_NAME, id1);
		request.addParameter(CONVERSATION_FIELD, "one");
		this.getActionProxy("/conversation/do1").execute();

		request.addParameter(CONVERSATION_NAME, id2);
		request.addParameter(CONVERSATION_FIELD, "two");
		this.getActionProxy("/conversation/do1").execute();

		request.addParameter(CONVERSATION_NAME, id1);
		this.getActionProxy("/conversation/do2").execute();
		assertEquals("one", this.getAction().getConversationString());

		request.addParameter(CONVERSATION_NAME, id2);
		this.getActionProxy("/conversation/do2").execute();
		assertEquals("two", this.getAction().getConversationString());

		request.addParameter(CONVERSATION_NAME, id1);
		this.getActionProxy("/conversation/end").execute();

		this.getActionProxy("/conversation/do2").execute();
		assertEquals("initialState", this.getAction().getConversationString());

	}

	@Test
	public void testCrossActionWorkflowFieldPersistence() throws Exception {
		// TODO
	}

	@Test
	public void testActionWithNoWorkflows() throws Exception {
		// TODO
	}

}