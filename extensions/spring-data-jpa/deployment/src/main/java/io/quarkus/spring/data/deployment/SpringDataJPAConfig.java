package io.quarkus.spring.data.deployment;

import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = "spring")
public class SpringDataJPAConfig {

    public DatasourceConfig datasource;
}
