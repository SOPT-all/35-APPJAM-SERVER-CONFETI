package org.sopt.confeti.restdocs.base;

import java.time.Duration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

public final class SharedTestContainers {

    private static final String ELASTICSEARCH_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch:9.0.1";
    private static final String MYSQL_IMAGE = "mysql:8.0";
    private static final String REDIS_IMAGE = "redis:latest";

    public static final int REDIS_PORT = 6379;
    private static final int STARTUP_TIMEOUT = 120;

    public static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>(MYSQL_IMAGE)
            .withDatabaseName("confeti_test")
            .withUsername("test")
            .withPassword("test123")
            .withEnv("MYSQL_ROOT_PASSWORD", "root_password")
            .withCommand("--character-set-server=utf8mb4",
                    "--collation-server=utf8mb4_unicode_ci",
                    "--sql-mode=STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION")
            .withReuse(true)
            .withStartupTimeout(Duration.ofSeconds(STARTUP_TIMEOUT));

    public static final ElasticsearchContainer ELASTICSEARCH_CONTAINER =
            new ElasticsearchContainer(ELASTICSEARCH_IMAGE)
                    .withEnv("discovery.type", "single-node")
                    .withEnv("xpack.security.enabled", "false")
                    .withEnv("ES_JAVA_OPTS", "-Xms512m -Xmx512m")
                    .withCommand("sh", "-c",
                            "elasticsearch-plugin install analysis-nori --batch && " +
                                    "/usr/local/bin/docker-entrypoint.sh eswrapper")
                    .withReuse(true)
                    .withStartupTimeout(Duration.ofSeconds(STARTUP_TIMEOUT));

    public static final GenericContainer<?> REDIS_CONTAINER = new GenericContainer<>(REDIS_IMAGE)
            .withExposedPorts(REDIS_PORT)
            .waitingFor(Wait.forListeningPort())
            .withStartupTimeout(Duration.ofSeconds(STARTUP_TIMEOUT))
            .withReuse(true);

    static {
        MYSQL_CONTAINER.start();
        ELASTICSEARCH_CONTAINER.start();
        REDIS_CONTAINER.start();
    }
}
