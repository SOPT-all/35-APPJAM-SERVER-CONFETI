# 구조 및 설계 컨벤션

## 1. 패키지 구조

### 기본 구조

- `api`: Controller, DTO, Facade를 포함하며 클라이언트 요청을 처리하는 프레젠테이션 계층
    - `controller`: RESTful API 엔드포인트 정의
    - `dto`: Request/Response DTO 정의
    - `facade`: 여러 도메인 서비스를 조율하는 Facade 클래스
- `domain`: 도메인별로 구성되며 각 도메인은 다음의 하위 패키지를 가짐
    - Entity 클래스 (도메인 모델)
    - `application`: Service 클래스와 DTO 정의
    - `infra`: JPA Repository 구현체 및 외부 시스템 연동
- `auth`: 인증/인가 관련 로직 (JWT, OAuth, 로그인/로그아웃 등)
- `external`: 외부 API 클라이언트 (Feign Client 등)
- `global`: 공통 설정, 예외 처리, 유틸리티, 어노테이션 등

### 규칙

- **서비스 간 참조 금지**: 도메인 Service는 다른 도메인의 Service를 직접 참조하지 않음
- **Facade 계층 필수**: 여러 도메인 Service를 사용해야 하는 비즈니스 로직은 Facade 계층에서 처리
- 하위 모듈이 많을 경우 bounded context별로 패키지 분리

## 2. Facade 패턴

### 목적

도메인별로 Service를 나누면서 발생하는 서비스 간 참조 문제를 방지하기 위해 Facade 계층을 도입함.

### 역할

- 여러 도메인의 Service를 조율하여 복잡한 비즈니스 로직 처리
- Controller와 Service 사이의 중간 계층으로 동작
- 트랜잭션 경계 설정 (필요 시 `@Transactional` 적용)

### 구현 규칙

- `@Facade` 어노테이션 사용
- `api/{domain}/facade` 패키지에 위치
- 여러 도메인의 Service를 주입받아 사용 가능
- DTO 변환 로직 포함 가능

### 예시

```java

@Facade
@RequiredArgsConstructor
public class SetlistFacade {

    private final SetlistService setlistService;
    private final SetlistEditService setlistEditService;
    private final UserService userService;  // 다른 도메인의 Service 참조

    public SetlistCreateResponseDTO createSetLists(Long userId,
        List<SetlistCreateRequestDTO> requests) {
        User user = userService.findById(userId);  // UserService 사용
        return new SetlistCreateResponseDTO(
            setlistService.createSetLists(user, requests)  // SetlistService 사용
        );
    }
}
```

## 3. 도메인 모델 설계

### Entity 설계

- JPA Entity로 도메인 모델 구현
- 불변성과 캡슐화를 중심으로 설계
- Setter 사용 최소화 (필요한 경우에만 `@Setter` 명시 - 사용 이유 주석 필수)
- Builder 패턴 및 정적 팩토리 메서드 사용 권장 (`@Builder`)
- 생성일자 (`created_at`), 수정일자 (`updated_at`) 추가 필수
    - JpaAuditing 사용

### 테이블 네이밍

- 복수형 사용 (예: `users`, `concerts`, `festivals`)
- 스네이크 케이스(snake_case) 적용: `@Table(name = "table_name")`

### 컬럼 네이밍

- 스네이크 케이스 적용 (예: `created_at`, `user_id`)
- 불리언: `is_` 접두사 사용 불필요 (JPA는 필드명 그대로 매핑)
- Enum: `@Enumerated(EnumType.STRING)` 사용 권장

### ID 생성 전략

- `@GeneratedValue(strategy = GenerationType.IDENTITY)` 사용

### 연관관계

- 지연 로딩 우선 사용: `fetch = FetchType.LAZY`
- 양방향 연관관계 시 연관관계 편의 메서드 작성
- Cascade 타입은 신중하게 설정

### 감사(Auditing) 필드

- `@CreatedDate`, `@LastModifiedDate` 사용
- `@EntityListeners(AuditingEntityListener.class)` 적용

## 4. Repository 네이밍 규칙

### 조회(Query)

- `find`를 기본으로 사용 (예: `findById`, `findAllByUserId`)
- 단건 조회: `findBy...` 형태로 `Optional` 반환
- 다건 조회: `findAllBy...` 또는 `findBy...` 형태로 `List` 반환
- 존재 여부 확인: `existsBy...` 형태로 `boolean` 반환
- 카운트: `countBy...` 형태로 `long` 반환

### 저장, 수정, 삭제(Command)

