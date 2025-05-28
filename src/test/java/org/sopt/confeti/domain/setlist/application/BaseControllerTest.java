package org.sopt.confeti.domain.setlist.application;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
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
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension.class)
@Import(TestElasticsearchConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseControllerTest {

    protected static final String DEFAULT_RESTDOC_PATH = "{class_name}/{method_name}/";
    private static final Logger log = LoggerFactory.getLogger(BaseControllerTest.class);
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

    @LocalServerPort
    int port;

    protected static ElasticsearchContainer elasticsearchContainer = SharedTestContainers.ELASTICSEARCH_CONTAINER;

    protected static MySQLContainer<?> mySQLContainer = SharedTestContainers.MYSQL_CONTAINER;

    protected static String accessToken = "Empty";

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");

        registry.add("spring.data.elasticsearch.uris", elasticsearchContainer::getHttpHostAddress);
        registry.add("spring.elasticsearch.uris", elasticsearchContainer::getHttpHostAddress);
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

    @AfterEach
    void resetDatabase() {
        try {
            initializeAllTableData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
}
