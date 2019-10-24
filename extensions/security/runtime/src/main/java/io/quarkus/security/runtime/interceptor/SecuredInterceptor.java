package io.quarkus.security.runtime.interceptor;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.springframework.security.access.annotation.Secured;

/**
 * @author Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com
 *         <br>
 *         Date: 03/10/2019
 */
@Interceptor
@Secured("")
@Priority(1)
public class SecuredInterceptor {

    @Inject
    SecurityHandler handler;

    @AroundInvoke
    public Object intercept(InvocationContext ic) throws Exception {
        return handler.handle(ic);
    }
}