- 저장: `save` 사용
- 수정: JPA의 Dirty Checking 활용 (별도 메서드 불필요)
- 삭제: `delete` 또는 `deleteById` 사용

### Repository 구성

- Repository는 인터페이스로 정의하며 `JpaRepository` 상속
- 커스텀 쿼리는 `@Query` 어노테이션 사용
- Repository는 `domain/{domain}/infra/repository` 패키지에 위치

## 5. Service 네이밍 규칙

### 조회(Query)

- `get` 사용을 권장 (예: `getUserInfo`, `getSetlistDetail`)
- null 체크와 예외 처리 필수
    - 예: `repository.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND))`
- 리스트 조회: `getAll...` 형태 사용 (예: `getAllMySetlists`)

### 저장, 수정, 삭제(Command)

- 생성: `create` 사용 (예: `createSetList`)
- 수정: `update` 또는 `patch` 사용
- 삭제: `delete` 사용

### Service 구성

- `@Service` 어노테이션 사용
- `domain/{domain}/application` 패키지에 위치
- 트랜잭션 관리 필수 (아래 참조)

## 6. 서비스 계층

### 트랜잭션 관리

- 모든 쓰기 작업에 `@Transactional` 적용
- 읽기 전용 메서드는 `@Transactional(readOnly = true)` 사용
- 트랜잭션 경계는 Service 또는 Facade 메서드 단위로 설정
- 기본 Propagation은 `REQUIRED` 사용

### 의존성 주입

- 생성자 주입 방식 사용 (`@RequiredArgsConstructor` 활용)
- 순환 참조 절대 금지 (Facade 패턴으로 해결)

### 예외 처리

- 비즈니스 로직에서 발생하는 예외는 커스텀 예외 클래스 사용
- `ErrorMessage` Enum을 활용하여 예외 메시지 관리

## 7. DTO와 데이터 매핑

### DTO 설계 원칙

- **모든 DTO는 반드시 `record` 클래스로 작성** (필수)
- Request와 Response DTO 분리
- DTO는 불변(immutable)하며 setter 메서드 절대 금지
- 필드 접근은 record의 자동 생성 accessor 메서드 사용 (예: `id()`)

### record 클래스 사용

```java
// Request DTO 예시
public record LoginRequest(
        @NotBlank String provider,
        @NotBlank String idToken
    ) {

}
```

### DTO 위치

- API 요청/응답 DTO: `api/{domain}/dto/request` 또는 `api/{domain}/dto/response`
- Facade 내부 DTO: `api/{domain}/facade/dto/request` 또는 `api/{domain}/facade/dto/response`
- Domain Service 내부 DTO: `domain/{domain}/application/dto`

### Validation

- Request DTO에 Bean Validation 어노테이션 사용 (`@NotNull`, `@NotBlank` 등)
- Controller 메서드 파라미터에 `@Valid` 적용

## 8. 커스텀 어노테이션

### @Permission

#### 목적

API 엔드포인트에 대한 권한 기반 접근 제어를 수행함.

#### 사용 위치

- Controller 메서드에만 적용 가능 (`@Target(ElementType.METHOD)`)

#### 동작 방식

1. HTTP 요청의 `Authorization` 헤더에서 JWT 토큰 추출
2. 토큰에서 사용자의 Role 정보 파싱
3. 메서드에 지정된 허용 Role과 비교하여 접근 권한 검증
4. 권한이 없으면 `FORBIDDEN` 예외 발생

#### 속성

- `role`: 허용할 Role 배열 (기본값: `Role.ONBOARDING`)
    - `Role.ONBOARDING`: 온보딩 중인 사용자
    - `Role.GENERAL`: 일반 사용자
    - `Role.ADMIN`: 관리자

#### 사용 예시

```java

@Permission(role = {Role.GENERAL})
@GetMapping("/user/info")
public ResponseEntity<BaseResponse<UserInfoResponse>> getUserInfo(@UserId Long userId) {
    // Role.GENERAL 권한을 가진 사용자만 접근 가능
}

@Permission(role = {Role.ONBOARDING, Role.GENERAL})
@PostMapping("/auth/reissue")
public ResponseEntity<BaseResponse<Token>> reissue(@RefreshToken String refreshToken) {
    // Role.ONBOARDING 또는 Role.GENERAL 권한을 가진 사용자 접근 가능
}
```

### @UserId

#### 목적

JWT 토큰에서 사용자 ID를 자동으로 추출하여 Controller 메서드 파라미터에 주입함.

#### 사용 위치

- Controller 메서드의 파라미터에만 적용 가능 (`@Target(ElementType.PARAMETER)`)
- 파라미터 타입은 `Long`이어야 함

