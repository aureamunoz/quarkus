package io.quarkus.spring.security;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.access.annotation.Secured;

import io.quarkus.security.AnnotationChecksService;
import io.quarkus.security.Check;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.security.identity.SecurityIdentity;

public class SpringSecurityAnnotationChecksServiceImpl implements AnnotationChecksService {

    //    private static final List<Class<? extends Annotation>> SECURITY_ANNOTATIONS = asList(Authenticated.class, DenyAll.class,
    //            PermitAll.class, RolesAllowed.class);

    //    private final Map<Method, Optional<io.quarkus.security.runtime.interceptor.Check>> checkForMethod = new ConcurrentHashMap<>();

    //    Map<Annotation, Optional<Check>> getAnnotationsToCheckMap(){
    //        HashMap<Class<? extends Annotation>, Optional<Check>> checksByAnnotationMap = new HashMap<>();
    //        checksByAnnotationMap.put(Authenticated.class, Optional.of(new AuthenticatedCheck()));
    //        checksByAnnotationMap.put(DenyAll.class, Optional.of(new DenyAllCheck()));
    //        checksByAnnotationMap.put(PermitAll.class, Optional.of(new PermitAllCheck()));
    //        checksByAnnotationMap.put(RolesAllowed.class, Optional.of(new RolesAllowedCheck()));
    ////        for (Class<? extends Annotation> annotation : SECURITY_ANNOTATIONS) {
    ////            Annotation securityAnnotation = (Annotation) annotation;
    ////            checksByAnnotationMap.put(securityAnnotation,checkForAnnotation(securityAnnotation))
    ////
    ////        }
    //
    //
    //        return null;
    //    }

    public Optional<Check> checkForAnnotation(Annotation securityAnnotation) {
        if (securityAnnotation instanceof Secured) {
            Secured rolesAllowed = (Secured) securityAnnotation;
            return Optional.of(new SecuredCheck(rolesAllowed.value()));
        }
        return Optional.empty();
    }

    //    private Annotation getAnnotation(Method method, Collection<Annotation> interceptorBindings) {
    //        Annotation securityAnnotation = determineSecurityAnnotation(method.getDeclaredAnnotations(), method::toString);
    //        if (securityAnnotation == null) {
    //            Class<?> declaringClass = method.getDeclaringClass();
    //            securityAnnotation = determineSecurityAnnotation(declaringClass.getDeclaredAnnotations(),
    //                    declaringClass::getCanonicalName);
    //        }
    //        if (securityAnnotation == null) {
    //            securityAnnotation = determineSecurityAnnotationFromBindings(interceptorBindings, method::toString);
    //        }
    //        return securityAnnotation;
    //    }

    //    private Optional<SecurityConstrainer.Check> determineSecurityCheck(Method method, Collection<Annotation> interceptorBindings) {
    //        Annotation securityAnnotation = getAnnotation(method,interceptorBindings);
    //        return checkForAnnotation(securityAnnotation);
    //    }

    //    private Annotation determineSecurityAnnotationFromBindings(Collection<Annotation> interceptorBindings,
    //                                                               Supplier<String> annotationPlacement) {
    //
    //        List<Annotation> securityAnnotations = new ArrayList<>();
    //        for (Annotation binding : interceptorBindings) {
    //            if (isSecurityAnnotation(binding)) {
    //                securityAnnotations.add(binding);
    //            }
    //        }
    //        return getExactlyOne(securityAnnotations, annotationPlacement);
    //    }
    //
    //    private Annotation determineSecurityAnnotation(Annotation[] annotations, Supplier<String> annotationPlacement) {
    //        List<Annotation> securityAnnotations = new ArrayList<>();
    //        for (Annotation binding : annotations) {
    //            if (isSecurityAnnotation(binding)) {
    //                securityAnnotations.add(binding);
    //            }
    //        }
    //        return getExactlyOne(securityAnnotations, annotationPlacement);
    //    }
    //
    //    private boolean isSecurityAnnotation(Annotation binding) {
    //        boolean isSecurityAnnotation = false;
    //        for (Class<? extends Annotation> annotationClass : SECURITY_ANNOTATIONS) {
    //            if (annotationClass == binding.annotationType()) {
    //                isSecurityAnnotation = true;
    //            }
    //        }
    //        return isSecurityAnnotation;
    //    }
    //
    //    private Annotation getExactlyOne(List<Annotation> securityAnnotations, Supplier<String> annotationPlacement) {
    //        switch (securityAnnotations.size()) {
    //            case 0:
    //                return null;
    //            case 1:
    //                return securityAnnotations.get(0);
    //            default:
    //                throw new IllegalStateException("Duplicate security annotations found on "
    //                        + annotationPlacement.get() +
    //                        ". Expected at most 1 annotation, found: " + securityAnnotations);
    //        }
    //    }

    private static class SecuredCheck implements Check {
        private final String[] allowedRoles;

        private SecuredCheck(String[] allowedRoles) {
            this.allowedRoles = allowedRoles;
        }

        @Override
        public void apply(SecurityIdentity identity) {
            Set<String> roles = identity.getRoles();
            if (roles != null) {
                for (String role : allowedRoles) {
                    if (roles.contains(role)) {
                        return;
                    }
                }
            }
            if (identity.isAnonymous()) {
                throw new UnauthorizedException();
            } else {
                throw new ForbiddenException();
            }
        }
    }

    private static class DenyAllCheck implements Check {
        @Override
        public void apply(SecurityIdentity identity) {
            if (identity.isAnonymous()) {
                throw new UnauthorizedException();
            } else {
                throw new ForbiddenException();
            }
        }
    }

    private static class PermitAllCheck implements Check {
        @Override
        public void apply(SecurityIdentity identity) {
        }
    }

    private static class AuthenticatedCheck implements Check {

        @Override
        public void apply(SecurityIdentity identity) {
            if (identity.isAnonymous()) {
                throw new UnauthorizedException();
            }
        }
    }

}
