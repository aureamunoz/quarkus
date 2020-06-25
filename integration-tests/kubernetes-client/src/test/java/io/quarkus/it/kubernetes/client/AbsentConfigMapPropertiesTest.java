package io.quarkus.it.kubernetes.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.LogRecord;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.LogFile;
import io.quarkus.test.ProdBuildResults;
import io.quarkus.test.ProdModeTestResults;
import io.quarkus.test.QuarkusProdModeTest;
import io.quarkus.test.common.QuarkusTestResource;

@QuarkusTestResource(CustomKubernetesMockServerTestResource.class)
public class AbsentConfigMapPropertiesTest {

    @RegisterExtension
    static final QuarkusProdModeTest config = new QuarkusProdModeTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class).addClasses(ConfigMapProperties.class))
            .setApplicationName("kubernetes-with-namespaced-configMaps")
            .setApplicationVersion("0.1-SNAPSHOT")
            .setRun(true)
            .setExpectExit(true)
                        .setLogFileName("milog.log")
            .setLogRecordPredicate(
                    r -> "io.quarkus.kubernetes.client.runtime.KubernetesConfigSourceProvider".equals(r.getLoggerName()))
            .withConfigurationResource("application-demo.properties");

    @ProdBuildResults
    private ProdModeTestResults prodModeTestResults;

    @LogFile
    private Path logfile;

    @Test
    public void buildShouldFail() throws IOException {
        List<LogRecord> buildLogRecords = prodModeTestResults.getRetainedBuildLogRecords();
        assertThat(buildLogRecords).isNotEmpty();
    }


}
