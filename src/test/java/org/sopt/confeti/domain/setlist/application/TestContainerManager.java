package org.sopt.confeti.domain.setlist.application;

import java.time.Duration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

public class TestContainerManager {

    private static final String ELASTICSEARCH_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch:9.0.1";
    private static final String MYSQL_IMAGE = "mysql:8.0";

    // Elasticsearch 컨테이너
    public static ElasticsearchContainer createElasticsearchContainer() {
        return new ElasticsearchContainer(DockerImageName.parse(ELASTICSEARCH_IMAGE))
                .withEnv("discovery.type", "single-node")
                .withEnv("xpack.security.enabled", "false")
                .withEnv("ES_JAVA_OPTS", "-Xms512m -Xmx512m")
                .withCommand("sh", "-c",
                        "elasticsearch-plugin install analysis-nori --batch && " +
                                "/usr/local/bin/docker-entrypoint.sh eswrapper")
                .withReuse(true)
                .withStartupTimeout(Duration.ofSeconds(120));
    }

    // MySQL 컨테이너
    public static MySQLContainer<?> createMySQLContainer() {
        return new MySQLContainer<>(DockerImageName.parse(MYSQL_IMAGE))
                .withDatabaseName("confeti_test")
                .withUsername("test")
                .withPassword("test123")
                .withEnv("MYSQL_ROOT_PASSWORD", "root_password")
                .withCommand("--character-set-server=utf8mb4",
                        "--collation-server=utf8mb4_unicode_ci",
                        "--sql-mode=STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION")
                .withReuse(true)
                .withStartupTimeout(Duration.ofSeconds(120));
    }

    // Elastic Search 동적 프로퍼티 설정
    public static void configureElasticsearch(ElasticsearchContainer container, DynamicPropertyRegistry registry) {
        String httpHostAddress = container.getHttpHostAddress();

        registry.add("spring.data.elasticsearch.uris", () -> "http://" + httpHostAddress);
        registry.add("spring.elasticsearch.uris", () -> "http://" + httpHostAddress);
    }

    // MySQL 동적 프로퍼티 설정
    public static void configureMySQL(MySQLContainer<?> container, DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");

        // JPA
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.MySQLDialect");
        registry.add("spring.jpa.show-sql", () -> "true");
    }
}
