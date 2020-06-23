package io.quarkus.it.kubernetes.client;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusProdModeTest;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;

@QuarkusTestResource(CustomKubernetesMockServerTestResource.class)
@TestProfile(AbsentConfigMapPropertiesTest.MyProfile.class)
@QuarkusTest
public class AbsentConfigMapPropertiesTest {

    @RegisterExtension
    static final QuarkusProdModeTest config = new QuarkusProdModeTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class).addClasses(ConfigMapProperties.class))
            .setApplicationVersion("0.1-SNAPSHOT")
            .setExpectedException(RuntimeException.class);
    //                        .assertBuildException(e -> assertThat(e)
    //                                .isInstanceOf(RuntimeException.class)
    //                                .hasCauseInstanceOf(RuntimeException.class)
    //                                .hasMessageContaining(
    //                                        "ConfigMap 'cmap4' not found in namespace 'demo'"));

    @Test
    public void buildShouldFail() throws IOException {
        fail("Build should have failed and therefore this method should not have been called");
    }

    public static class MyProfile implements QuarkusTestProfile {

        @Override
        public Map<String, String> getConfigOverrides() {
            Map<String, String> conf = new HashMap<>();
            conf.put("quarkus.kubernetes-config.enabled", "true");
            conf.put("quarkus.kubernetes-config.config-maps", "cmap4");
            conf.put("quarkus.kubernetes-config.namespace", "demo");
            conf.put("quarkus.kubernetes-config.secrets", "s1");
            return conf;
        }

    }

}
