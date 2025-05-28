package org.sopt.confeti.domain.setlist.application;

import java.time.Duration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

public final class SharedTestContainers {

    private static final String ELASTICSEARCH_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch:9.0.1";
    private static final String MYSQL_IMAGE = "mysql:8.0";

    public static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>(MYSQL_IMAGE)
            .withDatabaseName("confeti_test")
            .withUsername("test")
            .withPassword("test123")
            .withEnv("MYSQL_ROOT_PASSWORD", "root_password")
            .withCommand("--character-set-server=utf8mb4",
                    "--collation-server=utf8mb4_unicode_ci",
                    "--sql-mode=STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION")
            .withReuse(true)
            .withStartupTimeout(Duration.ofSeconds(120));

    public static final ElasticsearchContainer ELASTICSEARCH_CONTAINER =
            new ElasticsearchContainer(ELASTICSEARCH_IMAGE)
                    .withEnv("discovery.type", "single-node")
                    .withEnv("xpack.security.enabled", "false")
                    .withEnv("ES_JAVA_OPTS", "-Xms512m -Xmx512m")
                    .withCommand("sh", "-c",
                            "elasticsearch-plugin install analysis-nori --batch && " +
                                    "/usr/local/bin/docker-entrypoint.sh eswrapper")
                    .withReuse(true)
                    .withStartupTimeout(Duration.ofSeconds(120));

    static {
        MYSQL_CONTAINER.start();
        ELASTICSEARCH_CONTAINER.start();
    }
}
