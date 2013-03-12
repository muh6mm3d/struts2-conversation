package com.github.overengineer.scope.conversation.expression.configuration;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.overengineer.scope.conversation.configuration.ConversationArbitrator;
import com.github.overengineer.scope.conversation.expression.annotations.Eval;

public class DefaultExpressionConfigurationProvider implements ExpressionConfigurationProvider {
	
	private static final Logger LOG = LoggerFactory.getLogger(DefaultExpressionConfigurationProvider.class);
	
	protected ConversationArbitrator arbitrator;
	protected ConcurrentMap<Class<?>, ExpressionConfiguration> expressionConfigurations = new ConcurrentHashMap<Class<?>, ExpressionConfiguration>();
	
	/**
     * {@inheritDoc}
     */
    @Override
    public void setArbitrator(ConversationArbitrator arbitrator) {
        this.arbitrator = arbitrator;
    }
	
	/**
     * {@inheritDoc}
     */
	@Override
	public ExpressionConfiguration getExpressionConfiguration(Class<?> actionClass) {
		return this.expressionConfigurations.get(actionClass);
	}
	
	/**
     * {@inheritDoc}
     */
    @Override
    public void init(Set<Class<?>> actionClasses) {
    	if (this.expressionConfigurations.size() != actionClasses.size()) { //in case it's already been called
    		LOG.info("Building Expression Configurations...");
        	if (this.arbitrator == null) {
        		LOG.error("No ConversationArbitrator set for the ConversationConfigurationProvider, review configuration files to make sure an arbitrator is declared.");
        	}
            for (Class<?> clazz : actionClasses) {
                processClass(clazz, expressionConfigurations);
            }
            LOG.info("...building of Conversation Configurations successfully completed.");
    	}
    }
	
    protected void processClass(Class<?> clazz, Map<Class<?>, ExpressionConfiguration> expressionConfigurations) {
    	
    	ExpressionConfiguration expressionConfiguration = new ExpressionConfigurationImpl();
    	
    	for (Method method : this.arbitrator.getCandidateConversationMethods(clazz)) {
        	
        	String methodName = this.arbitrator.getName(method);
        	
        	this.resolveExpressionConfig(clazz, method, methodName, expressionConfiguration);
        	
    	}
    	
    	expressionConfigurations.put(clazz, expressionConfiguration);
    	
    }
    
	protected void resolveExpressionConfig(Class<?> clazz, Method method, String methodName, ExpressionConfiguration expressionConfiguration) {
		if (method.isAnnotationPresent(Eval.class)) {
			Eval eval = method.getAnnotation(Eval.class);
			expressionConfiguration.addExpressions(methodName, eval.preAction(), eval.postAction(), eval.postView());
		} else if (clazz.isAnnotationPresent(Eval.class)) {
			Eval eval = clazz.getAnnotation(Eval.class);
			expressionConfiguration.addExpressions(methodName, eval.preAction(), eval.postAction(), eval.postView());
		} else {
			expressionConfiguration.addExpressions(methodName, "", "", "");
		}
	}
	

}
