package io.quarkus.spring.security.deployment;

import java.lang.reflect.Modifier;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.MethodInfo;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.InterceptorBindingRegistrarBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.ApplicationIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.security.deployment.AdditionalSecurityCheckBuildItem;
import io.quarkus.security.deployment.SecurityCheckInstantiationUtil;
import io.quarkus.spring.security.runtime.interceptor.SpringSecuredInterceptor;

class SpringSecurityProcessor {

    private static final String FEATURE = "spring-security";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void registerSecurityInterceptors(BuildProducer<InterceptorBindingRegistrarBuildItem> registrars,
            BuildProducer<AdditionalBeanBuildItem> beans) {
        registrars.produce(new InterceptorBindingRegistrarBuildItem(new SpringSecurityAnnotationsRegistrar()));
        beans.produce(new AdditionalBeanBuildItem(SpringSecuredInterceptor.class));
    }

    @BuildStep
    void addSpringSecuredSecurityCheck(ApplicationIndexBuildItem index,
            BuildProducer<AdditionalSecurityCheckBuildItem> additionalSecurityCheckBuildItems) {
        for (AnnotationInstance instance : index.getIndex().getAnnotations(DotNames.SPRING_SECURED)) {
            if (instance.value() == null) {
                continue;
            }
            String[] rolesAllowed = instance.value().asStringArray();

            if (instance.target().kind() == AnnotationTarget.Kind.METHOD) {
                additionalSecurityCheckBuildItems.produce(new AdditionalSecurityCheckBuildItem(instance.target().asMethod(),
                        SecurityCheckInstantiationUtil.rolesAllowedSecurityCheck(rolesAllowed)));
            } else if (instance.target().kind() == AnnotationTarget.Kind.CLASS) {
                ClassInfo classInfo = instance.target().asClass();
                for (MethodInfo methodInfo : classInfo.methods()) {
                    if (!isPublicNonStaticNonConstructor(methodInfo)) {
                        continue;
                    }
                    additionalSecurityCheckBuildItems.produce(new AdditionalSecurityCheckBuildItem(methodInfo,
                            SecurityCheckInstantiationUtil.rolesAllowedSecurityCheck(rolesAllowed)));
                }
            }
        }
    }

    private boolean isPublicNonStaticNonConstructor(MethodInfo methodInfo) {
        return Modifier.isPublic(methodInfo.flags()) && !Modifier.isStatic(methodInfo.flags())
                && !"<init>".equals(methodInfo.name());
    }

}
