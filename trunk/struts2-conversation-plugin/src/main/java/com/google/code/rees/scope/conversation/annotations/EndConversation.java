package com.google.code.rees.scope.conversation.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes a method as a conversation-terminating method.
 * 
 * Only works on methods that are also actions. The method may
 * be specified as an action through configuration in the struts.xml,
 * by convention by being named <code>execute()</code> in an action class,
 * of by annotation with the convention plug-in's Action annotation.
 * <P>
 * 
 * @see #conversations()
 * 
 */
@Target({ java.lang.annotation.ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EndConversation {

    /**
     * The conversations for which this method will act as an end-point.
     * <p>
     * In an action class annotated with the {@link ConversationController}
     * annotation, the {@link #conversations()} field will default to that of
     * the controller's conversations if left blank. If not in a
     * ConversationController, then the conversations field must be specified in
     * order to associate this end point.
     */
    public abstract String[] conversations() default {};

}