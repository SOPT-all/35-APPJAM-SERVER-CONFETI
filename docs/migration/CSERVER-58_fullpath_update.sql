-- CSERVER-58: 기존 row의 *_path 컬럼을 폴더 prefix까지 포함한 fullPath 형태로 일괄 정정
--
-- 배경:
--   s3 file url 개선 시리즈(CSERVER-47~57)로 응답 시점에 CDN 도메인을 path에 결합.
--   본 티켓(CSERVER-58)에서 S3FileHandler.uploadFile 반환값을 fullPath로 변경했으므로
--   신규 row는 자동으로 fullPath 저장. 다만 기존 row는 파일명만 저장된 상태이므로
--   본 SQL로 일괄 정정.
--
-- 폴더 매핑 (FolderPath enum 기준):
--   FolderPath.CONCERT          = 'concert'
--   FolderPath.FESTIVAL         = 'festival'
--   FolderPath.USER             = 'user'
--   FolderPath.TICKET_VENDOR    = 'ticket-vendor'
--   FolderPath.PERFORMANCE_DRAFT = 'performance-draft'
--   FolderPath.POSTER           = 'poster'
--   FolderPath.LOGO             = 'logo'
--   FolderPath.PROFILE          = 'profile'
--
-- 멱등성:
--   각 UPDATE는 `NOT LIKE 'prefix%'` 조건으로 이미 fullPath인 row를 스킵.
--   따라서 여러 번 실행해도 안전 (no-op이 됨).
--
-- 사전 검증 (영향 row 수 확인 권장):
--   SELECT COUNT(*) FROM concerts WHERE poster_path IS NOT NULL AND poster_path NOT LIKE 'concert/poster/%';
--   (다른 컬럼도 동일 패턴으로 확인 후 UPDATE 실행)
--
-- 실행 권장 시점:
--   서버 코드(본 PR)의 운영 release 배포 직후 (또는 직전 임팩트 0인 시점).
--   배포-SQL 간격이 길어지면 신규 row(fullPath) ↔ 기존 row(파일명만) 혼재 상태에서
--   기존 row 응답 URL이 깨질 수 있음.

-- ----------------------------------------------------------------------
-- 1. concerts
-- ----------------------------------------------------------------------
UPDATE concerts
SET poster_path = CONCAT('concert/poster/', poster_path)
WHERE poster_path IS NOT NULL
  AND poster_path NOT LIKE 'concert/poster/%';

-- ----------------------------------------------------------------------
-- 2. festivals (poster)
-- ----------------------------------------------------------------------
UPDATE festivals
SET poster_path = CONCAT('festival/poster/', poster_path)
WHERE poster_path IS NOT NULL
  AND poster_path NOT LIKE 'festival/poster/%';

-- ----------------------------------------------------------------------
-- 3. festivals (logo)
-- ----------------------------------------------------------------------
UPDATE festivals
SET logo_path = CONCAT('festival/logo/', logo_path)
WHERE logo_path IS NOT NULL
  AND logo_path NOT LIKE 'festival/logo/%';

-- ----------------------------------------------------------------------
-- 4. ticket_vendors
-- ----------------------------------------------------------------------
UPDATE ticket_vendors
SET logo_path = CONCAT('ticket-vendor/logo/', logo_path)
WHERE logo_path IS NOT NULL
  AND logo_path NOT LIKE 'ticket-vendor/logo/%';

-- ----------------------------------------------------------------------
-- 5. users
-- ----------------------------------------------------------------------
UPDATE users
SET profile_path = CONCAT('user/profile/', profile_path)
WHERE profile_path IS NOT NULL
  AND profile_path NOT LIKE 'user/profile/%';

-- ----------------------------------------------------------------------
-- 6. performance_drafts (poster)
-- ----------------------------------------------------------------------
UPDATE performance_drafts
SET poster_path = CONCAT('performance-draft/poster/', poster_path)
WHERE poster_path IS NOT NULL
  AND poster_path NOT LIKE 'performance-draft/poster/%';

-- ----------------------------------------------------------------------
-- 7. performance_drafts (logo)
-- ----------------------------------------------------------------------
UPDATE performance_drafts
SET logo_path = CONCAT('performance-draft/logo/', logo_path)
WHERE logo_path IS NOT NULL
  AND logo_path NOT LIKE 'performance-draft/logo/%';

-- ----------------------------------------------------------------------
-- 8. performances (type 컬럼 기준 분기)
--   AdminFacade.createConcert/updateConcert에서 Concert와 Performance에
--   동일한 posterPath를 주입하므로 prefix가 도메인별로 다름.
--   Performance.type은 @Enumerated(EnumType.STRING)으로 'CONCERT'/'FESTIVAL' 저장.
-- ----------------------------------------------------------------------
UPDATE performances
SET poster_path = CONCAT('concert/poster/', poster_path)
WHERE type = 'CONCERT'
  AND poster_path IS NOT NULL
  AND poster_path NOT LIKE 'concert/poster/%';

UPDATE performances
SET poster_path = CONCAT('festival/poster/', poster_path)
WHERE type = 'FESTIVAL'
  AND poster_path IS NOT NULL
  AND poster_path NOT LIKE 'festival/poster/%';
