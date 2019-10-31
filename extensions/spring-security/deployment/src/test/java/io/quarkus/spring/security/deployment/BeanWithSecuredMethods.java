package io.quarkus.spring.security.deployment;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.springframework.security.access.annotation.Secured;

/**
 * @author Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com
 */
@ApplicationScoped
@Named(BeanWithSecuredMethods.NAME)
public class BeanWithSecuredMethods {
    public static final String NAME = "super-bean";

    @Secured("admin")
    public String securedMethod() {
        return "accessibleForAdminOnly";
    }

    public String unsecuredMethod() {
        return "accessibleForAll";
    }
}