#### 동작 방식

1. HTTP 요청의 `Authorization` 헤더에서 JWT 토큰 추출
2. 토큰에서 사용자 ID(subject) 추출
3. 파라미터에 Long 타입으로 주입
4. 토큰이 없거나 유효하지 않으면 예외 발생 (require=true인 경우)

#### 속성

- `require`: 토큰 필수 여부 (기본값: `true`)
    - `true`: 토큰이 없으면 `BAD_REQUEST` 예외 발생
    - `false`: 토큰이 없으면 `null` 반환 (인증 선택적 API에 사용)

#### 사용 예시

```java
// 필수 인증 (토큰 없으면 예외 발생)
@Permission(role = {Role.GENERAL})
@GetMapping("/my/setlists/all")
public ResponseEntity<BaseResponse<GetAllSetlistsResponse>> getAllMySetlists(
    @UserId Long userId  // require = true (기본값)
) {
    // userId는 항상 존재함
}

// 선택적 인증 (토큰 없으면 null 반환)
@GetMapping("/artists/search/ac")
public ResponseEntity<BaseResponse<SearchACArtistsResponse>> searchAutoComplete(
    @UserId(require = false) Long userId,  // 로그인하지 않은 사용자도 접근 가능
    @RequestParam String term
) {
    // userId는 null일 수 있음
}
```

#### 주의사항

- `@UserId`는 `@Permission`과 함께 사용하는 것이 일반적
- `@Permission` 없이 `@UserId`만 사용하면 권한 검증 없이 ID만 추출됨
- Swagger 문서에서 자동으로 숨김 처리됨 (`@Schema(hidden = true)`)

---

# API 응답 코드 컨벤션

## API 표준 응답 코드

| 코드  | 응답명                   | 사용 용도                                                       |
|-----|-----------------------|-------------------------------------------------------------|
| 200 | OK                    | 요청이 정상적으로 처리됨 (`ApiResponseUtil.success()` 사용)              |
| 201 | Created               | 리소스가 성공적으로 생성됨 (`ResponseEntity.created(uri).body()` 사용)    |
| 204 | No Content            | 요청 성공, 반환할 콘텐츠 없음 (`ResponseEntity.noContent().build()` 사용) |
| 400 | Bad Request           | 잘못된 요청 (`@Valid` 검증 실패 등)                                   |
| 401 | Unauthorized          | 인증 필요 (JWT 토큰 검증 실패)                                        |
| 403 | Forbidden             | 권한 없음 (`@Permission` 권한 검증 실패)                              |
| 404 | Not Found             | 리소스 없음 (Entity 조회 실패)                                       |
| 405 | Method Not Allowed    | 지원하지 않는 HTTP 메서드                                            |
| 409 | Conflict              | 리소스 상태 충돌 (중복 생성 시도 등)                                      |
| 422 | Unprocessable Entity  | 의미적 오류 (비즈니스 규칙 위반)                                         |
| 500 | Internal Server Error | 서버 내부 오류 (처리되지 않은 예외)                                       |

## Spring Boot 에러 코드

### ErrorMessage Enum

- 모든 에러 코드는 `ErrorMessage` Enum으로 관리
- Enum 이름: `UPPER_SNAKE_CASE` 사용
- 메시지: **한국어**로 작성 (프로젝트 정책)

### 에러 메시지 규칙

- 간결하고 명확하게: 사용자가 이해하기 쉬운 메시지 작성
- 실행 가능한 정보: 가능한 경우 문제 해결에 도움이 되는 정보 포함
- 기술적 세부사항 제외: 구현 세부사항이나 스택 트레이스 노출 금지

## 비즈니스 예외 클래스

- ErrorMessage를 받는 예외 클래스를 각 상황에 맞게 구현하여 사용
- 공통 예외: `ConfetiException` (프로젝트명 기반)
- 특정 예외: `NotFoundException`, `UnauthorizedException`, `ConflictException` 등

## GlobalExceptionHandler

- `@RestControllerAdvice`를 사용하여 전역 예외 처리
- 각 예외 타입별로 `@ExceptionHandler` 메서드 정의
- 예외 정보를 request attribute에 저장하여 로깅 가능
- `ApiResponseUtil.failure()`를 사용하여 일관된 에러 응답 생성

---

# RDB 스키마 컨벤션

## 테이블 네이밍

- 스네이크 케이스(snake_case) 적용: 소문자와 언더스코어 조합 (예: `user_account`)
- **복수형 사용** (예: `users`, `concerts`, `festivals`)

