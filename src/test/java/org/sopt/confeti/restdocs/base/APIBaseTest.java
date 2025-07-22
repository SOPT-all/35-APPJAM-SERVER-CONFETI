package org.sopt.confeti.restdocs.base;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopt.confeti.auth.jwt.JwtTokenGenerator;
import org.sopt.confeti.domain.artist_favorite.infra.repository.ArtistFavoriteRepository;
import org.sopt.confeti.domain.concert.infra.repository.ConcertRepository;
import org.sopt.confeti.domain.concert_artist.infra.repository.ConcertArtistRepository;
import org.sopt.confeti.domain.concert_favorite.infra.repository.ConcertFavoriteRepository;
import org.sopt.confeti.domain.concert_reservation_url.infra.repository.ConcertReservationUrlRepository;
import org.sopt.confeti.domain.elastic_search.PerformanceDocument;
import org.sopt.confeti.domain.elastic_search.SearchTermDocument;
import org.sopt.confeti.domain.elastic_search.infra.PerformanceSearchRepository;
import org.sopt.confeti.domain.elastic_search.infra.SearchTermRepository;
import org.sopt.confeti.domain.festival.infra.repository.FestivalRepository;
import org.sopt.confeti.domain.festival_artist.infra.repository.FestivalArtistRepository;
import org.sopt.confeti.domain.festival_date.infra.repository.FestivalDateRepository;
import org.sopt.confeti.domain.festival_favorite.infra.repository.FestivalFavoriteRepository;
import org.sopt.confeti.domain.festival_reservation_url.infra.repository.FestivalReservationUrlRepository;
import org.sopt.confeti.domain.festival_stage.infra.repository.FestivalStageRepository;
import org.sopt.confeti.domain.festival_time.infra.repository.FestivalTimeRepository;
import org.sopt.confeti.domain.setlist.infra.repository.SetlistRepository;
import org.sopt.confeti.domain.timetable_festival.infra.repository.TimetableFestivalRepository;
import org.sopt.confeti.domain.token.infra.AppleTokenRepository;
import org.sopt.confeti.domain.token.infra.RefreshTokenRepository;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.infra.repository.UserRepository;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.domain.view.performance.infra.repository.PerformanceRepository;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension.class)
@Import(ElasticsearchTestConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class APIBaseTest {

    protected static final String DEFAULT_RESTDOC_PATH = "{class_name}/{method_name}/";
    protected static final Logger log = LoggerFactory.getLogger(APIBaseTest.class);
    protected RequestSpecification spec;

    @Autowired
    private DataSource dataSource;

    @Autowired
    protected ArtistFavoriteRepository artistFavoriteRepository;

    @Autowired
    protected ConcertRepository concertRepository;

    @Autowired
    protected ConcertArtistRepository concertArtistRepository;

    @Autowired
    protected ConcertFavoriteRepository concertFavoriteRepository;

    @Autowired
    protected ConcertReservationUrlRepository concertReservationUrlRepository;

    @Autowired
    protected FestivalRepository festivalRepository;

    @Autowired
    protected FestivalArtistRepository festivalArtistRepository;

    @Autowired
    protected FestivalDateRepository festivalDateRepository;

    @Autowired
    protected FestivalFavoriteRepository festivalFavoriteRepository;

    @Autowired
    protected FestivalReservationUrlRepository festivalReservationUrlRepository;

    @Autowired
    protected FestivalStageRepository festivalStageRepository;

    @Autowired
    protected FestivalTimeRepository festivalTimeRepository;

    @Autowired
    protected SetlistRepository setlistRepository;

    @Autowired
    protected TimetableFestivalRepository timetableFestivalRepository;

    @Autowired
    protected AppleTokenRepository appleTokenRepository;

    @Autowired
    protected RefreshTokenRepository refreshTokenRepository;

    @Autowired
    protected ElasticsearchOperations elasticsearchOperations;

    @Autowired
    protected SearchTermRepository searchTermRepository;

    @Autowired
    protected PerformanceSearchRepository performanceSearchRepository;

    @Autowired
    protected PerformanceRepository performanceRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected JwtTokenGenerator jwtTokenGenerator;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @LocalServerPort
    int port;

    protected static ElasticsearchContainer elasticsearchContainer = SharedTestContainers.ELASTICSEARCH_CONTAINER;

    protected static MySQLContainer<?> mySQLContainer = SharedTestContainers.MYSQL_CONTAINER;

    protected static GenericContainer<?> redisMaster = SharedTestContainers.REDIS_MASTER;
    protected static GenericContainer<?> redisSlave1 = SharedTestContainers.REDIS_SLAVE_1;
    protected static GenericContainer<?> redisSlave2 = SharedTestContainers.REDIS_SLAVE_2;

    protected static String accessToken = "Empty";

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        // mysql
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");

        // elastic search
        registry.add("spring.data.elasticsearch.uris", elasticsearchContainer::getHttpHostAddress);
        registry.add("spring.elasticsearch.uris", elasticsearchContainer::getHttpHostAddress);

        // redis
        registry.add("spring.redis.host", redisMaster::getHost);
        registry.add("spring.redis.port",
                () -> redisMaster.getMappedPort(SharedTestContainers.REDIS_PORT).toString());
        registry.add("spring.redis.slaves[0].host", redisSlave1::getHost);
        registry.add("spring.redis.slaves[0].port",
                () -> redisSlave1.getMappedPort(SharedTestContainers.REDIS_PORT).toString());
        registry.add("spring.redis.slaves[1].host", redisSlave2::getHost);
        registry.add("spring.redis.slaves[1].port",
                () -> redisSlave2.getMappedPort(SharedTestContainers.REDIS_PORT).toString());
    }

    @BeforeEach
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

        // generate Access Token
        generateAccessToken();

        // clear redis
        clearRedisData();
    }

    @BeforeEach
    void setUpRestDocs(RestDocumentationContextProvider provider) {
        RestAssured.port = port;

        this.spec = new RequestSpecBuilder()
                .setPort(port)
                .addFilter(documentationConfiguration(provider)
                        .operationPreprocessors()
                        .withRequestDefaults(
                                modifyHeaders().set("Authorization", "Bearer {access_token}"),
                                prettyPrint()
                        )
                        .withResponseDefaults(prettyPrint())
                )
                .build();

        // 상속받는 테스트 환경에서 함수 오버라이딩으로 활성화
        setUpSearchTermTestData();
        setUpPerformanceTestData();
    }

    @AfterEach
    void resetDatabase() {
        try {
            initializeAllTableData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 레디스의 모든 데이터를 삭제하는 함수
     */
    private void clearRedisData() {
        try {
            redisTemplate.getConnectionFactory().getConnection().flushAll();
            log.info("=== Redis 모든 데이터 초기화 완료 ===");
        } catch (Exception e) {
            log.error("Redis 데이터 초기화 실패: {}", e.getMessage());
        }
    }

    /**
     * mysql의 모든 데이터를 삭제하는 함수
     */
    private void initializeAllTableData() {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {

                // 외래키 제약 해제
                statement.execute("SET FOREIGN_KEY_CHECKS = 0");

                // 모든 테이블 조회
                List<String> tableNames = getAllTableNames(connection);

                // 모든 테이블 데이터 삭제
                for (String tableName : tableNames) {
                    try {
                        statement.execute("DELETE FROM " + tableName);
                        System.out.println("테이블 데이터 삭제: " + tableName);
                    } catch (Exception e) {
                        log.warn("테이블 초기화 스킵 : {}", tableName);
                    }
                }

                // AUTO_INCREMENT 값 초기화
                for (String tableName : tableNames) {
                    try {
                        statement.execute("ALTER TABLE " + tableName + " AUTO_INCREMENT = 1");
                    } catch (Exception e) {
                        log.warn("AUTO_INCREMENT 초기화 스킵 : {}", tableName);
                    }
                }

                // 4. 외래키 제약 복원
                statement.execute("SET FOREIGN_KEY_CHECKS = 1");

                log.info("=== 모든 테이블 데이터 초기화 완료 ===");
            }
        } catch (Exception e) {
            log.error("테이블 데이터 초기화 실패");
        }
    }

    private List<String> getAllTableNames(Connection connection) throws Exception {
        List<String> tableNames = new ArrayList<>();
        ResultSet tables = connection.getMetaData().getTables(
                connection.getCatalog(), null, "%", new String[]{"TABLE"}
        );

        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");
            tableNames.add(tableName);
        }

        return tableNames;
    }

    /**
     * 콘서트 베이스 데이터
     */
    private void insertConcertTestData() {
        concertRepository.saveAll(TestDataManager.createConcerts());
    }

    /**
     * 페스티벌 베이스 데이터
     */
    private void insertFestivalTestData() {
        festivalRepository.saveAll(TestDataManager.createFestivals());
    }

    /**
     * 공연 베이스 데이터
     */
    private void insertPerformanceTestData() {
        performanceRepository.saveAll(TestDataManager.createPerformances());
    }

    /**
     * 공연 베이스 데이터
     */
    private void insertUserTestData() {
        userRepository.save(TestDataManager.createUser());
    }

    /**
     * Search Term 테스트 데이터 초기화 함수
     */
    private void setUpSearchTermTestData() {
        if (shouldResetESSearchTermData()) {
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

    protected abstract boolean shouldResetESSearchTermData();

    /**
     * Performance 테스트 데이터 초기화 함수
     */
    private void setUpPerformanceTestData() {
        if (shouldResetESPerformanceData()) {
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

    protected abstract boolean shouldResetESPerformanceData();

    /**
     * 유저의 Access Token을 발급받는 함수
     */
    private void generateAccessToken() {
        User user = getUser();

        accessToken = "Bearer " + jwtTokenGenerator.createAccessToken(
                String.valueOf(user.getId()), user.getRole(), user.getProvider()
        );
    }

    /**
     * Util 함수
     */
    protected User getUser() {
        return userRepository.findBySocialIdAndProvider(
                TestDataManager.userSocialId, TestDataManager.userProvider
        ).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND)
        );
    }

    /**
     * 문서 구성 함수
     */
    protected ResourceSnippetParametersBuilder tag(String name) {
        return ResourceSnippetParameters.builder().tag(name);
    }

    protected RestDocumentationFilter APIDocument(ResourceSnippetParametersBuilder resourceSnippetParametersBuilder) {
        return document(DEFAULT_RESTDOC_PATH,
                resource(resourceSnippetParametersBuilder.build())
        );
    }

    protected Object detail(Object obj) {
        return obj;
    }

    protected Object detail(Object... objs) {
        List<String> details = Arrays.stream(objs)
                .map(String::valueOf)
                .toList();

        return String.join("<br>", details);
    }
}
