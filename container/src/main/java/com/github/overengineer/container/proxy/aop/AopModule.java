package com.github.overengineer.container.proxy.aop;

import com.github.overengineer.container.*;
import com.github.overengineer.container.inject.DefaultInjectorFactory;
import com.github.overengineer.container.inject.InjectorFactory;
import com.github.overengineer.container.key.DefaultKeyRepository;
import com.github.overengineer.container.metadata.DefaultMetadataAdapter;
import com.github.overengineer.container.metadata.MetadataAdapter;
import com.github.overengineer.container.proxy.*;
import com.github.overengineer.container.key.GenericKey;
import com.github.overengineer.container.key.KeyRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rees.byars
 */
public class AopModule extends BaseModule {

    @Override
    protected void configure() {
        use(JdkProxyHandlerFactory.class).forType(ProxyHandlerFactory.class);
        use(ProxyComponentStrategyFactory.class).forType(ComponentStrategyFactory.class);
        use(JdkAopProxyHandlerFactory.class).forType(ProxyHandlerFactory.class);
        use(AdvisedInvocationFactory.class).forType(JoinPointInvocationFactory.class);
        use(DefaultPointcutInterpreter.class).forType(PointcutInterpreter.class);
        use(DefaultAopContainer.class).forType(AopContainer.class);
        useInstance(new ArrayList<Aspect>()).forGeneric(new GenericKey<List<Aspect>>(){});
    }
}