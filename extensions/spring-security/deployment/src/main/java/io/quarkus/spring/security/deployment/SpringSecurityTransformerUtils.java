package io.quarkus.spring.security.deployment;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.RolesAllowed;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.MethodInfo;
import org.springframework.security.access.annotation.Secured;

public class SpringSecurityTransformerUtils {

    public static final DotName ROLES_ALLOWED = DotName.createSimple(RolesAllowed.class.getName());

    public static final Map<DotName, Set<String>> SPRING_SECURITY_ANNOTATIONS = new HashMap<>();

    static {
        // keep the conents the same as in io.quarkus.resteasy.deployment.SecurityTransformerUtils
        SPRING_SECURITY_ANNOTATIONS.put(DotName.createSimple(Secured.class.getName()), Collections.singleton("value"));
    }

    public static boolean hasSpringSecurityAnnotation(MethodInfo methodInfo) {
        for (AnnotationInstance annotation : methodInfo.annotations()) {
            if (SPRING_SECURITY_ANNOTATIONS.keySet().contains(annotation.name())) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasSpringSecurityAnnotation(ClassInfo classInfo) {
        for (AnnotationInstance classAnnotation : classInfo.classAnnotations()) {
            if (SPRING_SECURITY_ANNOTATIONS.keySet().contains(classAnnotation.name())) {
                return true;
            }
        }

        return false;
    }
}
