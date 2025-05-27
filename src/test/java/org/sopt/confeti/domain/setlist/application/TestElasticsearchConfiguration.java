package org.sopt.confeti.domain.setlist.application;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@TestConfiguration
@EnableElasticsearchRepositories(basePackages = "org.sopt.confeti.domain.elastic_search.infra")
public class TestElasticsearchConfiguration extends ElasticsearchConfiguration {

    @Bean
    @Primary
    @Override
    public ClientConfiguration clientConfiguration() {
        // 컨테이너가 실행 중인지 확인
        if (!BaseControllerTest.elasticsearchContainer.isRunning()) {
            throw new IllegalStateException("Elasticsearch container is not running!");
        }

        // BaseControllerTest의 elasticsearchContainer 참조
        String httpHostAddress = BaseControllerTest.elasticsearchContainer.getHttpHostAddress();

        return ClientConfiguration.builder()
                .connectedTo(httpHostAddress)
                .withConnectTimeout(30000)
                .withSocketTimeout(60000)
                .build();
    }
}
