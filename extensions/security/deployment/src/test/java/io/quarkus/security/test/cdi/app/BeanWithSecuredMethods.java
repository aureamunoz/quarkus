package io.quarkus.security.test.cdi.app;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
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

    @DenyAll
    public String forbidden() {
        return "shouldBeDenied";
    }

    @Secured(value = "admin")
    public String springSecuredMethod() {
        return "accessibleWithSecuredForAdminOnly";
    }

    @RolesAllowed("admin")
    public String securedMethod() {
        return "accessibleForAdminOnly";
    }

    public String unsecuredMethod() {
        return "accessibleForAll";
    }
}