## 컬럼 네이밍

- 스네이크 케이스 적용 (예: `created_at`, `user_id`)
- 불리언: `is_` 접두사 사용하지 않음 (JPA는 필드명 그대로 매핑)
- 약어보다 전체 단어 사용: `desc` 대신 `description` 권장
- 데이터 타입 사용 금지: 컬럼 이름에 데이터 타입 포함 금지 (예: `text`, `int`)
- Enum 컬럼: `VARCHAR` 타입으로 Enum 이름 저장 (`@Enumerated(EnumType.STRING)`)

## 데이터 타입

### 자동 증가 식별자

- `BIGINT` + `AUTO_INCREMENT` (MySQL)
- JPA: `@GeneratedValue(strategy = GenerationType.IDENTITY)`

### 텍스트 데이터

- 짧은 텍스트: `VARCHAR(n)` (길이 제한이 명확한 경우)
- 긴 텍스트: `TEXT` 타입 사용 가능

### 타임스탬프

- `created_at`와 `updated_at`는 필수로 구성
- `DATETIME` 또는 `TIMESTAMP` 타입 사용
- JPA: `@CreatedDate`, `@LastModifiedDate` 사용

### 연관관계 컬럼

- 외래키 컬럼: `{참조 테이블명}_id` 형식 (예: `user_id`, `concert_id`)
- JPA: `@JoinColumn(name = "...")` 명시

## 인덱스 네이밍

- 일반 인덱스: `idx_{table}_{column}` (예: `idx_user_email`)
- 유니크 인덱스: `uidx_{table}_{column}`
- 복합 인덱스: `idx_{table}_{column1}_{column2}`

---

# Redis 컨벤션

## Redis 구성

- Redis Sentinel을 사용한 고가용성 구성
- Master-Slave 복제 구조
- Lettuce 클라이언트 사용
- `REPLICA_PREFERRED` 읽기 전략: Replica 우선, Replica 불가 시 Master에서 읽기

## 설정

- Command Timeout, Connect Timeout, Shutdown Timeout 설정 필수
- 연결 검증 활성화 (`setValidateConnection(true)`)

## 사용 용도

- Refresh Token 캐싱
- 세션 데이터 저장
- 임시 데이터 캐싱

## RedisTemplate 사용

- Key와 Value 모두 String 직렬화 사용
- 커스텀 직렬화가 필요한 경우 별도 설정

---

# Swagger 컨벤션

## 정책

### Docs 인터페이스 패턴 (필수)

- **모든 Controller는 반드시 Docs 인터페이스를 구현하는 방식 사용**
- Controller와 API 문서를 분리하여 가독성 향상
- 인터페이스에 Swagger 어노테이션 정의, Controller는 비즈니스 로직에만 집중

### 언어 규칙

- API 엔드포인트 설명은 **한국어**로 작성
- 내부 주석이나 코드 내 설명도 한국어 사용 가능

## OpenAPI 설정 클래스

Swagger의 전반적인 설정은 `SwaggerConfig` 클래스에서 관리.

### 기본 설정

- API 정보 (제목, 버전, 설명, 연락처)
- 서버 URL (로컬 개발, 운영 등)
- 보안 스키마 (JWT Bearer 인증)

### @Permission 권한 정보 자동 추가

- `addPermissionDescription` 메서드로 API 설명에 필요 권한 자동 추가
- 예: `🔒 **Required Permissions:** GENERAL`

## API 문서 작성 가이드

### Docs 인터페이스 패턴

#### 1. Docs 인터페이스 작성

`api/{domain}/controller/docs/{ControllerName}Docs.java` 경로에 인터페이스 생성

#### 2. Controller 구현

Controller 클래스에서 Docs 인터페이스를 `implements`하여 구현

##### 기본 원칙

- **인터페이스 활용 필수**: Controller와 문서 분리를 위해 반드시 인터페이스 사용
- **한국어 작성**: `summary`와 `description`은 한국어로 작성
- **응답 스키마 정의**: 응답 데이터 클래스를 명시하여 API 호출자가 반환값 구조를 쉽게 이해하도록 함
- **공통 에러 응답**: `@AuthErrorResponses`, `@CommonErrorResponses` 등 커스텀 어노테이션 활용

##### 에러 응답 처리

공통 에러 응답은 커스텀 어노테이션으로 정의하여 재사용성 향상

---

# 코드 스타일 및 베스트 프랙티스

## 메서드 길이

- 가능한 짧게 작성 (30줄 이내 권장)
- 복잡한 로직은 private 메서드로 분리

