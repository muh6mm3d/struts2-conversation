package com.google.code.struts2.scope.conversation;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.struts2.scope.util.ScopeUtil;

public class DefaultConversationManager implements ConversationManager, ConversationPostProcessor {
	
	private static final Logger LOG = LoggerFactory.getLogger(DefaultConversationManager.class);
	public static final String ACTIVE_CONVERSATIONS_REQUEST_ATTRIBUTE_KEY = "byars.active.conversations.request.attribute.key";
	
	protected Map<Class<?>, Collection<ConversationConfig>> conversationConfigs;
	protected ConversationConfigBuilder configBuilder;
	
	public void setConversationConfigBuilder(ConversationConfigBuilder configBuilder) {
		this.configBuilder = configBuilder;
		conversationConfigs = configBuilder.getConversationConfigs();
	}

	@Override
	public void processConversations(ConversationAdapter conversationAdapter) {
		Object action = conversationAdapter.getAction();
		Collection<ConversationConfig> actionConversationConfigs = this.conversationConfigs.get(action.getClass());
		if (actionConversationConfigs != null) {
			for (ConversationConfig conversationConfig : actionConversationConfigs) {
				processConversation(conversationConfig, conversationAdapter, action);
			}
		}
	}
	
	protected void processConversation(ConversationConfig conversationConfig, ConversationAdapter conversationAdapter, Object action) {
		
		String actionId = conversationAdapter.getActionId();
		Map<String, Object> sessionContext = conversationAdapter.getSessionContext();
		String conversationName = conversationConfig.getConversationName();
		String conversationId = (String) conversationAdapter.getRequest().getParameter(conversationName);
		
		if (conversationId != null) {
			
			if (conversationConfig.containsAction(actionId)) {
				
				@SuppressWarnings("unchecked")
				Map<String, Object> conversationContext = (Map<String, Object>) sessionContext.get(conversationId);

				if (conversationContext != null) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("In Conversation " + conversationName + ".  Setting Conversation Field values for method "
								+ actionId + " of class " + action.getClass());
					}
					Map<String, Field> actionConversationFields = conversationConfig.getFields();
					if (actionConversationFields != null) {
						ScopeUtil.setFieldValues(action, actionConversationFields, conversationContext);
					}
				}
				
				if (conversationConfig.isEndAction(actionId)) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("In Conversation " + conversationName + ", removing conversation map from session following conversation end.");
					}
					sessionContext.remove(conversationId);
				} else {
					conversationAdapter.dispatchPostProcessor(this, conversationConfig, conversationId);
				}
			}
		} else if (conversationConfig.isBeginAction(actionId)) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Beginning new " + conversationName + " conversation.");
			}
			conversationId = java.util.UUID.randomUUID().toString();
			conversationAdapter.dispatchPostProcessor(this, conversationConfig, conversationId);
		}
	}

	@Override
	public void injectConversationFields(ConversationAdapter conversationAdapter) {
		Object action = conversationAdapter.getAction();
		Class<?> actionClass = action.getClass();
		synchronized (conversationConfigs) {
			this.configBuilder.addClassConfig(actionClass);
			this.conversationConfigs = this.configBuilder.getConversationConfigs();
		}
		Collection<ConversationConfig> actionConversationConfigs = this.conversationConfigs.get(action.getClass());
		if (actionConversationConfigs != null) {
			Map<String, Object> session = conversationAdapter.getSessionContext();
			for (ConversationConfig conversation : actionConversationConfigs) {
				String conversationId = conversationAdapter.getRequest().getParameter(conversation.getConversationName());
				@SuppressWarnings("unchecked")
				Map<String, Object> conversationContext = (Map<String, Object>) session.get(conversationId);
				if (conversationContext != null) {
					Map<String, Field> actionConversationFields = conversation.getFields();
					if (actionConversationFields != null) {
						ScopeUtil.setFieldValues(action, actionConversationFields, conversationContext);
					}
				}
			}
		}
	}

	@Override
	public void extractConversationFields(ConversationAdapter conversationAdapter) {
		Object action = conversationAdapter.getAction();
		Class<?> actionClass = action.getClass();
		synchronized (conversationConfigs) {
			this.configBuilder.addClassConfig(actionClass);
			this.conversationConfigs = this.configBuilder.getConversationConfigs();
		}
		Collection<ConversationConfig> actionConversationConfigs = this.conversationConfigs.get(action.getClass());
		if (actionConversationConfigs != null) {
			for (ConversationConfig conversation : actionConversationConfigs) {
				
				Map<String, Field> actionConversationFields = conversation.getFields();
				String conversationId = null;
				String conversationName = conversation.getConversationName();
				HttpServletRequest request = conversationAdapter.getRequest();
				
				if (request != null) {
					conversationId = (String) request.getParameter(conversationName);
				}
				
				if (conversationId == null) {
					conversationId = java.util.UUID.randomUUID().toString();
				}
				
				if (actionConversationFields != null) {
					
					Map<String, Object> session = conversationAdapter.getSessionContext();
					Map<String, Object> conversationContext = ScopeUtil.getFieldValues(action,
							actionConversationFields);
					
					session.put(conversationId, conversationContext);
				}
				
				conversationAdapter.addConversation(conversationName, conversationId);
			}
		}
	}

	@Override
	public void postProcessConversation(
			ConversationAdapter conversationAdapter,
			ConversationConfig conversationConfig, String conversationId) {
		
		Object action = conversationAdapter.getAction();
		
		Map<String, Field> actionConversationFields = conversationConfig.getFields();
		
		if (actionConversationFields != null) {
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("Getting conversation fields for Conversation " + conversationConfig.getConversationName() 
						+ " following execution of action " + conversationAdapter.getActionId());
			}
			
			Map<String, Object> session = conversationAdapter.getSessionContext();
			@SuppressWarnings("unchecked")
			Map<String, Object> conversationContext = (Map<String, Object>) session.get(conversationId);
			if (conversationContext == null) {
				conversationContext = new HashMap<String, Object>();
			}
			conversationContext.putAll(ScopeUtil.getFieldValues(action,actionConversationFields));
			
			session.put(conversationId, conversationContext);
		}
		
		conversationAdapter.addConversation(conversationConfig.getConversationName(), conversationId);
	}

}