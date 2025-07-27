package org.sopt.confeti.restdocs.base;

import java.time.Duration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;

public final class SharedTestContainers {

    private static final String ELASTICSEARCH_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch:9.0.1";
    private static final String MYSQL_IMAGE = "mysql:8.0";
    private static final String REDIS_IMAGE = "bitnami/redis:8.0.3";
    private static final String REDIS_SENTINEL_IMAGE = "bitnami/redis-sentinel:8.0.3";

    public static final String REDIS_MASTER_HOST = "redis-master";
    public static final String REDIS_SLAVE_1_HOST = "redis-slave-1";
    public static final String REDIS_SLAVE_2_HOST = "redis-slave-2";

    public static final String REDIS_SENTINEL_1_HOST = "redis-sentinel-1";
    public static final String REDIS_SENTINEL_2_HOST = "redis-sentinel-2";
    public static final String REDIS_SENTINEL_3_HOST = "redis-sentinel-3";

    public static final int REDIS_PORT = 6379;
    public static final int REDIS_SENTINEL_PORT = 26379;
    public static final String REDIS_SENTINEL_MASTER_HOST = REDIS_MASTER_HOST;
    public static final String REDIS_SENTINEL_MASTER_PORT = String.valueOf(REDIS_PORT);
    public static final String REDIS_SENTINEL_MASTER_GROUP = "redis-master";

    private static final String REDIS_LOG_MESSAGE = ".*Ready to accept connections.*";
    private static final String REDIS_SENTINEL_LOG_MESSAGE = ".*Sentinel ID is.*";

    private static final int STARTUP_TIMEOUT = 60;

    public static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>(MYSQL_IMAGE)
            .withDatabaseName("confeti_test")
            .withUsername("test")
            .withPassword("test123")
            .withEnv("MYSQL_ROOT_PASSWORD", "root_password")
            .withUrlParam("tc", "proxy")
            .withCommand("--character-set-server=utf8mb4",
                    "--collation-server=utf8mb4_unicode_ci",
                    "--sql-mode=STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION")
            .withStartupTimeout(Duration.ofSeconds(STARTUP_TIMEOUT));

    public static final ElasticsearchContainer ELASTICSEARCH_CONTAINER =
            new ElasticsearchContainer(ELASTICSEARCH_IMAGE)
                    .withEnv("discovery.type", "single-node")
                    .withEnv("xpack.security.enabled", "false")
                    .withEnv("ES_JAVA_OPTS", "-Xms512m -Xmx512m")
                    .withCommand("sh", "-c",
                            "elasticsearch-plugin install analysis-nori --batch && " +
                                    "/usr/local/bin/docker-entrypoint.sh eswrapper")
                    .withStartupTimeout(Duration.ofSeconds(STARTUP_TIMEOUT));

    private static final Network network = Network.newNetwork();

    @Container
    public static final GenericContainer<?> REDIS_MASTER = new GenericContainer<>(REDIS_IMAGE)
            .withExposedPorts(REDIS_PORT)
            .withCommand("redis-server", "--enable-debug-command", "yes", "--protected-mode", "no")
            .withNetwork(network)
            .withNetworkAliases(REDIS_MASTER_HOST)
            .withEnv("ALLOW_EMPTY_PASSWORD", "yes")
            .withEnv("REDIS_REPLICATION_MODE", "master")
            .waitingFor(Wait.forLogMessage(REDIS_LOG_MESSAGE, 1))
            .withStartupTimeout(Duration.ofSeconds(STARTUP_TIMEOUT));

    @Container
    public static final GenericContainer<?> REDIS_SLAVE_1 = new GenericContainer<>(REDIS_IMAGE)
            .withExposedPorts(REDIS_PORT)
            .withNetwork(network)
            .withNetworkAliases(REDIS_SLAVE_1_HOST)
            .withEnv("ALLOW_EMPTY_PASSWORD", "yes")
            .withEnv("REDIS_MASTER_HOST", REDIS_MASTER_HOST)
            .withEnv("REDIS_REPLICATION_MODE", "slave")
            .waitingFor(Wait.forLogMessage(REDIS_LOG_MESSAGE, 1))
            .withStartupTimeout(Duration.ofSeconds(STARTUP_TIMEOUT));

