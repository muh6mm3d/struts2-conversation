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
 *  $Id: ConversationContextManager.java reesbyars $
 ******************************************************************************/
package com.google.code.rees.scope.conversation.context;

import java.io.Serializable;

/**
 * Uses a {@link ConversationContextFactory} to manage the creation, caching,
 * retrieval, removal, and expiration of conversations
 * 
 * @author rees.byars
 * 
 */
public interface ConversationContextManager extends Serializable {

    /**
     * 5 minutes
     */
    public static final long DEFAULT_MONITOR_FREQUENCY = 300000;

    /**
     * 20
     */
    public static final int DEFAULT_MAXIMUM_NUMBER_OF_A_GIVEN_CONVERSATION = 20;

    /**
     * Set the frequency with which {@link ConversationContext
     * ConversationContexts} will
     * be monitored for potential removal
     * 
     * @param frequency
     */
    public void setMonitoringFrequency(long frequency);

    /**
     * Set the max number of cached {@link ConversationContext
     * ConversationContexts}.
     * 
     * @param maxInstances
     */
    public void setMaxInstances(int maxInstances);

    /**
     * Set the {@link ConversationContextFactory} to be used by this manager
     * 
     * @param contextFactory
     */
    public void setContextFactory(ConversationContextFactory contextFactory);

    /**
     * Retrieve the context identified by the given information, creating
     * a new one if none exists.
     * 
     * @param conversationName
     * @param conversationId
     * @return
     */
    public ConversationContext getContext(String conversationName,
            String conversationId);

    /**
     * Remove the context identified by the given information, returning the
     * context
     * being removed
     * 
     * @param conversationName
     * @param conversationId
     * @return
     */
    public ConversationContext remove(String conversationName,
            String conversationId);

}
