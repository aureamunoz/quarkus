package io.quarkus.it.kubernetes.client;

import static io.quarkus.it.kubernetes.client.CustomKubernetesMockServerTestResource.assertProperty;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusProdModeTest;
import io.quarkus.test.common.QuarkusTestResource;

@QuarkusTestResource(CustomKubernetesMockServerTestResource.class)
public class ConfigMapPropertiesProdModeTest {

    @RegisterExtension
    static final QuarkusProdModeTest config = new QuarkusProdModeTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class).addClasses(ConfigMapProperties.class))
            .setApplicationName("k8s-configMaps")
            .setApplicationVersion("0.1-SNAPSHOT");

    @Test
    public void testPropertiesReadFromConfigMap() {
        assertProperty("dummy", "dummy");
        assertProperty("someProp1", "val1");
        assertProperty("someProp2", "val2");
        assertProperty("someProp3", "val3");
        assertProperty("someProp4", "val4");
        assertProperty("someProp5", "val5");
    }

}