    @Container
    public static final GenericContainer<?> REDIS_SLAVE_2 = new GenericContainer<>(REDIS_IMAGE)
            .withExposedPorts(REDIS_PORT)
            .withNetwork(network)
            .withNetworkAliases(REDIS_SLAVE_2_HOST)
            .withEnv("ALLOW_EMPTY_PASSWORD", "yes")
            .withEnv("REDIS_MASTER_HOST", REDIS_MASTER_HOST)
            .withEnv("REDIS_REPLICATION_MODE", "slave")
            .waitingFor(Wait.forLogMessage(REDIS_LOG_MESSAGE, 1))
            .withStartupTimeout(Duration.ofSeconds(STARTUP_TIMEOUT));

    @Container
    public static final GenericContainer<?> REDIS_SENTINEL_1 = new GenericContainer<>(REDIS_SENTINEL_IMAGE)
            .withExposedPorts(REDIS_SENTINEL_PORT)
            .withNetwork(network)
            .withNetworkAliases(REDIS_SENTINEL_1_HOST)
            .withEnv("REDIS_SENTINEL_DOWN_AFTER_MILLISECONDS", "3000")
            .withEnv("REDIS_MASTER_HOST", REDIS_SENTINEL_MASTER_HOST)
            .withEnv("REDIS_MASTER_PORT_NUMBER", REDIS_SENTINEL_MASTER_PORT)
            .withEnv("REDIS_MASTER_SET", REDIS_SENTINEL_MASTER_GROUP)
            .withEnv("REDIS_SENTINEL_QUORUM", "2")
            .waitingFor(Wait.forLogMessage(REDIS_SENTINEL_LOG_MESSAGE, 1))
            .withStartupTimeout(Duration.ofSeconds(STARTUP_TIMEOUT));

    @Container
    public static final GenericContainer<?> REDIS_SENTINEL_2 = new GenericContainer<>(REDIS_SENTINEL_IMAGE)
            .withExposedPorts(REDIS_SENTINEL_PORT)
            .withNetwork(network)
            .withNetworkAliases(REDIS_SENTINEL_2_HOST)
            .withEnv("REDIS_SENTINEL_DOWN_AFTER_MILLISECONDS", "3000")
            .withEnv("REDIS_MASTER_HOST", REDIS_SENTINEL_MASTER_HOST)
            .withEnv("REDIS_MASTER_PORT_NUMBER", REDIS_SENTINEL_MASTER_PORT)
            .withEnv("REDIS_MASTER_SET", REDIS_SENTINEL_MASTER_GROUP)
            .withEnv("REDIS_SENTINEL_QUORUM", "2")
            .waitingFor(Wait.forLogMessage(REDIS_SENTINEL_LOG_MESSAGE, 1))
            .withStartupTimeout(Duration.ofSeconds(STARTUP_TIMEOUT));

    @Container
    public static final GenericContainer<?> REDIS_SENTINEL_3 = new GenericContainer<>(REDIS_SENTINEL_IMAGE)
            .withExposedPorts(REDIS_SENTINEL_PORT)
            .withNetwork(network)
            .withNetworkAliases(REDIS_SENTINEL_3_HOST)
            .withEnv("REDIS_SENTINEL_DOWN_AFTER_MILLISECONDS", "3000")
            .withEnv("REDIS_MASTER_HOST", REDIS_SENTINEL_MASTER_HOST)
            .withEnv("REDIS_MASTER_PORT_NUMBER", REDIS_SENTINEL_MASTER_PORT)
            .withEnv("REDIS_MASTER_SET", REDIS_SENTINEL_MASTER_GROUP)
            .withEnv("REDIS_SENTINEL_QUORUM", "2")
            .waitingFor(Wait.forLogMessage(REDIS_SENTINEL_LOG_MESSAGE, 1))
            .withStartupTimeout(Duration.ofSeconds(STARTUP_TIMEOUT));

    static {
        MYSQL_CONTAINER.start();
        ELASTICSEARCH_CONTAINER.start();
        REDIS_MASTER.start();
        REDIS_SLAVE_1.start();
        REDIS_SLAVE_2.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(REDIS_MASTER.getLogs());
        System.out.println(REDIS_SLAVE_1.getLogs());
        System.out.println(REDIS_SLAVE_2.getLogs());

        REDIS_SENTINEL_1.start();
        REDIS_SENTINEL_2.start();
        REDIS_SENTINEL_3.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(REDIS_SENTINEL_1.getLogs());
        System.out.println(REDIS_SENTINEL_2.getLogs());
        System.out.println(REDIS_SENTINEL_3.getLogs());
    }
}
