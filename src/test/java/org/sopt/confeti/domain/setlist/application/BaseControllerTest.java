package org.sopt.confeti.domain.setlist.application;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sopt.confeti.domain.concert.infra.repository.ConcertRepository;
import org.sopt.confeti.domain.elastic_search.PerformanceDocument;
import org.sopt.confeti.domain.elastic_search.SearchTermDocument;
import org.sopt.confeti.domain.elastic_search.infra.PerformanceSearchRepository;
import org.sopt.confeti.domain.elastic_search.infra.SearchTermRepository;
import org.sopt.confeti.domain.festival.infra.repository.FestivalRepository;
import org.sopt.confeti.domain.user.infra.repository.UserRepository;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.domain.view.performance.infra.repository.PerformanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension.class)
@Testcontainers
@Import(TestElasticsearchConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseControllerTest {

    protected static final String DEFAULT_RESTDOC_PATH = "{class_name}/{method_name}/";
    protected RequestSpecification spec;

    @Autowired
    protected ElasticsearchOperations elasticsearchOperations;

    @Autowired
    protected SearchTermRepository searchTermRepository;

    @Autowired
    protected PerformanceSearchRepository performanceSearchRepository;

    @Autowired
    protected ConcertRepository concertRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    PerformanceRepository performanceRepository;

    @Autowired
    UserRepository userRepository;

    @LocalServerPort
    int port;

    @Container
    public static ElasticsearchContainer elasticsearchContainer = TestContainerManager.createElasticsearchContainer();

    @Container
    public static MySQLContainer<?> mySQLContainer = TestContainerManager.createMySQLContainer();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        elasticsearchContainer.start();
        mySQLContainer.start();

        TestContainerManager.configureElasticsearch(elasticsearchContainer, registry);
        TestContainerManager.configureMySQL(mySQLContainer, registry);
    }

    @BeforeAll
    void setUp() {
        // insert concert test data to mysql
        insertConcertTestData();

        // insert festival test data to mysql
        insertFestivalTestData();

        // insert performance test data to mysql
        insertPerformanceTestData();

        // insert user test data to mysql
        insertUserTestData();

        // insert search term test data to es
        createSearchTermTestIndex();
        insertSearchTermTestDataES();

        // insert performance test data to es
        createPerformanceTestIndex();
        insertPerformanceTestDataES();
    }

    @BeforeEach
    void setUpRestDocs(RestDocumentationContextProvider provider) {
        RestAssured.port = port;

        this.spec = new RequestSpecBuilder()
                .setPort(port)
                .addFilter(documentationConfiguration(provider)
                        .operationPreprocessors()
                        .withRequestDefaults(
                                modifyUris().scheme("http").host("localhost").port(8080),
                                prettyPrint()
                        )
                        .withResponseDefaults(prettyPrint())
                )
                .build();

        // 상속받는 테스트 환경에서 함수 오버라이딩으로 활성화
        setUpSearchTermTestData();
        setUpPerformanceTestData();
    }

    /**
     * 콘서트 베이스 데이터
     */
    private void insertConcertTestData() {
        concertRepository.saveAll(TestDataManager.concerts);
    }

    /**
     * 페스티벌 베이스 데이터
     */
    private void insertFestivalTestData() {
        festivalRepository.saveAll(TestDataManager.festivals);
    }

    /**
     * 공연 베이스 데이터
     */
    private void insertPerformanceTestData() {
        performanceRepository.saveAll(TestDataManager.performances);
    }

    /**
     * 공연 베이스 데이터
     */
    private void insertUserTestData() {
        userRepository.save(TestDataManager.user);
    }

    /**
     * Search Term 테스트 데이터 초기화 함수
     */
    private void setUpSearchTermTestData() {
        if (shouldResetSearchTermData()) {
            cleanSearchTermTestIndex();
            createSearchTermTestIndex();
            insertSearchTermTestDataES();
        }
    }

    private void cleanSearchTermTestIndex() {
        if (existSearchTermIndex()) {
            elasticsearchOperations.indexOps(SearchTermDocument.class).delete();
        }
    }

    private void createSearchTermTestIndex() {
        if (!existSearchTermIndex()) {
            elasticsearchOperations.indexOps(SearchTermDocument.class).createWithMapping();
        }
    }

    private void insertSearchTermTestDataES() {
        if (existSearchTermIndex()) {
            searchTermRepository.saveAll(
                    TestDataManager.searchTerms.stream()
                            .map(SearchTermDocument::create)
                            .toList()
            );

            elasticsearchOperations.indexOps(SearchTermDocument.class).refresh();
        }
    }

    private boolean existSearchTermIndex() {
        return elasticsearchOperations.indexOps(SearchTermDocument.class).exists();
    }

    protected abstract boolean shouldResetSearchTermData();

    /**
     * Performance 테스트 데이터 초기화 함수
     */
    private void setUpPerformanceTestData() {
        if (shouldResetPerformanceData()) {
            cleanPerformanceTestIndex();
            createPerformanceTestIndex();
            insertPerformanceTestDataES();
        }
    }

    private void cleanPerformanceTestIndex() {
        if (existPerformanceIndex()) {
            elasticsearchOperations.indexOps(PerformanceDocument.class).delete();
        }
    }

    private void createPerformanceTestIndex() {
        if (!existPerformanceIndex()) {
            elasticsearchOperations.indexOps(PerformanceDocument.class).createWithMapping();
        }
    }

    private void insertPerformanceTestDataES() {
        if (existPerformanceIndex()) {
            performanceSearchRepository.saveAll(
                    performanceRepository.findAll().stream()
                            .map(PerformanceDTO::from)
                            .map(PerformanceDocument::create)
                            .toList()
            );

            elasticsearchOperations.indexOps(PerformanceDocument.class).refresh();
        }
    }

    private boolean existPerformanceIndex() {
        return elasticsearchOperations.indexOps(PerformanceDocument.class).exists();
    }

    protected abstract boolean shouldResetPerformanceData();
}
