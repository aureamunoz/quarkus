package io.quarkus.spring.data.deployment;

import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigGroup
public class DatasourceConfig {

    /**
     * Name of the file containing the SQL statements to execute when Hibernate ORM starts.
     *
     * @asciidoclet
     */
    @ConfigItem(defaultValueDocumentation = "import.sql (DEV,TEST)")
    public Optional<String> data;
}
